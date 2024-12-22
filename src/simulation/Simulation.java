package simulation;

import agents.Dog;
import agents.Farmer;
import agents.Rabbit;
import field.Field;
import field.FieldHandler;
import field.FieldObserver;

import java.util.Random;

public class Simulation {
    private final Random rand;
    public Field field;
    private final FieldObserver fieldObserver;
    private final FieldHandler fieldHandler;
    private final ThreadManager threadManager;
    private final DisplayManager displayManager;
    private final InputHandler inputHandler;
    private final double rabbitSpawnProbability;
    private final int offset;

    public Simulation(int farmers, double rabbitSpawnProbability, int offset) {
        rand = new Random();
        field = Field.getInstance();
        this.offset = offset;
        fieldObserver = new FieldObserver();
        fieldHandler = new FieldHandler(fieldObserver);
        threadManager = new ThreadManager();
        displayManager = new DisplayManager(threadManager);
        inputHandler = new InputHandler(threadManager, displayManager);

        this.rabbitSpawnProbability = rabbitSpawnProbability;

        for (int i = 0; i < farmers; ++i) {// Start with the semaphore blocked
            Farmer farmer = new Farmer(fieldObserver, threadManager);
            fieldHandler.addAgent(farmer);
            fieldHandler.addAgent(new Dog(fieldObserver, threadManager, farmer));
        }
        displayManager.clearTerminal();
    }

    private void tryAddRabbit() {
        if (rabbitSpawnProbability > rand.nextDouble()) {
            Rabbit newRabbit = new Rabbit(fieldObserver, threadManager);
            fieldHandler.addAgent(newRabbit);

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
            for (int i = 0; i < fieldHandler.numPatches(); ++i) {
                fieldHandler.getPatch(i).update();
            }

            // Handle all field events
            fieldHandler.updateField();
        } finally {
            threadManager.getDisplayLock().unlock();
        }
        threadManager.updateDisplay();
    }

    public void runSimulation() {
        // Initialize threads for existing agents
        for (int i = 0; i < fieldHandler.numAgents(); ++i) {
            Thread thread = new Thread(fieldHandler.getAgent(i));
            thread.setDaemon(true);
            thread.start();
        }

        displayManager.setDaemon(true);
        displayManager.start();
        // Display the initial state of the field
        threadManager.updateDisplay();

        inputHandler.setDaemon(true);
        inputHandler.start();

        while (true) { // Simulation loop
            try {
                Thread.sleep(offset); // Wait for 1 second before next step
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            threadManager.getSimulationLock().lock();
            if (inputHandler.stopSignal()) {
                threadManager.getSimulationLock().unlock();
                break;
            }
            threadManager.getSimulationLock().unlock();

            displayManager.clearTerminal();

            // Dynamically add new Rabbit agents
            tryAddRabbit();

            // set the current number of agents
            threadManager.setAgentsRunning(fieldHandler.numAgents());

            // Perform the simulation step
            step();
        }
        System.out.println("Stopping simulation...");
        threadManager.stopSimulation();
        threadManager.startTurn(); // this will terminate all Agent threads
        threadManager.updateDisplay(); // this will terminate the display thread
    }
}