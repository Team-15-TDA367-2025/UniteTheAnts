package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.termite.Termite;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.managers.StructureManager;

public class EnemyFactory {
    private final DestructionListener destructionListener;
    private final EntityQuery entityQuery;
    private final StructureManager structureManager;

    public EnemyFactory(EntityQuery entityQuery, StructureManager structureManager, DestructionListener destructionListener) {
        this.entityQuery = entityQuery;
        this.structureManager = structureManager;
        this.destructionListener = destructionListener;
    }

    public Termite createTermite(Vector2 pos) {
        return new Termite(pos, entityQuery, structureManager, destructionListener);
    }
}
