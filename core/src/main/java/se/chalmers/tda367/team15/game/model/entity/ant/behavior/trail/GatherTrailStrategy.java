package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.Comparator;
import java.util.List;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.behavior.WanderBehavior;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.managers.PheromoneManager;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneGridConverter;

/**
 * Trail strategy for worker ants following GATHER pheromones.
 * - When inventory is empty: prefer higher distance (outward to find resources)
 * - When inventory is full: prefer lower distance (return to colony)
 */
public class GatherTrailStrategy implements TrailStrategy {

    private static final float SPEED_MULTIPLIER = 1.2f;

    @Override
    public Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current) {
        if (neighbors.isEmpty()) {
            return null;
        }

        boolean returningHome = ant.getInventory().isFull();

        // Sort by distance - ascending for return, descending for outward
        Comparator<Pheromone> comparator = Comparator.comparingInt(Pheromone::getDistance);
        if (!returningHome) {
            comparator = comparator.reversed();
        }

        return neighbors.stream()
                .filter(p -> current == null || p.getDistance() != current.getDistance())
                .sorted(comparator)
                .findFirst()
                .orElse(neighbors.get(0)); // Fallback to any neighbor if stuck
    }

    @Override
    public void onTrailEnd(Ant ant, Pheromone current, PheromoneManager pheromoneManager,
            Home home, EntityQuery entityQuery, PheromoneGridConverter converter, TrailStrategy strategy) {
        // At trail end, worker should switch to wander behavior
        ant.setBehavior(new WanderBehavior(ant, home, entityQuery, converter, strategy, pheromoneManager));
    }

    @Override
    public float getSpeedMultiplier() {
        return SPEED_MULTIPLIER;
    }
}
