package simulation;

import agents.Agent;
import field.FieldHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {
    private final ReentrantLock turnLock;
    private final ReentrantLock agentLock;
    private final Condition turnStartCondition;
    private final Condition agentsFinishedCondition;
    private int agentsRunning;
    private boolean simulationRunning;
    private boolean turnRunning;

    ThreadManager() {
        turnLock = new ReentrantLock();
        agentLock = new ReentrantLock();
        turnStartCondition = turnLock.newCondition();
        agentsFinishedCondition = agentLock.newCondition();

        simulationRunning = true;
        turnRunning = false;
    }

    void setAgentsRunning(int agents) {
        agentsRunning = agents;
    }

    public ReentrantLock getAgentLock() {
        return agentLock;
    }

    public ReentrantLock getTurnLock() {
        return turnLock;
    }

    public Condition getTurnStartCondition() {
        return turnStartCondition;
    }

    void startTurn() {
        turnLock.lock();
        try {
            turnRunning = true;
            turnStartCondition.signalAll();  // Notify all threads waiting for the turn to start
        } finally {
            turnLock.unlock();
        }
    }

    public Condition getAgentsFinishedCondition() {
        return agentsFinishedCondition;
    }

    public void decrementAgentsRunning() {
        agentLock.lock();
        try {
            if (agentsRunning > 0) {
                --agentsRunning;
            }

            if (agentsRunning == 0) {
                turnRunning = false;
                agentsFinishedCondition.signalAll();
            }
        } finally {
            agentLock.unlock();
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
