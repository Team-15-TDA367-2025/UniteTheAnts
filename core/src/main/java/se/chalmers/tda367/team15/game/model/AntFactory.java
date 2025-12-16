package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.AntType;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.managers.PheromoneManager;
import se.chalmers.tda367.team15.game.model.world.MapProvider;

public class AntFactory {
    private final PheromoneManager pheromoneSystem;
    private final MapProvider map;
    private final EntityQuery entityQuery;
    private final DestructionListener destructionListener;

    public AntFactory(PheromoneManager pheromoneSystem, MapProvider map, EntityQuery entityQuery, DestructionListener destructionListener) {
        this.pheromoneSystem = pheromoneSystem;
        this.map = map;
        this.entityQuery = entityQuery;
        this.destructionListener = destructionListener;
    }

    public Ant createAnt(Home home, AntType type) {
        Vector2 position = home.getPosition();
        return new Ant(position, pheromoneSystem, type, map, home, entityQuery, destructionListener);
    }
}
