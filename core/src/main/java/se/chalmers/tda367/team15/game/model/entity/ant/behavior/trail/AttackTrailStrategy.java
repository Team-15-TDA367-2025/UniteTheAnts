package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.behavior.WanderBehavior;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.managers.PheromoneManager;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneGridConverter;

/**
 * Trail strategy for soldier ants following ATTACK pheromones.
 * - Stay on the trail but maximize distance from other soldiers
 * - Prefer higher distance pheromones (outward) as tiebreaker
 * - At trail end, patrol back and forth
 */
public class AttackTrailStrategy implements TrailStrategy {

    private static final float SPEED_MULTIPLIER = 1.0f;

    private final EntityQuery entityQuery;
    private final PheromoneGridConverter converter;

    public AttackTrailStrategy(EntityQuery entityQuery, PheromoneGridConverter converter) {
        this.entityQuery = entityQuery;
        this.converter = converter;
    }

    @Override
    public Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current) {
        if (neighbors.isEmpty()) {
            return null;
        }

        // Get all soldiers on the same trail
        List<Ant> otherSoldiers = entityQuery.getEntitiesOfType(Ant.class).stream()
                .filter(a -> a != ant)
                .filter(a -> a.getType().id().equals("soldier"))
                .filter(a -> a.getPosition().dst(ant.getPosition()) <= ant.getVisionRadius())
                .toList();

        if (otherSoldiers.isEmpty()) {
            // No soldiers in sight - prefer higher distance (outward)
            return neighbors.stream()
                    .max(Comparator.comparingInt(Pheromone::getDistance))
                    .orElse(neighbors.get(0));
        }

        // Pick pheromone that maximizes minimum distance to any soldier
        return neighbors.stream()
                .max(Comparator.comparingDouble(p -> minDistanceToSoldiers(p, otherSoldiers)))
                .orElse(neighbors.get(0));
    }

    private double minDistanceToSoldiers(Pheromone pheromone, List<Ant> soldiers) {
        Vector2 pheromoneWorldPos = converter.pheromoneGridToWorld(pheromone.getPosition());

        return soldiers.stream()
                .mapToDouble(soldier -> soldier.getPosition().dst(pheromoneWorldPos))
                .min()
                .orElse(Double.MAX_VALUE);
    }

    @Override
    public void onTrailEnd(Ant ant, Pheromone current, PheromoneManager pheromoneManager,
            Home home, EntityQuery entityQueryParam, PheromoneGridConverter converterParam, TrailStrategy strategy) {
        // At trail end, soldier patrols back and forth (switch to wander)
        ant.setBehavior(new WanderBehavior(ant, home, entityQueryParam, converterParam, strategy, pheromoneManager));
    }

    @Override
    public float getSpeedMultiplier() {
        return SPEED_MULTIPLIER;
    }
}
