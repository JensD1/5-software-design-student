package be.uantwerpen.sd.labs.lab4b.level.warehouse;

import be.uantwerpen.sd.labs.lab4b.gen.WarehouseWorldGenerator;
import be.uantwerpen.sd.labs.lab4b.gen.WorldGenerator;
import be.uantwerpen.sd.labs.lab4b.level.Level;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.logic.CoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.logic.warehouse.WarehouseCoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.warehouse.WarehouseMovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.domain.Box;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
import be.uantwerpen.sd.labs.lab4b.model.domain.warehouse.*;

public final class WarehouseKit extends LevelKit {

    private final MovementStrategy m = new WarehouseMovementStrategy();
    private final WorldGenerator g = new WarehouseWorldGenerator();
    private final RendererHints h = () -> false;
    private final Level level = new WarehouseLevel();

    private WarehouseKit() {
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
        return new WarehouseFloor();
    }

    @Override
    public GroundTile wall() {
        return new WarehouseWall();
    }

    @Override
    public GroundTile target() {
        return new WarehouseTarget();
    }

    @Override
    public Box box() {
        return new WarehouseBox();
    }

    @Override
    public Player player() {
        return new WarehousePlayer();
    }

    @Override
    public CoveragePolicy coverage() {
        return new WarehouseCoveragePolicy();
    }

    private static final class Holder {
        static final LevelKit INSTANCE = new WarehouseKit();
    }

}
