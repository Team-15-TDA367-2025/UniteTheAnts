package se.chalmers.tda367.team15.game.model.entity.ant.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.AttackCategory;
import se.chalmers.tda367.team15.game.model.entity.AttackComponent;
import se.chalmers.tda367.team15.game.model.entity.AttackTarget;
import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.interfaces.CanBeAttacked;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.managers.PheromoneManager;
import se.chalmers.tda367.team15.game.model.pheromones.PheromoneGridConverter;

public class AttackBehavior extends AntBehavior {
    private final HashMap<AttackCategory, Integer> targetPriority = new HashMap<>();
    private final Home home;
    private final AttackComponent attackComponent;
    private final PheromoneGridConverter converter;

    public AttackBehavior(Home home, Ant ant, Vector2 lastPosBeforeAttack, EntityQuery entityQuery,
            PheromoneGridConverter converter) {
        super(ant, entityQuery);
        targetPriority.put(AttackCategory.TERMITE, 1);
        this.home = home;
        this.attackComponent = new AttackComponent(5, 1000, 2, ant);
        this.converter = converter;
    }

    @Override
    public void update(PheromoneManager system) {
        AttackTarget target = findTarget();

        if (target == null) {
            ant.setBehavior(new FollowTrailBehavior(home, entityQuery, ant, converter));
        } else {
            Vector2 targetV = target.hasPosition.getPosition().sub(ant.getPosition());
            ant.setVelocity(targetV.nor().scl(ant.getSpeed()));
            attackComponent.attack(target);
        }

    }

    private AttackTarget findTarget() {
        List<CanBeAttacked> entities = entityQuery.getEntitiesOfType(CanBeAttacked.class);
        List<AttackTarget> potentialTargets = potentialTargets(entities);

        if (potentialTargets.isEmpty()) {
            return null;
        }

        AttackTarget target = potentialTargets.getFirst();
        for (AttackTarget t : potentialTargets) {
            int priorityT = targetPriority.get(t.canBeAttacked.getAttackCategory());
            int priorityCurrent = targetPriority.get(target.canBeAttacked.getAttackCategory());

            if (priorityT < priorityCurrent) {
                continue;
            }

            float distT = t.hasPosition.getPosition().dst(ant.getPosition());
            float distCurrent = target.hasPosition.getPosition().dst(ant.getPosition());

            if (distT >= distCurrent) {
                continue;
            }

            target = t;
        }

        return target;
    }

    private List<AttackTarget> potentialTargets(List<CanBeAttacked> entities) {
        List<AttackTarget> targets = new ArrayList<>();
        for (CanBeAttacked e : entities) {
            targets.add(new AttackTarget(e, e));
        }
        targets.removeIf(t -> t.canBeAttacked.getFaction() == ant.getFaction());
        targets.removeIf(t -> t.hasPosition.getPosition().dst(ant.getPosition()) > ant.getVisionRadius());

        return targets;
    }
}
