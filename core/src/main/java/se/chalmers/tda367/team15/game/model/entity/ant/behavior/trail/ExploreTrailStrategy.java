package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.Collections;
import java.util.List;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.behavior.WanderBehavior;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.managers.PheromoneManager;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneGridConverter;

/**
 * Trail strategy for scout ants following EXPLORE pheromones.
 * - Always prefer higher distance (outward exploration)
 * - Random selection when tied
 * - Destroy pheromone when reaching trail end, then enter wander mode
 */
public class ExploreTrailStrategy implements TrailStrategy {

    private static final float SPEED_MULTIPLIER = 1.3f;

    private final PheromoneManager pheromoneManager;
    private final Home home;
    private final EntityQuery entityQuery;
    private final PheromoneGridConverter converter;

    public ExploreTrailStrategy(PheromoneManager pheromoneManager, Home home,
            EntityQuery entityQuery, PheromoneGridConverter converter) {
        this.pheromoneManager = pheromoneManager;
        this.home = home;
        this.entityQuery = entityQuery;
        this.converter = converter;
    }

    @Override
    public Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current) {
        if (neighbors.isEmpty()) {
            return null;
        }

        int currentDistance = current != null ? current.getDistance() : 0;

        // Filter to only higher distance pheromones
        List<Pheromone> higherDistance = neighbors.stream()
                .filter(p -> p.getDistance() > currentDistance)
                .toList();

        if (higherDistance.isEmpty()) {
            // At trail end - will be handled by onTrailEnd
            return null;
        }

        // Random selection among highest distance pheromones
        int maxDistance = higherDistance.stream()
                .mapToInt(Pheromone::getDistance)
                .max()
                .orElse(0);

        List<Pheromone> candidates = higherDistance.stream()
                .filter(p -> p.getDistance() == maxDistance)
                .toList();

        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    @Override
    public void onTrailEnd(Ant ant, Pheromone current) {
        // Destroy the pheromone at trail end
        if (current != null) {
            pheromoneManager.removePheromone(current.getPosition());
        }

        // Switch to wander behavior with low home bias
        ant.setBehavior(new WanderBehavior(ant, home, entityQuery, converter));
    }

    @Override
    public float getSpeedMultiplier() {
        return SPEED_MULTIPLIER;
    }
}
