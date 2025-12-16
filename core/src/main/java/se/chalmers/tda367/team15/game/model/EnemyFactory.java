package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.entity.AttackComponent;
import se.chalmers.tda367.team15.game.model.entity.termite.AttackBehaviour;
import se.chalmers.tda367.team15.game.model.entity.termite.AttackTargetingComponent;
import se.chalmers.tda367.team15.game.model.entity.termite.Termite;
import se.chalmers.tda367.team15.game.model.entity.termite.TermiteBehaviourManager;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.managers.StructureManager;

import java.util.HashMap;

public class EnemyFactory {
    private final DestructionListener destructionListener;
    private final EntityQuery entityQuery;
    private final StructureManager structureManager;
    private final HashMap<AttackCategory, Integer> targetPriority;
    public EnemyFactory(EntityQuery entityQuery, StructureManager structureManager, DestructionListener destructionListener, HashMap<AttackCategory, Integer> targetPriority) {
        this.entityQuery = entityQuery;
        this.structureManager = structureManager;
        this.destructionListener = destructionListener;
        this.targetPriority = targetPriority;
    }

    public Termite createTermite(Vector2 pos) {
        Termite termite =  new Termite(pos, entityQuery, structureManager, destructionListener, targetPriority);
        AttackTargetingComponent attackTargetingComponent = new AttackComponent attackComponent    =
        AttackBehaviour attackBehaviour = new AttackBehaviour(termite, );
        TermiteBehaviourManager termiteBehaviourManager = new TermiteBehaviourManager();
        termite.setManager(termiteBehaviourManager);

        return termite;
    }
}
