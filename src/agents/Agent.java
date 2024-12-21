package agents;

import field.Field;
import field.FieldEvent;
import field.FieldObserver;
import simulation.ThreadManager;

import java.util.Random;

/**
 * Represents a movable entity (Agent) capable of performing actions. Each Agent is operating on a separate thread.
 */
public abstract class Agent implements Runnable {
    protected final Random rand;
    private boolean destroyed;
    protected int x;
    protected int y;
    protected String symbol;
    protected final Field field;
    protected final FieldObserver fieldObserver;
    private final ThreadManager threadManager;

    protected Agent (FieldObserver fieldObserver, ThreadManager threadManager) {
        rand = new Random();
        field = Field.getInstance();

        x = rand.nextInt(field.getWidth());
        y = rand.nextInt(field.getHeight());
        this.fieldObserver = fieldObserver;
        this.threadManager = threadManager;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
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

    public void run() {
        while (true) {
            threadManager.getSimulationLock().lock();
            try {
                while(!threadManager.hasTurnStarted()) {
                    // First wait for the turn to begin
                    threadManager.getTurnStartCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                threadManager.getSimulationLock().unlock();
            }

            // If agent has been removed or the simulation has come to an end, terminate the thread
            if (threadManager.hasSimulationStopped() || isDestroyed()) {
                return;
            }

            // Agent starts its turn
            step();

            threadManager.decrementAgentsRunning();

            threadManager.getSimulationLock().lock();
            try {
                while(threadManager.areAgentsRunning()) {
                    // Wait for all the agents to end
                    threadManager.getAgentsFinishedCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                threadManager.getSimulationLock().unlock();
            }
        }
    }

    protected abstract void setSymbol();
    // step() is a function that implements agent logic for each turn
    protected abstract void step();

    protected void move() {
        // Store position of an agent before its move
        FieldEvent event = new FieldEvent(FieldEvent.Type.MOVE, x, y, this);
        // Randomly choose whether to move on the X axis (0) or Y axis (1)
        int axis = rand.nextInt(2);  // Generates either 0 or 1
        int value = rand.nextInt(2) * 2 - 1; // Generates either -1 or 1

        if (axis == 0) {
            if (x + value < field.getWidth() && x + value >= 0) {
                x += value;
            } else {
                // If the agent is about to leave the field, move in opposite direction
                x -= value;
            }
        } else {
            if (y + value < field.getHeight() && y + value >= 0) {
                y += value;
            } else {
                // If the agent is about to leave the field, move in opposite direction
                y -= value;
            }
        }
        // Add the event once the move operation succeeds
        fieldObserver.addEvent(event);
    }
}
