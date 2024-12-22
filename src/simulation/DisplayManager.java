package simulation;

import field.Field;

/**
 * The DisplayManager class is responsible for handling the visual output of the simulation.
 * It runs as a separate thread to manage real-time updates and interaction during the simulation.

 * Key responsibilities:
 * - Display the simulation grid.
 * - Display a simulation menu with options to toggle the legend or terminate the simulation.
 * - Optionally display a legend explaining grid symbols.
 * - Respond to input events triggered by the user through the ThreadManager.

 * Methods:
 * - run(): Continuously updates the display based on simulation state.
 * - toggleShowLegend(): Toggles the visibility of the legend in the display.
 * - clearTerminal(): Clears the terminal output for better user experience.
 * - displayGrid(): Outputs the current state of the simulation field.
 * - displayMenu(): Displays the interactive menu with current options.
 * - displayLegend(): Displays the legend of grid symbols.

 * Thread-Safety:
 * - Utilizes the locks and conditions from ThreadManager to synchronize updates between threads
 *   and ensure the display is correctly updated during the simulation.
 */
public class DisplayManager extends Thread {
    private final Field field;
    private final ThreadManager threadManager;
    private boolean showLegend;

    DisplayManager(ThreadManager threadManager) {
        field = Field.getInstance();
        this.threadManager = threadManager;
        showLegend = false;
    }

    @Override
    public void run() {
        while (true) {
            threadManager.getDisplayLock().lock();
            try {
                while (threadManager.isDisplayUpToDate()) {
                    threadManager.getDisplayUpdateCondition().await();
                }

                if (threadManager.hasSimulationStopped()) {
                    return;
                }

                displayGrid();
                displayMenu();
                if (showLegend) {
                    displayLegend();
                }
                threadManager.displayUpdated();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                threadManager.getDisplayLock().unlock();
            }
        }
    }

    private void displayGrid() {
        field.displayGrid();
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("+------------------------------------------+");
        System.out.println("|             SIMULATION  MENU             |");
        System.out.println("+------------------------------------------+");
        if (showLegend) {
            System.out.println("| 1. Hide Legend                           |");
        } else {
            System.out.println("| 1. Show Legend                           |");
        }
        System.out.println("| 2. Terminate Simulation                  |");
        System.out.println("+------------------------------------------+");
    }

    private void displayLegend() {
        System.out.println();
        System.out.println("+------------------------------------------+");
        System.out.println("|                  LEGEND                  |");
        System.out.println("+------------------------------------------+");
        System.out.println("| F    - Farmer                            |");
        System.out.println("| D    - Dog                               |");
        System.out.println("| D!   - Dog hunting rabbit                |");
        System.out.println("| R    - Rabbit                            |");
        System.out.println("| R(E) - Rabbit is eating                  |");
        System.out.println("| ,    - Carrot seed                       |");
        System.out.println("| C    - Carrot                            |");
        System.out.println("| X    - Damaged Land                      |");
        System.out.println("| X(t) - Damaged Land, t turns left to fix |");
        System.out.println("+------------------------------------------+");
    }

    void toggleShowLegend() {
        showLegend = !showLegend;
        threadManager.getDisplayLock().lock();
        clearTerminal();
        displayGrid();
        displayMenu();
        if (showLegend) {
            displayLegend();
        }
        threadManager.getDisplayLock().unlock();
    }

    void clearTerminal() {
        // print line in case the terminal doesn't support ANSI escape code e.g. in IDE
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
