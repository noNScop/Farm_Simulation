package simulation;

import java.util.List;
import java.util.LinkedList;

import agents.Farmer;

public class Simulation {
    public Field field;
    public List<Farmer> farmers = new LinkedList<>();

    public Simulation(int rows, int columns, int farmers) {
        field = new Field(rows, columns);

        for (int i = 0; i < farmers; ++i) {
            Farmer farmer = new Farmer(rows, columns, i);
            this.farmers.add(farmer);
        }
    }

    public void step() {
        for (Farmer farmer : farmers) {
            field.removeAgent(farmer.getX(), farmer.getY(), farmer.getSymbol());
            farmer.move();
            field.emplaceAgent(farmer.getX(), farmer.getY(), farmer.getSymbol());
        }
        field.displayGrid();
    }

    public void run() {
        for (int i = 0; i < 10; ++i) {
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