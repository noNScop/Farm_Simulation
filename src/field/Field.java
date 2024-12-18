package field;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private static Field instance;
    private Tile[][] grid;

    private Field(int rows, int columns) {
        grid = new Tile[rows][columns];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns ; ++j) {
                grid[i][j] = new Tile();
            }
        }
    }

    public static void createInstance(int rows, int columns) {
        // Only creates the instance if it hasn't been created yet
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

    public Boolean hasCarrots(int x, int y) {
        if (grid[y][x].getAgents().contains("C")) {
            return true;
        } else {
            return false;
        }
    }

    public void emplace(int x, int y, String symbol) {
        grid[y][x].add(symbol);
    }

    public void remove(int x, int y, String symbol) {
        grid[y][x].remove(symbol);
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
                System.out.print('[' + grid[i][j].getAgents(columnWidth.get(j)) + ']');
            }
            System.out.println();
        }
    }
}