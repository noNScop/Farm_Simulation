package field;

import agents.Agent;
import patches.Patch;

import java.util.ArrayList;
import java.util.List;

/**
 * The Field class represents the simulation grid where agents and patches are placed. It follows a Singleton pattern,
 * ensuring only one instance of the field exists. The field is structured as a 2D array of Tiles, where each tile
 * may contain an Agents and/or a Patch. The class provides methods to access and modify agents and patches, as well
 * as display the current state of the grid. It also handles the layout of columns for better display clarity.
 */
public class Field {
    private static Field instance;
    private final Tile[][] grid;

    private Field(int rows, int columns) {
        grid = new Tile[rows][columns];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns ; ++j) {
                grid[i][j] = new Tile();
            }
        }
    }

    public static void createInstance(int rows, int columns) {
        // Only creates the instance if it hasn't been created yet, singleton like behaviour
        if (instance == null) {
            instance = new Field(rows, columns);
        }
    }

    public static void destroyInstance() {
        instance = null;
    }

    public static Field getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Singleton Field not initialized. Call init() first.");
        }
        return instance;
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public Agent getAgent(int x, int y, Class<?> type) {
        return grid[y][x].getAgent(type);
    }

    public Patch getPatch(int x, int y) {
        return grid[y][x].getPatch();
    }

    public void addAgent(int x, int y, Agent agent) {
        grid[y][x].addAgent(agent);
    }

    public void removeAgent(int x, int y, Agent agent) {
        grid[y][x].removeAgent(agent);
    }

    public void emplacePatch(int x, int y, Patch patch) {
        grid[y][x].emplacePatch(patch);
    }

    public void removePatch(int x, int y) {
        grid[y][x].removePatch();
    }

    private List<Integer> getColumnWidth() {
        // calculate maximum number of agents in each column, adjust column width accordingly later
        // for better clarity of displayed field, if there is no agent in column put 1
        List<Integer> columnWidth = new ArrayList<>();
        for (int i = 0; i < grid[0].length; ++i) {
            columnWidth.add(1);
            for (int j = 0; j < grid.length; ++j) {
                columnWidth.set(i, Math.max(columnWidth.get(i), grid[j][i].getTileWidth()));
            }
        }
        return columnWidth;
    }

    public void displayGrid() {
        List<Integer> columnWidth = getColumnWidth();
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
                System.out.print('[' + grid[i][j].getSymbols(columnWidth.get(j)) + ']');
            }
            System.out.println();
        }
    }
}