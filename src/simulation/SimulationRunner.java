package simulation;

import field.Field;

import java.io.*;
import java.util.Scanner;

public class SimulationRunner {
    private static final Scanner scanner = new Scanner(System.in);
    private static int fieldWidth = 10;
    private static int fieldHeight = 10;
    private static int farmerCount = 2;
    private static double rabbitSpawnProbability = 0.2;
    private static int offset = 1000;

    public static void main(String[] args) {
        while (true) {
            showMenu();
            String input = scanner.nextLine();

            if ("1".equals(input)) {
                adjustSimulationSettings();
            } else if ("2".equals(input)) {
                loadSimulationSettings();
            } else if ("3".equals(input)) {
                saveSimulationSettings();
            } else if ("4".equals(input)) {
                startSimulation();
            } else if ("5".equals(input)) {
                System.out.println("Exiting the program...");
                return;
            }
        }
    }

    private static void showMenu() {
        System.out.println("+------------------------------------------+");
        System.out.println("|                MAIN  MENU                |");
        System.out.println("+------------------------------------------+");
        System.out.println("| 1. Adjust Simulation Settings            |");
        System.out.println("| 2. Load Simulation Settings              |");
        System.out.println("| 3. Save Simulation Settings              |");
        System.out.println("| 4. Run Simulation                        |");
        System.out.println("| 5. Exit The Program                      |");
        System.out.println("+------------------------------------------+");
    }

    private static void adjustSimulationSettings() {
        clearTerminal();

        String fieldWidthPrompt = "Enter field width (current: " + fieldWidth + "): ";
        fieldWidth = getIntInput(fieldWidthPrompt, fieldWidth, 1, 100);

        String fieldHeightsPrompt = "Enter field height (current: " + fieldHeight + "): ";
        fieldHeight = getIntInput(fieldHeightsPrompt, fieldHeight, 1, 100);

        String farmerCountPrompt = "Enter farmer count (current: " + farmerCount + "): ";
        farmerCount = getIntInput(farmerCountPrompt, farmerCount, 1, 1000);

        String rabbitSpawnPrompt = "Enter rabbit spawn probability (current: " + rabbitSpawnProbability + "): ";
        rabbitSpawnProbability = getDoubleInput(rabbitSpawnPrompt, rabbitSpawnProbability, 0.0, 1.0);

        String offsetPrompt = "Enter pause time between simulation steps [ms] (current: " + offset + "): ";
        offset = getIntInput(offsetPrompt, offset, 50, Integer.MAX_VALUE);

        System.out.println("+------------------------------------------+");
        System.out.println("Settings updated:");
        System.out.println("Field width: " + fieldWidth);
        System.out.println("Field height: " + fieldHeight);
        System.out.println("Farmer count: " + farmerCount);
        System.out.println("Rabbit spawn probability: " + rabbitSpawnProbability);
        System.out.println("Pause time between simulation steps [ms]: " + offset);
        System.out.println("+------------------------------------------+");
    }

    private static int getIntInput(String prompt, int current, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isBlank()) return current; // Default to current value if input is blank
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value; // Valid input
                System.out.println("Invalid input. Please enter an integer between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double getDoubleInput(String prompt, double current, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isBlank()) return current; // Default to current value if input is blank
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) return value; // Valid input
                System.out.println("Invalid input. Please enter a double between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double.");
            }
        }
    }

    private static void loadSimulationSettings() {
        String fileName;

        while (true) {
            System.out.print("Enter a valid file name (or press Enter to cancel): ");
            fileName = scanner.nextLine().trim();

            if (fileName.isBlank()) {
                System.out.println("Operation canceled.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        switch (parts[0]) {
                            case "fieldWidth" -> {
                                int value = Integer.parseInt(parts[1]);
                                if (value < 1 || value > 100) {
                                    throw new NumberFormatException();
                                }
                                fieldWidth = value;
                            } case "fieldHeight" -> {
                                int value = Integer.parseInt(parts[1]);
                                if (value < 1 || value > 100) {
                                    throw new NumberFormatException();
                                }
                                fieldHeight = value;
                            } case "farmerCount" -> {
                                int value = Integer.parseInt(parts[1]);
                                if (value < 1 || value > 1000) {
                                    throw new NumberFormatException();
                                }
                                farmerCount = value;
                            } case "rabbitSpawnProbability" -> {
                                double value = Double.parseDouble(parts[1]);
                                if (value < 0.0 || value > 1.0) {
                                    throw new NumberFormatException();
                                }
                                rabbitSpawnProbability = value;
                            } case "offset" -> {
                                int value = Integer.parseInt(parts[1]);
                                if (value < 50) {
                                    throw new NumberFormatException();
                                }
                                offset = value;
                            } default -> System.out.println("Unknown parameter: " + parts[0]);
                        }
                    }
                }
                return;
            } catch (IOException e) {
                System.err.println("Error reading from file: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid format in file.");
            }
        }
    }

    private static void saveSimulationSettings() {
        String fileName;

        while (true) {
            System.out.print("Enter a valid file name (or press Enter to cancel): ");
            fileName = scanner.nextLine().trim();

            if (fileName.isBlank()) {
                System.out.println("Operation canceled.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("fieldWidth=" + fieldWidth);
                writer.newLine();
                writer.write("fieldHeight=" + fieldHeight);
                writer.newLine();
                writer.write("farmerCount=" + farmerCount);
                writer.newLine();
                writer.write("rabbitSpawnProbability=" + rabbitSpawnProbability);
                writer.newLine();
                writer.write("offset=" + offset);
                writer.newLine();
                System.out.println("Parameters saved to " + System.getProperty("user.dir") + "/" + fileName);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
                continue;
            }
            return;
        }
    }

    private static void startSimulation() {
        // Create a Singleton Field
        Field.createInstance(fieldHeight, fieldWidth);
        Simulation simulation = new Simulation(farmerCount, rabbitSpawnProbability, offset);
        simulation.runSimulation();
        // Ensure the field is reset after each simulation
        Field.destroyInstance();
    }

    private static void clearTerminal() {
        // print line in case the terminal doesn't support ANSI escape code e.g. in IDE
        System.out.println();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}