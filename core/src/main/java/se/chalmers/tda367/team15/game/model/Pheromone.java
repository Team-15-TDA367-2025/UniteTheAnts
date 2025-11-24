package se.chalmers.tda367.team15.game.model;

public class Pheromone {
    private final Vec2i position;
    private final PheromoneType type;
    private final int distance;

    public Pheromone(Vec2i position, PheromoneType type, int distance) {
        this.position = position;
        this.type = type;
        this.distance = distance;
    }

    public Vec2i getPosition() {
        return position;
    }

    public PheromoneType getType() {
        return type;
    }

    public int getDistance() {
        return distance;
    }
}

