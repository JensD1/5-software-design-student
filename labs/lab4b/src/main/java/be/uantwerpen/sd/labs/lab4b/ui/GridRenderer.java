package be.uantwerpen.sd.labs.lab4b.ui;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.Level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.Cell;
import be.uantwerpen.sd.labs.lab4b.model.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class GridRenderer {
    private final AppConfig C = AppConfig.get();
    private final Palette P;

    public GridRenderer(Palette palette) {
        this.P = palette;
    }

    private static void fill(GraphicsContext g, Color c, int x, int y, int s) {
        g.setFill(c);
        g.fillRect(x, y, s, s);
    }

    private static void fillRound(GraphicsContext g, Color c, int x, int y, int s, double r) {
        g.setFill(c);
        g.fillRoundRect(x, y, s, s, r, r);
    }

    private static void fillInset(GraphicsContext g, Color c, int x, int y, int s, int pad) {
        g.setFill(c);
        g.fillRect(x + pad, y + pad, s - 2 * pad, s - 2 * pad);
    }

    public void draw(GraphicsContext g, World w) {
        final int TILE = C.tilePx, PAD = C.paddingPx;

        // 0) Background for the whole scene (fits both worlds)
        g.setFill(P.background);
        g.fillRect(0, 0, w.w * TILE + PAD * 2, w.h * TILE + C.legendHeightPx + PAD * 2);

        // 1) Clear the grid area overlay so tiles are crisp
        g.clearRect(PAD, PAD, w.w * TILE, w.h * TILE);

        for (int y = 0; y < w.h; y++)
            for (int x = 0; x < w.w; x++) {
                int sx = PAD + x * TILE, sy = PAD + y * TILE;
                Cell c = w.get(x, y);

                // ground
                switch (c.ground) {
                    case FLOOR -> fill(g, P.floor, sx, sy, TILE);
                    case WALL -> fill(g, P.wall, sx, sy, TILE);
                    case ICE -> fill(g, P.ice, sx, sy, TILE);
                    case TARGET -> {
                        fill(g, P.targetBase, sx, sy, TILE);      // use theme-chosen base
                        g.setFill(P.target);
                        g.fillOval(sx + TILE * 0.18, sy + TILE * 0.18, TILE * 0.64, TILE * 0.64);
                    }
                }

                // things (unchanged)
                switch (c.thing) {
                    case NONE -> {
                    }
                    case PLAYER -> fillRound(g, P.player, sx, sy, TILE, TILE * 0.22);
                    case BOX -> fillInset(g, P.box, sx, sy, TILE, 2);
                    case ICE_BOX -> fillInset(g, P.iceBox, sx, sy, TILE, 2);
                }
            }
    }
}
