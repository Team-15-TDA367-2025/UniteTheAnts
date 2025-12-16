package se.chalmers.tda367.team15.game.model.entity.ant;

import java.util.Set;

import se.chalmers.tda367.team15.game.model.pheromones.PheromoneType;

/**
 * Configuration record for ant types.
 * Defines both the egg properties (cost, time) and the ant's stats.
 * 
 * @param id                unique identifier (e.g., "worker")
 * @param displayName       human-readable name for UI
 * @param foodCost          amount of food required to create
 * @param developmentTicks  number of ticks until hatching
 * @param maxHealth         starting health of the ant
 * @param moveSpeed         movement speed of the ant
 * @param carryCapacity     resource carrying capacity
 * @param textureName       base name of the texture to use
 * @param allowedPheromones which pheromone types this ant can follow
 * @param homeBias          tendency to wander toward home (0.0 = none, 1.0 =
 *                          strong)
 */
public record AntType(
        String id,
        String displayName,
        int foodCost,
        int developmentTicks,
        float maxHealth,
        float moveSpeed,
        int carryCapacity,
        String textureName,
        Set<PheromoneType> allowedPheromones,
        float homeBias) {
}
