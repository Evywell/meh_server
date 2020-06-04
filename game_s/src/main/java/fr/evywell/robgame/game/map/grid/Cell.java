package fr.evywell.robgame.game.map.grid;

import fr.evywell.robgame.game.map.grid.notifier.AbstractDeliverVisitor;
import fr.evywell.robgame.game.map.grid.notifier.GridNotifier;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    public final int x;
    public final int y;
    private final Grid grid;
    private GridNotifier notifier;

    public static final float CELL_WIDTH = 30f;
    public static final float CELL_HALF_WIDTH = CELL_WIDTH / 2f;
    public static final byte CELL_NOTIFY_NONE = 0b0;
    public static final byte CELL_NOTIFY_CHANGED = 0b1;

    public Cell(int x, int y, Grid grid) {
        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    public void visitPlayers(AbstractDeliverVisitor visitor) {
        this.grid.visitPlayers(this, visitor);
    }

    public Cell[] getNeighboring() {
        List<Cell> gridList = new ArrayList<>();
        gridList.add(this); // Add self in the list
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

    public void attachNotifier(GridNotifier notifier) {
        this.notifier = notifier;
    }

    public void detachNotifier() {
        this.notifier = null;
    }

    public boolean hasNotifier() {
        return this.notifier == null;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
