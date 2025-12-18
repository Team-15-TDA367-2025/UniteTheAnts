package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.List;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;

/**
 * Strategy interface for how ants follow pheromone trails.
 * Different ant types use different strategies based on their role.
 */
public interface TrailStrategy {

    /**
     * Selects the next pheromone to move toward from available neighbors.
     *
     * @param ant       The ant making the decision
     * @param neighbors Available pheromones in adjacent cells
     * @param current   The current pheromone the ant is on (may be null)
     * @return The selected pheromone to move toward, or null to leave the trail
     */
    Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current);

    /**
     * Called when the ant reaches a trail end (no valid next pheromone).
     *
     * @param ant              The ant at the trail end
     * @param current          The current pheromone
     * @param pheromoneManager For destroying pheromones if needed
     * @param home             For switching to wander behavior
     * @param entityQuery      For behavior switching
     * @param converter        For coordinate conversion
     * @param trailStrategy    For creating new behaviors with the same strategy
     */
    void onTrailEnd(Ant ant, Pheromone current);

    /**
     * @return Speed multiplier when following this trail type
     */
    float getSpeedMultiplier();
}
