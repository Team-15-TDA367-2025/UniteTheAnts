package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;

/**
 * Trail strategy for worker ants following GATHER pheromones.
 * - Walk forward (pick randomly from all higher-distance options)
 * - Turn around at dead ends (don't leave trail)
 * - Only return home when inventory is full
 */
public class GatherTrailStrategy extends TrailStrategy {

    private static final float SPEED_MULTIPLIER = 1.2f;
    private final Random random = new Random();

    @Override
    public Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current) {
        if (neighbors.isEmpty()) {
            return null;
        }

        boolean isFull = ant.getInventory().isFull();

        // If inventory is full, always go home (lower distance)
        if (isFull) {
            outwards = false;
            List<Pheromone> homeward = filterByDistance(neighbors, current, false);
            if (homeward.isEmpty()) {
                // At colony entrance - leave trail
                return null;
            }
            return getBestByDistance(homeward, false);
        }

        // Not full: wander on trail
        // Get ALL forward options (strictly higher distance when outwards)
        List<Pheromone> forward = filterByDistance(neighbors, current, outwards);

        if (forward.isEmpty()) {
            // Dead end - turn around
            outwards = !outwards;
            forward = filterByDistance(neighbors, current, outwards);

            if (forward.isEmpty()) {
                // Still nothing - pick any neighbor that isn't current
                return neighbors.stream()
                        .filter(p -> current == null || !p.getPosition().equals(current.getPosition()))
                        .findAny()
                        .orElse(neighbors.get(0));
            }
        }

        // Pick randomly from ALL forward options (not just best distance)
        List<Pheromone> shuffled = new ArrayList<>(forward);
        Collections.shuffle(shuffled, random);
        return shuffled.get(0);
    }

    @Override
    public void onTrailEnd(Ant ant, Pheromone current) {
        // Worker ants should stay on trail, but if somehow off trail, wander
        ant.setWanderBehaviour();
    }

    @Override
    public float getSpeedMultiplier() {
        return SPEED_MULTIPLIER;
    }
}
