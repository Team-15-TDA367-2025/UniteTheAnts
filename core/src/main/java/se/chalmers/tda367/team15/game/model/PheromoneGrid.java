package se.chalmers.tda367.team15.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PheromoneGrid {
    private final Map<Vec2i, Pheromone> pheromones;

    public PheromoneGrid() {
        this.pheromones = new HashMap<>();
    }

    public boolean hasPheromoneAt(Vec2i pos) {
        return pheromones.containsKey(pos);
    }

    public Pheromone getPheromoneAt(Vec2i pos) {
        return pheromones.get(pos);
    }

    public void addPheromone(Pheromone pheromone) {
        pheromones.put(pheromone.getPosition(), pheromone);
    }

    public void removePheromone(Vec2i pos) {
        pheromones.remove(pos);
    }

    public Collection<Pheromone> getAllPheromones() {
        return new ArrayList<>(pheromones.values());
    }

    /**
     * Gets all pheromones in the 3x3 area around the center (including diagonals and center).
     */
    public List<Pheromone> getPheromonesIn3x3(Vec2i centerGridPos) {
        List<Pheromone> pheromonesInArea = new ArrayList<>();
        
        // Check all 9 cells in 3x3 grid (including center and all 8 neighbors)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Vec2i pos = new Vec2i(centerGridPos.x + dx, centerGridPos.y + dy);
                Pheromone pheromone = pheromones.get(pos);
                if (pheromone != null) {
                    pheromonesInArea.add(pheromone);
                }
            }
        }
        return pheromonesInArea;
    }
}

