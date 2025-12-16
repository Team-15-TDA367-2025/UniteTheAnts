package se.chalmers.tda367.team15.game.model.entity.termite;

import java.util.HashMap;
import java.util.List;

import se.chalmers.tda367.team15.game.model.AttackCategory;
import se.chalmers.tda367.team15.game.model.interfaces.CanAttack;
import se.chalmers.tda367.team15.game.model.interfaces.CanBeAttacked;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.managers.StructureManager;

/**
 * Component that handles the behaviour of {@link Termite}
 */
public class AttackTargetingComponent {
    CanAttack canAttack;
    EntityQuery entityQuery;
    StructureManager structureManager;
    HashMap<AttackCategory, Integer> targetPriority;
    AttackTargetingComponent(CanAttack canAttack, EntityQuery entityQuery, StructureManager structureManager, HashMap<AttackCategory, Integer> targetPriority) {
        this.canAttack = canAttack;
        this.entityQuery = entityQuery;
        this.structureManager=structureManager;
        this.targetPriority = targetPriority;
    }

    /**
     * Updates the behaviour of the Termite.
     *
     * @return the target {@link CanBeAttacked} or {@code null} if there are no
     *         attack targets.
     */

    public CanBeAttacked findTarget() {

        CanBeAttacked target = null;

        List<CanBeAttacked> potentialTargets = potentialTargets();

        // determine target, entities first, then structures, then stand still.
        if (!potentialTargets.isEmpty()) {
            target = potentialTargets.getFirst();
            for (CanBeAttacked t : potentialTargets) {
                // Greater or equal target priority?
                if (targetPriority.get(t.getAttackCategory()) >= targetPriority
                        .get(target.getAttackCategory())) {
                    // closest distance?
                    if (t.getPosition().dst(canAttack.getPosition()) < target.getPosition()
                            .dst(canAttack.getPosition())) {
                        target = t;
                    }
                }
            }

        }
        return target;
    }

   private List<CanBeAttacked> potentialTargets() {
        List<CanBeAttacked> targets = entityQuery.getEntitiesOfType(CanBeAttacked.class);
        targets.removeIf(t -> t.getFaction() == canAttack.getFaction());
        targets.removeIf(t -> t.getPosition().dst(canAttack.getPosition()) > canAttack.getVisionRadius());
        return targets;
    }
}
