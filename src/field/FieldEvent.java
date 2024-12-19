package field;

import agents.Agent;
import patches.Patch;

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
