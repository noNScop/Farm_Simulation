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
    private final double rabbitSpawnProbability;

    public Simulation(int farmers, double rabbitSpawnProbability) {
        rand = new Random();
        field = Field.getInstance();
        fieldObserver = new FieldObserver();
        fieldHandler = new FieldHandler(fieldObserver);
        threadManager = new ThreadManager();

        this.rabbitSpawnProbability = rabbitSpawnProbability;

        for (int i = 0; i < farmers; ++i) {// Start with the semaphore blocked
            Farmer farmer = new Farmer(fieldObserver, threadManager);
            fieldHandler.addAgent(farmer);
            fieldHandler.addAgent(new Dog(fieldObserver, threadManager, farmer));
        }
        clearTerminal();
        field.displayGrid();
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
        threadManager.startTurn();

        threadManager.getLock().lock();
        try {
            while(threadManager.areAgentsRunning()) {
                threadManager.getAgentsFinishedCondition().await();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        } finally {
            threadManager.getLock().unlock();
        }

        for (int i = 0; i < fieldHandler.numPatches(); ++i) {
            fieldHandler.getPatch(i).update();
        }

        fieldHandler.updateField();
        field.displayGrid();
    }

    public void runSimulation() {
        // Initialize threads for existing agents
        for (int i = 0; i < fieldHandler.numAgents(); ++i) {
            Thread thread = new Thread(fieldHandler.getAgent(i));
            thread.setDaemon(true);
            thread.start();
        }

        for (int i = 0; i < 100; ++i) { // Simulation loop
            try {
                Thread.sleep(1000); // Wait for 1 second before next step
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clearTerminal();

            // Dynamically add new Rabbit agents
            tryAddRabbit();

            // set the current number of agents
            threadManager.setAgentsRunning(fieldHandler.numAgents());

            // Perform the simulation step
            step();
        }

        threadManager.stopSimulation();
        threadManager.startTurn(); // this will terminate all Agent threads
    }

    private static void clearTerminal() {
        // print line in case the terminal doesn't support ANSI escape code e.g. in IDE
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}