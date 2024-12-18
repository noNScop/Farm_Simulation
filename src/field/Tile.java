package field;

import java.util.LinkedList;
import java.util.List;

class Tile {
    private List<String> agents = new LinkedList<>();

    public void add(String symbol) {
        agents.add(symbol);
    }

    public void remove(String symbol) {
        agents.remove(symbol);
    }

    private StringBuilder getSymbols() {
        StringBuilder result = new StringBuilder();

        for (String agent : agents) {
            result.append(agent);
        }
        return result;
    }

    public String getAgents(int width) {
        if (agents.isEmpty()) {
            return " ".repeat(width);
        } else {
            StringBuilder result = getSymbols();
            // Adjust the width of a cell
            result.append(" ".repeat(Math.max(0, width - getTileWidth())));
            return result.toString();
        }
    }

    public String getAgents() {
        if (agents.isEmpty()) {
            return " ";
        } else {
            return getSymbols().toString();
        }
    }

    public int getTileWidth() {
        int width = 0;
        for (String agent : agents) {
            width += agent.length();
        }
        return width;
    }
}