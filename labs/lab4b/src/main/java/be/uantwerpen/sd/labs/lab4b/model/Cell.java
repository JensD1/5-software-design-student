package be.uantwerpen.sd.labs.lab4b.model;

public final class Cell {
    public Ground ground;
    public Thing thing;

    public Cell(Ground g, Thing t) {
        this.ground = g;
        this.thing = t;
    }

    public boolean isWalkable() {
        return ground != Ground.WALL;
    }

    public boolean isTarget() {
        return ground == Ground.TARGET;
    }

    public boolean isEmpty() {
        return thing == Thing.NONE;
    }
}
