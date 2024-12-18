package field;

import java.util.LinkedList;
import java.util.List;

class Tile {
    private List<String> agents = new LinkedList<>();

    public void addAgent(String symbol) {
        agents.add(symbol);
    }

    public void removeAgent(String symbol) {
        agents.remove(symbol);
    }

    public String getAgents(int width) {
        if (agents.isEmpty()) {
            return " ".repeat(width);
        } else {
            StringBuilder result = new StringBuilder();

            for (String agent : agents) {
                result.append(agent);
            }
            // Adjust the width of a cell
            for (int i = 0; i < width - getTileWidth(); ++i) {
                result.append(" ");
            }
            return result.toString();
        }
    }

    public String getAgents() {
        if (agents.isEmpty()) {
            return " ";
        } else {
            StringBuilder result = new StringBuilder();

            for (String agent : agents) {
                result.append(agent);
            }
            return result.toString();
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