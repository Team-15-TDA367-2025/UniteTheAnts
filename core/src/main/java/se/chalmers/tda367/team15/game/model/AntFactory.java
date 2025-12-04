package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneSystem;
import se.chalmers.tda367.team15.game.model.structure.Colony;

public class AntFactory {
    private static final int DEFAULT_CAPACITY = 5;
    private final PheromoneSystem pheromoneSystem;
    private final Colony colony;
    
    public AntFactory(PheromoneSystem pheromoneSystem, Colony colony) {
        this.pheromoneSystem = pheromoneSystem;
        this.colony = colony;
    }

    public Ant createAnt() {
        Vector2 position = colony.getPosition();
        return new Ant(position, pheromoneSystem, DEFAULT_CAPACITY);
    }
}
