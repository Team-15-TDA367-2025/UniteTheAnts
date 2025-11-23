package se.chalmers.tda367.team15.game.model;

public class seekFoodPheromone extends Pheromone{

    @Override
    PheromoneType getPheromoneType() {
        return PheromoneType.seekFoodPheromone;
    }
}
