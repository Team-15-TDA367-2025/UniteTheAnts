package se.chalmers.tda367.team15.game.model;

import se.chalmers.tda367.team15.game.model.entity.Entity;

public interface EntityDeathObserver {
    void onEntityDeath(Entity e);
}
