package field;

import agents.Agent;
import patches.Patch;

import java.util.LinkedList;
import java.util.List;

/**
 * The FieldHandler class manages interactions between agents and patches in the field.
 * It provides methods to add, remove, and retrieve agents and patches from the field.
 * Additionally, it is responsible for updating the state of the field based on events
 * (such as agent movements and patch additions/removals) provided by the fieldObserver.

 * This class also ensures that all operations on agents and patches are properly reflected
 * in the underlying field grid, providing synchronization between agent state and the environment.
 */
public class FieldHandler {
    private final FieldObserver fieldObserver;
    private final Field field;
    private final List<Agent> agents = new LinkedList<>();
    private final List<Patch> patches = new LinkedList<>();

    public FieldHandler(FieldObserver fieldObserver) {
        this.fieldObserver = fieldObserver;
        field = Field.getInstance();
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
        field.addAgent(agent.getX(), agent.getY(), agent);
    }

    public void removeAgent(int x, int y, Agent agent) {
        agent.destroy();
        agents.remove(agent);
        field.removeAgent(x, y, agent);
    }

    public Agent getAgent(int idx) {
        if (idx < agents.size()) {
            return agents.get(idx);
        } else {
            throw new IndexOutOfBoundsException("Index " + idx + " is out of bounds for agents list.");
        }
    }

    public int numAgents() {
        return agents.size();
    }

    public void addPatch(int x, int y, Patch patch) {
        patches.add(patch);
        field.emplacePatch(x, y, patch);
    }

    public void removePatch(int x, int y, Patch patch) {
        patches.remove(patch);
        field.removePatch(x, y);
    }

    public Patch getPatch(int idx) {
        if (idx < patches.size()) {
            return patches.get(idx);
        } else {
            throw new IndexOutOfBoundsException("Index " + idx + " is out of bounds for patches list.");
        }
    }

    public int numPatches() {
        return patches.size();
    }

    private void moveAgent(FieldEvent event) {
        Agent agent = event.getAgent();
        if (agent.isDestroyed()) {
            return;
        }

        int old_x = event.getX();
        int old_y = event.getY();
        field.removeAgent(old_x, old_y, agent);
        field.addAgent(agent.getX(), agent.getY(), agent);
    }

    public void updateField() {
        while (!fieldObserver.isEmpty()) {
            FieldEvent event = fieldObserver.getNextEvent();

            switch (event.getType()) {
                case MOVE -> moveAgent(event);
                case ADD_PATCH -> addPatch(event.getX(), event.getY(), event.getPatch());
                case REMOVE_PATCH -> removePatch(event.getX(), event.getY(), event.getPatch());
                case REMOVE_AGENT -> removeAgent(event.getX(), event.getY(), event.getAgent());
            }
        }
    }
}
