package be.uantwerpen.sd.labs.lab4b.level;

import be.uantwerpen.sd.labs.lab4b.model.Ground;
import be.uantwerpen.sd.labs.lab4b.model.Thing;
import javafx.scene.paint.Color;

public abstract class Level {

    /**
     * Factory Method: subclass chooses the theme (affects generator + renderer).
     */
    public abstract Theme theme();

    /**
     * Give the UI a palette for this theme.
     */
    public abstract Palette palette();

    public abstract Ground defaultFloor();

    public abstract Thing boxProduct();

    public abstract boolean requireBackstops();

    public int extraPullBias() {
        return 0;
    }

    public enum Theme {WAREHOUSE, GLACIER}

    public static final class Palette {
        public final Color background, floor, wall, ice, target, player, box, iceBox;
        public final Color targetBase;

        public Palette(Color background, Color floor, Color wall, Color ice,
                       Color target, Color player, Color box, Color iceBox,
                       Color targetBase) {
            this.background = background;
            this.floor = floor;
            this.wall = wall;
            this.ice = ice;
            this.target = target;
            this.player = player;
            this.box = box;
            this.iceBox = iceBox;
            this.targetBase = targetBase;
        }
    }
}
