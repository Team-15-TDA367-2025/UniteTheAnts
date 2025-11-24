package se.chalmers.tda367.team15.game.model;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.tda367.team15.game.model.entity.Entity;
import se.chalmers.tda367.team15.game.model.structure.Structure;

public class GameWorld {
    private List<Entity> entities;
    private List<Structure> structures;

    public GameWorld() {
        this.entities = new ArrayList<>();
        this.structures = new ArrayList<>();
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public List<Drawable> getDrawables() {
        // We just have entities right now, we might need to change this in the future.
        List<Drawable> drawables = new ArrayList<>(entities);
        drawables.addAll(structures);
        return drawables;
    }

    public void update(float deltaTime, PheromoneSystem pheromoneSystem) {
        for (Entity entity : entities) {
            if (entity instanceof se.chalmers.tda367.team15.game.model.entity.ant.Ant) {
                ((se.chalmers.tda367.team15.game.model.entity.ant.Ant) entity).update(deltaTime, pheromoneSystem);
            } else {
                entity.update(deltaTime);
            }
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addStructure(Structure structure) {
        structures.add(structure);
    }
}
