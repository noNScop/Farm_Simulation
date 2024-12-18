package field;

import agents.Agent;
import agents.Patch;

import java.util.LinkedList;
import java.util.List;

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
        field.emplace(agent.getX(), agent.getY(), agent.getSymbol());
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
        field.emplace(x, y, patch.getSymbol());
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
        int old_x = event.getX();
        int old_y = event.getY();
        field.remove(old_x, old_y, agent.getSymbol());
        field.emplace(agent.getX(), agent.getY(), agent.getSymbol());
    }

    private void updateAgent(FieldEvent event) {
        Agent agent = event.getAgent();
        field.remove(agent.getX(), agent.getY(), event.getOldSymbol());
        field.emplace(agent.getX(), agent.getY(), agent.getSymbol());
    }

    private void updatePatch(FieldEvent event) {
        Patch patch = event.getPatch();
        field.remove(event.getX(), event.getY(), event.getOldSymbol());
        field.emplace(event.getX(), event.getY(), patch.getSymbol());
    }

    public void updateField() {
        while (!fieldObserver.isEmpty()) {
            FieldEvent event = fieldObserver.getNextEvent();

            if (event.getType() == FieldEvent.Type.MOVE) {
                moveAgent(event);
            } else if (event.getType() == FieldEvent.Type.ADD_PATCH) {
                addPatch(event.getX(), event.getY(), event.getPatch());
            } else if (event.getType() == FieldEvent.Type.UPDATE_PATCH) {
                updatePatch(event);
            } else if (event.getType() == FieldEvent.Type.UPDATE_AGENT) {
                updateAgent(event);
            }
        }
    }
}
