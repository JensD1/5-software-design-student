package be.uantwerpen.sd.labs.lab4b.level;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.model.Ground;
import be.uantwerpen.sd.labs.lab4b.model.Thing;

public final class GlacierLevel extends Level {
    private static javafx.scene.paint.Color color(String hex) {
        return javafx.scene.paint.Color.web(hex);
    }

    @Override
    public Theme theme() {
        return Theme.GLACIER;
    }

    @Override
    public Palette palette() {
        var C = AppConfig.get();
        var t = C.themeGlacier;
        return new Palette(
                color(t.get("background")),
                color(t.get("floor")),
                color(t.get("wall")),
                color(t.get("ice")),
                color(t.get("target")),
                color(t.get("player")),
                color(t.get("box")),
                color(t.get("ice_box")),
                color(t.get("ice"))
        );
    }

    @Override
    public Ground defaultFloor() {
        return Ground.ICE;
    }

    @Override
    public Thing boxProduct() {
        return Thing.ICE_BOX;
    }

    @Override
    public boolean requireBackstops() {
        return true;
    }

    @Override
    public int extraPullBias() {
        return AppConfig.get().glacierPullBonus;
    }
}