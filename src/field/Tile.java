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

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void removeAgent(Agent agent) {
        agents.remove(agent);
    }

    public void emplacePatch(Patch patch) {
        if (this.patch == null) {
            this.patch = patch;
        } else {
            throw new IllegalStateException("Tile already contains a patch. Only one patch is allowed per tile.");
        }
    }

    public void removePatch() {
        patch = null;
    }

    public boolean hasPatch(Class<?> type) {
        if (patch == null) {
            return false;
        } else {
            return type.isInstance(patch);
        }
    }

    public boolean hasAgent(Class<?> type) {
        for (Agent agent : agents) {
            if (type.isInstance(agent)) {
                return true;
            }
        }
        return false;
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

    public String getSymbols(int width) {
        if (agents.isEmpty() && patch == null) {
            return " ".repeat(width);
        } else {
            StringBuilder result = buildSymbols();
            // Adjust the width of a cell
            result.append(" ".repeat(Math.max(0, width - getTileWidth())));
            return result.toString();
        }
    }

    public String getSymbols() {
        return buildSymbols().toString();
    }

    public int getTileWidth() {
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