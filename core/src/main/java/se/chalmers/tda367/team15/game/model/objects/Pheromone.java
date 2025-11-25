package se.chalmers.tda367.team15.game.model.objects;

public class Pheromone extends WorldObject {
    private PheromoneType type;

    public Pheromone(int x, int y, PheromoneType type) {
        super(x, y);
        this.type = type;
    }

    public PheromoneType getType() {
        return type;
    }

    public void setType(PheromoneType type) {
        this.type = type;
    }

}
