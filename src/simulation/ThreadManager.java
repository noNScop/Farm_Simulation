package simulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {
    private final ReentrantLock simulationLock;
    private final ReentrantLock displayLock;
    private final Condition turnStartCondition;
    private final Condition agentsFinishedCondition;
    private final Condition displayUpdateCondition;
    private int agentsRunning;
    private boolean simulationRunning;
    private boolean turnRunning;
    private boolean displayUpToDate;

    ThreadManager() {
        simulationLock = new ReentrantLock();
        turnStartCondition = simulationLock.newCondition();
        agentsFinishedCondition = simulationLock.newCondition();

        displayLock = new ReentrantLock();
        displayUpdateCondition = displayLock.newCondition();

        simulationRunning = true;
        turnRunning = false;
        displayUpToDate = true;
    }

    void setAgentsRunning(int agents) {
        agentsRunning = agents;
    }

    public ReentrantLock getSimulationLock() {
        return simulationLock;
    }

    public Condition getTurnStartCondition() {
        return turnStartCondition;
    }

    void startTurn() {
        simulationLock.lock();
        turnRunning = true;
        turnStartCondition.signalAll();  // Notify all threads waiting for the turn to start
        simulationLock.unlock();
    }

    public Condition getAgentsFinishedCondition() {
        return agentsFinishedCondition;
    }

    public void decrementAgentsRunning() {
        simulationLock.lock();
        if (agentsRunning > 0) {
            --agentsRunning;
        }

        if (agentsRunning == 0) {
            turnRunning = false;
            agentsFinishedCondition.signalAll();
        }
        simulationLock.unlock();
    }

    public boolean areAgentsRunning() {
        return agentsRunning != 0;
    }

    void stopSimulation() {
        simulationRunning = false;
    }

    public boolean hasSimulationStopped() {
        return !simulationRunning;
    }

    public boolean hasTurnStarted() {
        return turnRunning;
    }

    ReentrantLock getDisplayLock() {
        return displayLock;
    }

    Condition getDisplayUpdateCondition() {
        return displayUpdateCondition;
    }

    boolean isDisplayUpToDate() {
        return displayUpToDate;
    }

    void updateDisplay() {
        displayLock.lock();
        displayUpToDate = false;
        displayUpdateCondition.signal();
        displayLock.unlock();
    }

    void displayUpdated() {
        displayUpToDate = true;
    }
}
