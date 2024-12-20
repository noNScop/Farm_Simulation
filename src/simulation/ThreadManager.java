package simulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {
    private final ReentrantLock lock;
    private final Condition turnStartCondition;
    private final Condition agentsFinishedCondition;
    private int agentsRunning;
    private boolean simulationRunning;
    private boolean turnRunning;

    ThreadManager() {
        lock = new ReentrantLock();
        turnStartCondition = lock.newCondition();
        agentsFinishedCondition = lock.newCondition();

        simulationRunning = true;
        turnRunning = false;
    }

    void setAgentsRunning(int agents) {
        agentsRunning = agents;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Condition getTurnStartCondition() {
        return turnStartCondition;
    }

    void startTurn() {
        lock.lock();
        try {
            turnRunning = true;
            turnStartCondition.signalAll();  // Notify all threads waiting for the turn to start
        } finally {
            lock.unlock();
        }
    }

    public Condition getAgentsFinishedCondition() {
        return agentsFinishedCondition;
    }

    public void decrementAgentsRunning() {
        lock.lock();
        try {
            if (agentsRunning > 0) {
                --agentsRunning;
            }

            if (agentsRunning == 0) {
                turnRunning = false;
                agentsFinishedCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean areAgentsRunning() {
        return agentsRunning != 0;
    }

    void stopSimulation() {
        simulationRunning = false;
    }

    public boolean getSimulationStatus() {
        return simulationRunning;
    }

    public boolean hasTurnStarted() {
        return turnRunning;
    }
}
