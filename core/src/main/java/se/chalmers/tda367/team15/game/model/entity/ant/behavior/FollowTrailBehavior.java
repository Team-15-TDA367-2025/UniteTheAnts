package se.chalmers.tda367.team15.game.model.entity.ant.behavior;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.Pheromone;
import se.chalmers.tda367.team15.game.model.PheromoneSystem;
import se.chalmers.tda367.team15.game.model.entity.ant.Ant;

public class FollowTrailBehavior implements AntBehavior {
    private boolean returningToColony = false;
    private Pheromone lastPheromone = null;
    private Pheromone currentTarget = null;
    private static final float SPEED_BOOST_ON_TRAIL = 2f;
    private static final float REACHED_THRESHOLD = 0.3f;

    @Override
    public void update(Ant ant, PheromoneSystem system, float deltaTime) {
        GridPoint2 gridPos = ant.getGridPosition();
        List<Pheromone> neighbors = system.getPheromonesIn3x3(gridPos);
        if (lastPheromone == null) {
            lastPheromone = neighbors.stream().min(Comparator.comparingInt(Pheromone::getDistance)).orElse(null);
        }

        if (lastPheromone == null || neighbors.isEmpty()) {
            ant.setBehavior(new WanderBehavior());
            return;
        }

        // Check if we've reached our current target
        if (currentTarget != null) {
            Vector2 currentPos = ant.getPosition();
            Vector2 targetPos = new Vector2(currentTarget.getPosition().x + 0.5f, currentTarget.getPosition().y + 0.5f);
            float distSq = currentPos.dst2(targetPos);

            if (distSq < REACHED_THRESHOLD * REACHED_THRESHOLD) {
                // We've reached the target, update lastPheromone and clear current target
                lastPheromone = currentTarget;
                currentTarget = null;
            }
        }

        // If we don't have a current target, find a new one
        if (currentTarget == null) {
            // Try to find next pheromone in current direction
            Pheromone next = findNextPheromone(neighbors, returningToColony);
            
            // If blocked/end of trail, try flipping direction
            if (next == null) {
                boolean flippedState = !returningToColony;
                next = findNextPheromone(neighbors, flippedState);
                if (next != null) {
                    returningToColony = flippedState;
                }
            }
            
            if (next == null) {
                ant.setBehavior(new WanderBehavior());
                return;
            }
            currentTarget = next;
        }

        // Move towards the current target
        Vector2 targetPos = new Vector2(currentTarget.getPosition().x + 0.5f, currentTarget.getPosition().y + 0.5f);
        Vector2 currentPos = ant.getPosition();
        Vector2 direction = targetPos.cpy().sub(currentPos);

        // Set velocity towards target
        if (direction.len2() > 0.01f) {
            direction.nor();
            float speed = ant.getSpeed() * SPEED_BOOST_ON_TRAIL;
            ant.setVelocity(direction.scl(speed));
        } else {
            // Very close, just update and find next
            lastPheromone = currentTarget;
            currentTarget = null;
        }
    }

    private Pheromone findNextPheromone(List<Pheromone> neighbors, boolean returning) {
        int currentDist = lastPheromone.getDistance();

        // Filter candidates:
        // 1. Not the node we are currently at (lastPheromone)
        // 2. Strictly in the correct direction (increasing if leaving, decreasing if returning)
        List<Pheromone> candidates = neighbors.stream()
                .filter(p -> !p.getPosition().equals(lastPheromone.getPosition()))
                .filter(p -> returning ? p.getDistance() < currentDist : p.getDistance() > currentDist)
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            return null;
        }

        // Shuffle first to handle ties randomly
        Collections.shuffle(candidates);

        // Sort to find the best step
        // If returning: prefer highest distance < current (closest step down)
        // If leaving: prefer lowest distance > current (closest step up)
        candidates.sort((p1, p2) -> {
            int d1 = p1.getDistance();
            int d2 = p2.getDistance();
            return returning ? Integer.compare(d2, d1) : Integer.compare(d1, d2);
        });

        return candidates.get(0);
    }
}
