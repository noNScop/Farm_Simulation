package simulation;

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
    private final double rabbitSpawnProbability;

    public Simulation(int farmers, double rabbitSpawnProbability) {
        rand = new Random();
        field = Field.getInstance();
        fieldObserver = new FieldObserver();
        fieldHandler = new FieldHandler(fieldObserver);
        this.rabbitSpawnProbability = rabbitSpawnProbability;

        for (int i = 0; i < farmers; ++i) {
            fieldHandler.addAgent(new Farmer(fieldObserver));
        }
        clearTerminal();
        field.displayGrid();
    }

    public void step() {
        if (rabbitSpawnProbability > rand.nextDouble()) {
            fieldHandler.addAgent(new Rabbit(fieldObserver));
        }

        Thread[] threads = new Thread[fieldHandler.numAgents()];
        for (int i = 0; i < fieldHandler.numAgents(); ++i) {
            threads[i] = new Thread(fieldHandler.getAgent(i));
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join(); // Wait for this thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupt status
                System.out.println("Main thread was interrupted.");
            }
        }

        for (int i = 0; i < fieldHandler.numPatches(); ++i) {
            fieldHandler.getPatch(i).update();
        }

        fieldHandler.updateField();
        field.displayGrid();
    }

    public void runSimulation() {
        for (int i = 0; i < 100; ++i) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearTerminal();
            step();
        }
    }

    private static void clearTerminal() {
        // print line in case the terminal doesn't support ANSI escape code e.g. in IDE
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}