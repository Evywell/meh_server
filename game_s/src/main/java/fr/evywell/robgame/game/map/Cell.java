package fr.evywell.robgame.game.map;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    public final int x;
    public final int y;
    private final Grid grid;
    public static final float CELL_WIDTH = 20f;

    public Cell(int x, int y, Grid grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    public Cell[] getNeighboring() {
        List<Cell> gridList = new ArrayList<>();
        int width = grid.getWidth();
        int height = grid.getHeight();
        // Left
        if (x > 0) {
            Cell left = grid.getCell(x - 1, y);
            gridList.add(left);
            // Top Left
            if (y + 1 < height) {
                Cell topLeft = grid.getCell(x - 1, y + 1);
                gridList.add(topLeft);
            }
            // Bottom Left
            if (y - 1 >= 0) {
                Cell bottomLeft = grid.getCell(x - 1, y - 1);
                gridList.add(bottomLeft);
            }
        }
        // Right
        if (x < width - 1) {
            Cell right = grid.getCell(x + 1, y);
            gridList.add(right);
            // Top Right
            if (y + 1 < height) {
                Cell topRight = grid.getCell(x + 1, y + 1);
                gridList.add(topRight);
            }
            // Bottom Right
            if (y - 1 >= 0) {
                Cell bottomRight = grid.getCell(x + 1, y - 1);
                gridList.add(bottomRight);
            }
        }
        // Top
        if (y + 1 < height) {
            Cell top = grid.getCell(x, y + 1);
            gridList.add(top);
        }
        // Bottom
        if (y - 1 >= 0) {
            Cell bottom = grid.getCell(x, y - 1);
            gridList.add(bottom);
        }

        Cell[] cells = new Cell[gridList.size()];
        return gridList.toArray(cells);
    }

}
