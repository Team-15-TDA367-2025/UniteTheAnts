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

public class FollowTrailBehavior extends AntBehavior {
    private static final float SPEED_BOOST_ON_TRAIL = 2f;
    private static final float REACHED_THRESHOLD_SQ = 0.3f * 0.3f;

    private boolean returningToColony = false;
    private Pheromone lastPheromone = null;
    private Pheromone currentTarget = null;

    public FollowTrailBehavior(Ant ant) {
        super(ant);
    }

    @Override
    public void update(PheromoneSystem system, float deltaTime) {
        GridPoint2 gridPos = ant.getGridPosition();
        List<Pheromone> neighbors = system.getPheromonesIn3x3(gridPos);

        // Initialize if needed
        if (lastPheromone == null) {
            lastPheromone = neighbors.stream()
                    .min(Comparator.comparingInt(Pheromone::getDistance))
                    .orElse(null);
        }

        if (lastPheromone == null) {
            ant.setBehavior(new WanderBehavior(ant));
            return;
        }

        if (hasReachedTarget()) {
            lastPheromone = currentTarget;
            currentTarget = null;
        }

        if (currentTarget == null && !selectNextTarget(neighbors)) {
            return;
        }

        moveTowardsTarget();
    }

    private boolean hasReachedTarget() {
        if (currentTarget == null)
            return false;

        Vector2 targetPos = getCenterPos(currentTarget);
        return ant.getPosition().dst2(targetPos) < REACHED_THRESHOLD_SQ;
    }

    private boolean selectNextTarget(List<Pheromone> neighbors) {
        // Try current direction
        Pheromone next = findNextPheromone(neighbors, returningToColony);

        // If blocked, try flipping
        if (next == null) {
            boolean flipped = !returningToColony;
            next = findNextPheromone(neighbors, flipped);
            if (next != null) {
                returningToColony = flipped;
            }
        }

        if (next == null) {
            ant.setBehavior(new WanderBehavior(ant));
            return false;
        }

        currentTarget = next;
        return true;
    }

    private void moveTowardsTarget() {
        Vector2 targetPos = getCenterPos(currentTarget);
        Vector2 direction = targetPos.cpy().sub(ant.getPosition());

        if (direction.len2() > 0.01f) {
            direction.nor();
            float speed = ant.getSpeed() * SPEED_BOOST_ON_TRAIL;
            ant.setVelocity(direction.scl(speed));
        }
    }

    private Pheromone findNextPheromone(List<Pheromone> neighbors, boolean returning) {
        int currentDist = lastPheromone.getDistance();

        // Filter for valid next steps in the given direction
        List<Pheromone> candidates = neighbors.stream()
                .filter(p -> !p.getPosition().equals(lastPheromone.getPosition()))
                .filter(p -> returning ? p.getDistance() < currentDist : p.getDistance() > currentDist)
                .collect(Collectors.toList());

        if (candidates.isEmpty())
            return null;

        Collections.shuffle(candidates);

        // Sort to find best step (closest step in desired direction)
        candidates.sort((p1, p2) -> returning
                ? Integer.compare(p2.getDistance(), p1.getDistance()) // Returning: Descending (highest < current)
                : Integer.compare(p1.getDistance(), p2.getDistance())); // Leaving: Ascending (lowest > current)

        return candidates.get(0);
    }

    private Vector2 getCenterPos(Pheromone p) {
        return new Vector2(p.getPosition().x + 0.5f, p.getPosition().y + 0.5f);
    }
}
