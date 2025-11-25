package se.chalmers.tda367.team15.game.model.objects;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.tda367.team15.game.model.entities.Ant;

public class Colony extends WorldObject {
    private List<Ant> ants;

    public Colony(int x, int y) {
        super(x, y);
        this.ants = new ArrayList<>();
    }

    public void addAnt(Ant ant) {
        ants.add(ant);
    }

    public void removeAnt(Ant ant) {
        if (ants.remove(ant)) {
        }
    }

    public List<Ant> getAnts() {
        return new ArrayList<>(ants);
    }

}
