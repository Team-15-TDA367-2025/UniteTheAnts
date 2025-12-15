package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.termite.Termite;

public class EnemyFactory {
    private final GameWorld world;
    private final DestructionListener destructionListener;

    public EnemyFactory(GameWorld world, DestructionListener destructionListener) {
        this.world = world;
        this.destructionListener = destructionListener;
    }

    public Termite createTermite(Vector2 pos) {
        return new Termite(pos, world, destructionListener);
    }
}
