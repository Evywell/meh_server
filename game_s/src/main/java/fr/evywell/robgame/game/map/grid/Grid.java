package fr.evywell.robgame.game.map.grid;

import fr.evywell.common.maths.Vector3;
import fr.evywell.robgame.game.entities.GameObject;
import fr.evywell.robgame.game.entities.ObjectGuid;
import fr.evywell.robgame.game.entities.Player;
import fr.evywell.robgame.game.entities.Unit;
import fr.evywell.robgame.game.map.grid.notifier.AbstractDeliverVisitor;
import fr.evywell.robgame.game.map.grid.notifier.GridNotifier;

import java.util.HashMap;

public class Grid {

    private Cell[][] cells;
    private java.util.Map<Integer, GridNotifier> notifiers;
    public final int width, height;

    public static final int MAX_GRID_LENGTH = 40; // Nombre de cellule maximum sur une ligne/colonne
    public static final float MAX_GRID_WIDTH = MAX_GRID_LENGTH * Cell.CELL_WIDTH;
    public static final float HALF_MAX_GRID_WIDTH = MAX_GRID_WIDTH / 2;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.notifiers = new HashMap<>();
        this.notifiers.put(
                GridNotifier.GridNotifierType.PLAYER,
                new GridNotifier(this, GridNotifier.GridNotifierType.PLAYER)
        );
        this.notifiers.put(
                GridNotifier.GridNotifierType.CREATURE,
                new GridNotifier(this, GridNotifier.GridNotifierType.CREATURE)
        );
        this.notifiers.put(
                GridNotifier.GridNotifierType.GAMEOBJECT,
                new GridNotifier(this, GridNotifier.GridNotifierType.GAMEOBJECT)
        );
        this.cells = new Cell[width][height];
    }

    public void addToGrid(GameObject go) {
        if (go instanceof Player) {
            this.notifiers.get(GridNotifier.GridNotifierType.PLAYER).listen(go);
        } else if (go instanceof Unit) {
            this.notifiers.get(GridNotifier.GridNotifierType.CREATURE).listen(go);
        } else {
            this.notifiers.get(GridNotifier.GridNotifierType.GAMEOBJECT).listen(go);
        }
        Cell cell = getCellFromGameObject(go);
        cell.addToCell(go);
        go.setCell(cell);
    }

    public void removeToGrid(GameObject go) {
        if (go instanceof Player) {
            this.notifiers.get(GridNotifier.GridNotifierType.PLAYER).dismiss(go);
        } else if (go instanceof Unit) {
            this.notifiers.get(GridNotifier.GridNotifierType.CREATURE).dismiss(go);
        } else {
            this.notifiers.get(GridNotifier.GridNotifierType.GAMEOBJECT).dismiss(go);
        }
    }

    public void visitPlayers(Cell cell, AbstractDeliverVisitor visitor) {
        // Je pense que ça peut être optimisé
        this.notifiers.get(GridNotifier.GridNotifierType.PLAYER).emit(cell, visitor);
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }
        return this.cells[x][y];
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void fillGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.cells[x][y] = new Cell(x, y, this);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell[] c = this.cells[x][y].getNeighboring();
            }
        }
    }

    public Cell getCellFromGameObject(GameObject go) {
        float middleX = width / 2f; // 2.5
        float middleY = height / 2f; // 2.5
        if (go.pos_x < 0) {
            middleX *= -1;
        }
        if (go.pos_z < 0) {
            middleY *= -1;
        }
        int gridXMiddle = (int) Math.ceil((Math.abs(go.pos_x) + Cell.CELL_HALF_WIDTH) / Cell.CELL_WIDTH);
        int gridYMiddle = (int) Math.ceil((Math.abs(go.pos_z) + Cell.CELL_HALF_WIDTH) / Cell.CELL_WIDTH);
        int gridX = (int) Math.abs(Math.floor(gridXMiddle + middleX)) - ((go.pos_x >= 0) ? 1 : 0);
        int gridY = (int) Math.abs(Math.floor(gridYMiddle + middleY)) - ((go.pos_y >= 0) ? 1 : 0);

        return this.getCell(gridX, gridY);
    }

    public void moveGameObject(GameObject go) {
        Cell originCell = go.getCell();
        Cell finalCell = this.getCellFromGameObject(go);
        if (finalCell.equals(originCell)) {
            // Les deux cellules sont identiques, il n'y a rien à faire
            return;
        }
        originCell.removeToCell(go);
        finalCell.addToCell(go);
        go.setCell(finalCell);
    }

    public static boolean isValidPosition(Vector3 v, float orientation) {
        return isValidPosition(v.x, v.y, v.z, orientation);
    }

    public static boolean isValidPosition(float x, float y, float z, float orientation) {
        return isValidPosition(x) && isValidPosition(y) && isValidPosition(z) && isValidPosition(orientation);
    }

    public static boolean isValidPosition(float x) {
        return Float.isFinite(x) && x <= HALF_MAX_GRID_WIDTH ; // Est-ce que le point est dans la plus grande map possible
    }

    private void addListenersForPlayer(Cell cell, Player p) {
        /*
        Cell[] listeningCells = cell.getNeighboring();
        for (Cell c : listeningCells) {
            notifier.listen(c, p);
        }
        p.setCell(cell);
         */
    }
}
