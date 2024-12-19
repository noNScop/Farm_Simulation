package agents;

import java.util.Random;
import field.Field;
import field.FieldEvent;
import field.FieldObserver;
import patches.Carrot;

public class Farmer implements Agent {
    private final Random rand;
    private int x;
    private int y;
    private final int fieldColumns;
    private final int fieldRows;
    private final String symbol;
    private final Field field;
    private final FieldObserver fieldObserver;

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
        if (field.hasPatch(x, y, Carrot.class)) {
            move();
        } else {
            plantCarrots();
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public void move() {
//        Store position of an agent before its move
        FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
        // Randomly choose whether to move on the X axis (0) or Y axis (1)
        int axis = rand.nextInt(2);  // Generates either 0 or 1
        int value = rand.nextInt(2) * 2 - 1; // Generates either -1 or 1

        if (axis == 0) {
            if (x + value < fieldColumns && x + value >= 0) {
                x += value;
            } else {
                x -= value;
            }
        } else {
            if (y + value < fieldRows && y + value >= 0) {
                y += value;
            } else {
                y -= value;
            }
        }
//        Add the event once the move operation succeeds
        fieldObserver.addEvent(event);
    }

    public void plantCarrots() {
        fieldObserver.addEvent(new FieldEvent(FieldEvent.Type.ADD_PATCH, x, y, new Carrot(fieldObserver, x, y)));
    }
}