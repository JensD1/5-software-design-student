package be.uantwerpen.sd.labs.lab4b.level.glacier;

import be.uantwerpen.sd.labs.lab4b.gen.GlacierWorldGenerator;
import be.uantwerpen.sd.labs.lab4b.gen.WorldGenerator;
import be.uantwerpen.sd.labs.lab4b.level.Level;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.logic.CoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.logic.glacier.GlacierCoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.glacier.GlacierMovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.domain.Box;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
import be.uantwerpen.sd.labs.lab4b.model.domain.glacier.*;

public final class GlacierKit extends LevelKit {

    private final MovementStrategy m = new GlacierMovementStrategy(this);
    private final WorldGenerator g = new GlacierWorldGenerator();
    private final RendererHints h = () -> true;
    private final Level level = new GlacierLevel();

    private GlacierKit() {
    }

    public static LevelKit createLevelKit() {
        return Holder.INSTANCE;
    }

    @Override
    public MovementStrategy movement() {
        return m;
    }

    @Override
    public WorldGenerator generator() {
        return g;
    }

    @Override
    public RendererHints hints() {
        return h;
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    public GroundTile floor() {
        return new GlacierFloor();
    }

    @Override
    public GroundTile wall() {
        return new GlacierWall();
    }

    @Override
    public GroundTile target() {
        return new GlacierTarget();
    }

    @Override
    public Box box() {
        return new GlacierBox();
    }

    @Override
    public Player player() {
        return new GlacierPlayer();
    }

    @Override
    public CoveragePolicy coverage() {
        return new GlacierCoveragePolicy();
    }

    private static final class Holder {
        static final LevelKit INSTANCE = new GlacierKit();
    }

}
