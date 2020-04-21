package fr.evywell.robgame.game.map;

public class Grid {

    private Cell[][] cells;
    public final int width, height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
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

}
