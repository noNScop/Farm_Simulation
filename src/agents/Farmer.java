package agents;

import java.util.Random;
import simulation.Field;

public class Farmer {
    private final int id;
    private int x;
    private int y;
    private final int fieldColumns;
    private final int fieldRows;
    private String symbol;

    public Farmer(int rows, int columns, int id) {
        Random rand = new Random();
        fieldRows = rows;
        fieldColumns = columns;
        x = rand.nextInt(columns);;
        y = rand.nextInt(rows);;
        this.id = id;
        symbol = "F";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSymbol() {
        return symbol;
    }

    public void move() {
        Random rand = new Random();

        // Randomly choose whether to move on the X axis (0) or Y axis (1)
        int axis = rand.nextInt(2);  // Generates either 0 or 1
        int value = rand.nextInt(2) * 2 - 1; // Generates either -1 or 1

        if (axis == 0) {
            if (x + value < fieldColumns && x + value >= 0) {
                x += value;
            }
        } else {
            if (y + value < fieldRows && y + value >= 0) {
                y += value;
            }
        }
    }
}