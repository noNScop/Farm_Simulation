package agents;

import java.util.Random;
import field.Field;
import field.FieldEvent;
import field.FieldObserver;

public class Farmer implements Agent {
    private Random rand;
    private int x;
    private int y;
    private final int fieldColumns;
    private final int fieldRows;
    private String symbol;
    private Field field;
    private FieldObserver fieldObserver;

    public Farmer(FieldObserver fieldObserver) {
        rand = new Random();
        field = Field.getInstance();
        fieldRows = field.getHeight();
        fieldColumns = field.getWidth();

        x = rand.nextInt(fieldColumns);
        y = rand.nextInt(fieldRows);
        symbol = "F";
        this.fieldObserver = fieldObserver;
    }

    @Override
    public void run() {
//        if (field.hasCarrots(x, y)) {
//            move();
//        } else {
//                TODO carrot thing
//        }
        move();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    private void setSymbol() {

    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public void move() {
        FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
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
//        Add the event once the move operation succeeds
        fieldObserver.addEvent(event);
    }

    public void plantCarrots() {
        Carrot carrots = new Carrot(getX(), getY());

    }
}