package simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import agents.Farmer;

public class Field {
    private Tile[][] grid;

    public Field(int rows, int columns) {
        grid = new Tile[rows][columns];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns ; ++j) {
                grid[i][j] = new Tile();
            }
        }
    }

    public void emplaceAgent(int x, int y, String symbol) {
        grid[y][x].addAgent(symbol);
    }

    public void removeAgent(int x, int y, String symbol) {
        grid[y][x].removeAgent(symbol);
    }

    private List<Integer> getColumnWidth() {
        // calculate maximum number of agents in each column, adjust column width accordingly later
        // for better clarity of displayed field, if there is no agent in column put 1
        List<Integer> maxAgents = new ArrayList<>();
        for (int i = 0; i < grid[0].length; ++i) {
            maxAgents.add(1);
            for (int j = 0; j < grid.length; ++j) {
                maxAgents.set(i, Math.max(maxAgents.get(i), grid[j][i].getAgentsCount()));
            }
        }
        return maxAgents;
    }

    public void displayGrid() {
        List<Integer> columnWidth = getColumnWidth();
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                System.out.print('[' + grid[i][j].getAgents(columnWidth.get(j)) + ']');
            }
            System.out.println();
        }
    }
}