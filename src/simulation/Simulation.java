package simulation;

import agents.Dog;
import agents.Farmer;
import agents.Rabbit;
import field.Field;
import field.FieldManager;
import field.FieldObserver;

import java.util.Random;

/**
 * The Simulation class manages the core logic of the simulation, including initializing agents
 * (Farmers, Dogs, and Rabbits), running the simulation steps, and handling interactions between agents
 * and the field. It coordinates with various components like the field handler, display manager,
 * and input handler to simulate the environment, update the field, and manage agent behavior over multiple steps.
 * It runs in a multithreaded environment and allows for dynamic events like adding new rabbits during the
 * simulation.
 */
public class Simulation {
    private final Random rand;
    public Field field;
    private final FieldObserver fieldObserver;
    private final FieldManager fieldManager;
    private final ThreadManager threadManager;
    private final DisplayManager displayManager;
    private final InputManager inputManager;
    private final double rabbitSpawnProbability;
    private final int offset;

    public Simulation(int farmers, double rabbitSpawnProbability, int offset) {
        rand = new Random();
        field = Field.getInstance();
        this.offset = offset;
        fieldObserver = new FieldObserver();
        fieldManager = new FieldManager(fieldObserver);
        threadManager = new ThreadManager();
        displayManager = new DisplayManager(threadManager);
        inputManager = new InputManager(threadManager, displayManager);

        this.rabbitSpawnProbability = rabbitSpawnProbability;

        for (int i = 0; i < farmers; ++i) {// Start with the semaphore blocked
            Farmer farmer = new Farmer(fieldObserver, threadManager);
            fieldManager.addAgent(farmer);
            fieldManager.addAgent(new Dog(fieldObserver, threadManager, farmer));
        }
        displayManager.clearTerminal();
    }

    private void tryAddRabbit() {
        if (rabbitSpawnProbability > rand.nextDouble()) {
            Rabbit newRabbit = new Rabbit(fieldObserver, threadManager);
            fieldManager.addAgent(newRabbit);

            Thread rabbitThread = new Thread(newRabbit);
            rabbitThread.setDaemon(true);
            rabbitThread.start();
        }
    }

    public void step() {
        // Ensure that the terminal display is not updated during the update of the field
        threadManager.getDisplayLock().lock();
        try {
            threadManager.startTurn();

            threadManager.getSimulationLock().lock();
            try {
                while(threadManager.areAgentsRunning()) {
                    // Wait for all agents to finish
                    threadManager.getAgentsFinishedCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                threadManager.getSimulationLock().unlock();
            }

            // Update all patches
            for (int i = 0; i < fieldManager.numPatches(); ++i) {
                fieldManager.getPatch(i).update();
            }

            // Handle all field events
            fieldManager.updateField();
        } finally {
            threadManager.getDisplayLock().unlock();
        }
        threadManager.updateDisplay();
    }

    public void runSimulation() {
        // Initialize threads for existing agents
        for (int i = 0; i < fieldManager.numAgents(); ++i) {
            Thread thread = new Thread(fieldManager.getAgent(i));
            thread.setDaemon(true);
            thread.start();
        }

        displayManager.setDaemon(true);
        displayManager.start();
        // Display the initial state of the field
        threadManager.updateDisplay();

        inputManager.setDaemon(true);
        inputManager.start();

        while (true) { // Simulation loop
            try {
                Thread.sleep(offset); // Wait for 1 second before next step
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            threadManager.getSimulationLock().lock();
            if (inputManager.stopSignal()) {
                threadManager.getSimulationLock().unlock();
                break;
            }
            threadManager.getSimulationLock().unlock();

            displayManager.clearTerminal();

            // Dynamically add new Rabbit agents
            tryAddRabbit();

            // set the current number of agents
            threadManager.setAgentsRunning(fieldManager.numAgents());

            // Perform the simulation step
            step();
        }
        System.out.println("Stopping simulation...");
        threadManager.stopSimulation();
        threadManager.startTurn(); // this will terminate all Agent threads
        threadManager.updateDisplay(); // this will terminate the display thread
    }
}