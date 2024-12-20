package field;

import agents.Agent;
import patches.Patch;

import java.util.LinkedList;
import java.util.List;

class Tile {
    private Patch patch;
    private final List<Agent> agents;

    Tile() {
        agents = new LinkedList<>();
    }

    void addAgent(Agent agent) {
        agents.add(agent);
    }

    void removeAgent(Agent agent) {
        agents.remove(agent);
    }

    void emplacePatch(Patch patch) {
        this.patch = patch;
    }

    void removePatch() {
        patch = null;
    }

    boolean hasAgent(Class<?> type) {
        for (Agent agent : agents) {
            if (type.isInstance(agent)) {
                return true;
            }
        }
        return false;
    }

    Agent getAgent(Class<?> type) {
        for (Agent agent : agents) {
            if (type.isInstance(agent)) {
                return agent;
            }
        }
        return null;
    }

    boolean hasPatch(Class<?> type) {
        if (patch == null) {
            return false;
        } else {
            return type.isInstance(patch);
        }
    }

    Patch getPatch() {
        return patch;
    }

    private StringBuilder buildSymbols() {
        StringBuilder result = new StringBuilder();

        for (Agent agent : agents) {
            result.append(agent.getSymbol());
        }
        if (patch != null) {
            result.append(patch.getSymbol());
        }
        return result;
    }

    String getSymbols(int width) {
        if (agents.isEmpty() && patch == null) {
            return " ".repeat(width);
        } else {
            StringBuilder result = buildSymbols();
            // Adjust the width of a cell
            result.append(" ".repeat(Math.max(0, width - getTileWidth())));
            return result.toString();
        }
    }

    int getTileWidth() {
        int width = 0;
        for (Agent agent : agents) {
            width += agent.getSymbol().length();
        }
        if (patch != null) {
            width += patch.getSymbol().length();
        }
        return width;
    }
}