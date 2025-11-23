package se.chalmers.tda367.team15.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Has the responsibility of keeping track of what pheromones exist in the game world
 * and removing them when they exist past their lifetime.
 */
public class PheromoneHandler {

    private final ArrayList<Pheromone> pheromoneList = new ArrayList<>();

    ArrayList<Pheromone> getPheromoneList() {
        return new ArrayList<>(pheromoneList);
    }

    void addPheromone(Pheromone pheromone) {

        pheromoneList.add(pheromone);
    }

    public void update() {
        for (Pheromone pheromone : pheromoneList) {
            if((System.currentTimeMillis() - pheromone.getCreationDate()) > pheromone.getMaxLifeTimeMs() ) {
                pheromoneList.remove(pheromone);
            }
        }
    }
}
