package be.uantwerpen.sd.labs.lab4b.logic;

import be.uantwerpen.sd.labs.lab4b.model.Cell;
import be.uantwerpen.sd.labs.lab4b.model.Ground;
import be.uantwerpen.sd.labs.lab4b.model.Thing;
import be.uantwerpen.sd.labs.lab4b.model.World;

public final class Movement {
    private Movement() {
    }

    public static boolean move(World w, int dx, int dy) {
        int px = w.playerX, py = w.playerY;
        int nx = px + dx, ny = py + dy;
        if (!w.inBounds(nx, ny)) return false;

        Cell dest = w.get(nx, ny);
        if (dest.ground == Ground.WALL) return false;

        if (dest.thing == Thing.NONE) {
            swapPlayer(w, px, py, nx, ny);
            return true;
        }

        // Pushable?
        if (dest.thing == Thing.BOX || dest.thing == Thing.ICE_BOX) {
            int bx = nx + dx, by = ny + dy;
            if (!w.inBounds(bx, by)) return false;
            Cell beyond = w.get(bx, by);
            if (beyond.ground == Ground.WALL || beyond.thing != Thing.NONE) return false;

            // Move box
            beyond.thing = dest.thing;
            dest.thing = Thing.NONE;

            if (beyond.thing == Thing.ICE_BOX && beyond.ground == Ground.ICE) {
                while (true) {
                    int sx = bx + dx, sy = by + dy;
                    if (!w.inBounds(sx, sy)) break;
                    Cell s = w.get(sx, sy);
                    if (s.ground != Ground.ICE || s.thing != Thing.NONE) break;
                    s.thing = beyond.thing;
                    beyond.thing = Thing.NONE;
                    bx = sx;
                    by = sy;
                    beyond = s;
                }
            }

            swapPlayer(w, px, py, nx, ny);
            return true;
        }

        return false;
    }

    private static void swapPlayer(World w, int px, int py, int nx, int ny) {
        Cell from = w.get(px, py);
        Cell to = w.get(nx, ny);
        to.thing = Thing.PLAYER;
        from.thing = Thing.NONE;
        w.playerX = nx;
        w.playerY = ny;
    }
}
