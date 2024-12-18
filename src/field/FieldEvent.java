package field;

import agents.Agent;
import agents.Patch;

public class FieldEvent {
    public enum Type {
        MOVE,
        UPDATE_PATCH,
        UPDATE_AGENT,
        ADD_PATCH,
        REMOVE_PATCH,
        REMOVE_AGENT
    }

    private final Type eventType;
    private Agent agent;
    private Patch patch;
    private int x;
    private int y;
    private String oldSymbol;

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

    public FieldEvent(int x, int y, String oldSymbol, Patch patch) {
        eventType = Type.UPDATE_PATCH;
        this.x = x;
        this.y = y;
        this.oldSymbol = oldSymbol;
        this.patch = patch;
    }

    public FieldEvent(String oldSymbol, Agent agent) {
        eventType = Type.UPDATE_AGENT;
        this.oldSymbol = oldSymbol;
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

    public Patch getPatch() {
        return patch;
    }

    public String getOldSymbol() {
        return oldSymbol;
    }
}
