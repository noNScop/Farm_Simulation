package field;

import agents.Agent;
import patches.Patch;

/**
 * The FieldEvent class represents an event that occurs in the simulation field, such as moving an agent,
 * adding or removing a patch, or removing an agent. The event stores the type of the event, the coordinates
 * (x, y) where the event occurs, and either the associated agent or patch involved in the event.

 * It serves the purpose of allowing communication between the agents and the field
 */
public class FieldEvent {
    public enum Type {
        MOVE,
        ADD_PATCH,
        REMOVE_PATCH,
        REMOVE_AGENT
    }

    private final Type eventType;
    private Agent agent;
    private Patch patch;
    private final int x;
    private final int y;

    public FieldEvent(Type eventType, int x, int y, Agent agent) {
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.agent = agent;
    }

    public FieldEvent(Type eventType, int x, int y, Patch patch) {
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.patch = patch;
    }

    public Type getType() {
        return eventType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Agent getAgent() {
        return agent;
    }

    public Patch getPatch() {
        return patch;
    }
}
