package se.chalmers.tda367.team15.game.model.entity.ant.behavior.trail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.pheromones.Pheromone;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneGridConverter;

/**
 * Trail strategy for soldier ants following ATTACK pheromones.
 * Simple patrol behavior:
 * - Walk along trail in current direction
 * - Turn around when seeing another soldier (after random delay)
 * - Turn around at trail ends
 */
public class AttackTrailStrategy extends TrailStrategy {

    private static final float SPEED_MULTIPLIER = 1.0f;
    private static final int MIN_DELAY_TICKS = 2;
    private static final int MAX_DELAY_TICKS = 6;

    private final EntityQuery entityQuery;
    private final PheromoneGridConverter converter;
    private final Random random = new Random();

    private int turnAroundDelay = 0;
    private boolean sawSoldier = false;

    public AttackTrailStrategy(EntityQuery entityQuery, PheromoneGridConverter converter) {
        this.entityQuery = entityQuery;
        this.converter = converter;
    }

    /**
     * Force the turn-around to happen next tick (for testing).
     */
    void forceCheckNextTick() {
        turnAroundDelay = 0;
        sawSoldier = true;
    }

    @Override
    public Pheromone selectNextPheromone(Ant ant, List<Pheromone> neighbors, Pheromone current) {
        if (neighbors.isEmpty()) {
            return null;
        }

        int currentDistance = current != null ? current.getDistance() : 0;

        // Filter out current position to force movement
        List<Pheromone> movableNeighbors = neighbors.stream()
                .filter(p -> current == null || p.getDistance() != currentDistance)
                .toList();

        if (movableNeighbors.isEmpty()) {
            // Stuck at current position - try any neighbor
            return neighbors.stream()
                    .filter(p -> current == null || !p.getPosition().equals(current.getPosition()))
                    .findAny()
                    .orElse(null);
        }

        // Check for other soldiers in vision
        boolean canSeeOtherSoldier = entityQuery.getEntitiesOfType(Ant.class).stream()
                .filter(a -> a != ant)
                .filter(a -> a.getType().id().equals("soldier"))
                .anyMatch(a -> a.getPosition().dst(ant.getPosition()) <= ant.getVisionRadius());

        if (canSeeOtherSoldier && !sawSoldier) {
            // Just saw a soldier - start turn-around delay
            sawSoldier = true;
            turnAroundDelay = random.nextInt(MAX_DELAY_TICKS - MIN_DELAY_TICKS + 1) + MIN_DELAY_TICKS;
        }

        if (sawSoldier) {
            turnAroundDelay--;
            if (turnAroundDelay <= 0) {
                // Turn around
                outwards = !outwards;
                sawSoldier = false;
            }
        }

        if (!canSeeOtherSoldier) {
            sawSoldier = false;
        }

        // Move in current direction
        List<Pheromone> forward = filterByDistance(movableNeighbors, current, outwards);

        if (forward.isEmpty()) {
            // Trail end - turn around
            outwards = !outwards;
            forward = filterByDistance(movableNeighbors, current, outwards);
        }

        if (forward.isEmpty()) {
            // Fallback to any movable neighbor
            forward = movableNeighbors;
        }

        // Pick randomly from forward options (slight jitter)
        List<Pheromone> shuffled = new ArrayList<>(forward);
        Collections.shuffle(shuffled, random);
        return shuffled.get(0);
    }

    @Override
    public void onTrailEnd(Ant ant, Pheromone current) {
        // Turn around at trail end
        outwards = !outwards;
    }

    @Override
    public float getSpeedMultiplier() {
        return SPEED_MULTIPLIER;
    }
}
