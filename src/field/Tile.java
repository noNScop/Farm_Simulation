package field;

import agents.Agent;
import patches.Patch;

import java.util.LinkedList;
import java.util.List;

/**
 * The Tile class represents a single cell in the field grid that can contain multiple agents and/or a patch.
 * Each tile can hold a patch (e.g., Carrot, DamagedLand) and a list of agents that occupy the tile. The class
 * provides methods to manipulate agents and patches within a tile, including adding/removing agents, placing/removing
 * patches, and retrieving information about the tile's contents.

 * Key functionalities:
 * 1. Adding or removing agents from the tile.
 * 2. Placing and removing patches from the tile.
 * 3. Retrieving agents of a specified type.
 * 4. Constructing the string representation of the tile with symbols for its contents.
 * 5. Calculating the width of the tile based on the length of the symbols of its agents and patch.
 */
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

    Agent getAgent(Class<?> type) {
        for (Agent agent : agents) {
            if (type.isInstance(agent)) {
                return agent;
            }
        }
        return null;
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