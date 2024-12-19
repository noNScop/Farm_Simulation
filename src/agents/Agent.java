package agents;

import field.Field;
import field.FieldEvent;
import field.FieldObserver;

import java.util.Random;

/**
 * Represents a movable entity (Agent) capable of performing actions. Each Agent is operating on a separate thread.
 */
public abstract class Agent implements Runnable {
    private final Random rand;
    protected int x;
    protected int y;
    private final int fieldColumns;
    private final int fieldRows;
    protected String symbol;
    protected final Field field;
    protected final FieldObserver fieldObserver;

    protected Agent (FieldObserver fieldObserver) {
        rand = new Random();
        field = Field.getInstance();
        fieldRows = field.getHeight();
        fieldColumns = field.getWidth();

        x = rand.nextInt(fieldColumns);
        y = rand.nextInt(fieldRows);
        setSymbol();
        this.fieldObserver = fieldObserver;
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

    protected abstract void setSymbol();

    protected void move() {
//        Store position of an agent before its move
        FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
        // Randomly choose whether to move on the X axis (0) or Y axis (1)
        int axis = rand.nextInt(2);  // Generates either 0 or 1
        int value = rand.nextInt(2) * 2 - 1; // Generates either -1 or 1

        if (axis == 0) {
            if (x + value < fieldColumns && x + value >= 0) {
                x += value;
            } else {
//                If the agent is about to leave the field, move in opposite direction
                x -= value;
            }
        } else {
            if (y + value < fieldRows && y + value >= 0) {
                y += value;
            } else {
//                If the agent is about to leave the field, move in opposite direction
                y -= value;
            }
        }
//        Add the event once the move operation succeeds
        fieldObserver.addEvent(event);
    }
}
