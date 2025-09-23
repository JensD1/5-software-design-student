package be.uantwerpen.sd.labs.lab4b.model;

public final class World {
    public final int w, h;
    private final Cell[][] grid; // [y][x]

    public int playerX = -1, playerY = -1;

    public World(int w, int h) {
        this.w = w;
        this.h = h;
        this.grid = new Cell[h][w];
    }

    public void set(int x, int y, Cell c) {
        grid[y][x] = c;
        if (c.thing == Thing.PLAYER) {
            playerX = x;
            playerY = y;
        }
    }

    public Cell get(int x, int y) {
        return grid[y][x];
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < w && y < h;
    }

    public int coveredTargets() {
        int count = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Cell c = grid[y][x];
                if (c.ground == Ground.TARGET && (c.thing == Thing.BOX || c.thing == Thing.ICE_BOX)) count++;
            }
        return count;
    }

    public int totalTargets() {
        int t = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                if (grid[y][x].ground == Ground.TARGET) t++;
            }
        return t;
    }

    public World copy() {
        World w2 = new World(w, h);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                Cell c = grid[y][x];
                w2.set(x, y, new Cell(c.ground, c.thing)); // set() records playerX/Y
            }
        return w2;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
