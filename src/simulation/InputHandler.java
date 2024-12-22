package simulation;

import java.util.Scanner;

/**
 * The InputHandler class handles user input in a separate thread during the simulation.
 * It listens for specific commands (e.g., toggling the legend or stopping the simulation)
 * and notifies the relevant managers to take appropriate actions.
 * It also ensures that only valid inputs ("1" or "2") are processed, otherwise prompts the user again.
 */
public class InputHandler extends Thread {
    private final ThreadManager threadManager;
    private final DisplayManager displayManager;
    private final Scanner scanner;
    private boolean stopSimulation;

    InputHandler(ThreadManager threadManager, DisplayManager displayManager) {
        this.threadManager = threadManager;
        this.displayManager = displayManager;
        stopSimulation = false;
        scanner = new Scanner(System.in);
    }

    boolean stopSignal() {
        return stopSimulation;
    }

    public void run() {
        while (true) {
            String input = scanner.nextLine();

            if ("1".equals(input)) {
                displayManager.toggleShowLegend();
            } else if ("2".equals(input)) {
                threadManager.getSimulationLock().lock();
                try {
                    stopSimulation = true;
                } finally {
                    threadManager.getSimulationLock().unlock();
                }
                return;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
            }
        }
    }
}
