package field;

import agents.Agent;
import agents.Patch;

public class FieldEvent {
    public enum Type {
        MOVE,
        UPDATE,
        ADD,
        REMOVE
    }

    private final Type eventType;
    private final Agent agent;
    private final int x;
    private final int y;

    public FieldEvent(Type eventType, int x, int y, Agent agent) {
        this.eventType = eventType;
        this.x = x;
        this.y = y;
        this.agent = agent;
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
}
