package simulation;

import field.Field;

public class DisplayManager extends Thread {
    private final Field field;
    private final ThreadManager threadManager;

    DisplayManager(ThreadManager threadManager) {
        field = Field.getInstance();
        this.threadManager = threadManager;
    }

    @Override
    public void run() {
        while (true) {
            threadManager.getDisplayLock().lock();
            try {
                while (threadManager.isDisplayUpToDate()) {
                    threadManager.getDisplayUpdateCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                threadManager.getDisplayLock().unlock();
            }

            if (threadManager.hasSimulationStopped()) {
                return;
            }

            displayGrid();
            displayMenu();
            threadManager.displayUpdated();
        }
    }

    void displayGrid() {
        field.displayGrid();
    }

    void displayMenu() {
        System.out.println();
        System.out.println("+-----------------------------+");
        System.out.println("|       SIMULATION MENU       |");
        System.out.println("+-----------------------------+");
        System.out.println("| 1. Show Legend              |");
        System.out.println("| 2. Terminate Simulation     |");
        System.out.println("+-----------------------------+");
    }
}
