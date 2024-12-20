package agents;

import field.Field;
import field.FieldEvent;
import field.FieldObserver;
import simulation.Simulation;
import simulation.ThreadManager;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a movable entity (Agent) capable of performing actions. Each Agent is operating on a separate thread.
 */
public abstract class Agent implements Runnable {
    protected final Random rand;
    private boolean destroyed;
    protected int x;
    protected int y;
    protected final int fieldColumns;
    protected final int fieldRows;
    protected String symbol;
    protected final Field field;
    protected final FieldObserver fieldObserver;
    private final ThreadManager threadManager;
    private final ReentrantLock turnLock;
    private final ReentrantLock agentLock;

    protected Agent (FieldObserver fieldObserver, ThreadManager threadManager) {
        rand = new Random();
        field = Field.getInstance();
        fieldRows = field.getHeight();
        fieldColumns = field.getWidth();

        x = rand.nextInt(fieldColumns);
        y = rand.nextInt(fieldRows);
        this.fieldObserver = fieldObserver;
        this.threadManager = threadManager;
        turnLock = threadManager.getTurnLock();
        agentLock = threadManager.getAgentLock();
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
            turnLock.lock();
            try {
                while(!threadManager.hasTurnStarted()) {
                    threadManager.getTurnStartCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                turnLock.unlock();
            }

            if (!threadManager.getSimulationStatus() || isDestroyed()) {
                break;
            }
            
            step();

            threadManager.decrementAgentsRunning();

            agentLock.lock();
            try {
                while(threadManager.areAgentsRunning()) {
                    threadManager.getAgentsFinishedCondition().await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                agentLock.unlock();
            }
        }
    }

    protected abstract void setSymbol();
    protected abstract void step();

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
