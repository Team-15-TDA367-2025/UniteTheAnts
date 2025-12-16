package se.chalmers.tda367.team15.game.model.entity.termite;

import com.badlogic.gdx.math.Vector2;
import se.chalmers.tda367.team15.game.model.entity.AttackComponent;
import se.chalmers.tda367.team15.game.model.entity.Entity;
import se.chalmers.tda367.team15.game.model.interfaces.CanAttack;
import se.chalmers.tda367.team15.game.model.interfaces.CanBeAttacked;

public class AttackBehaviour extends TermiteBehaviour {
    private final AttackTargetingComponent attackTargetingComponent;
    private final AttackComponent attackComponent;
    private CanAttack canAttack;

    AttackBehaviour(CanAttack canAttack,AttackTargetingComponent attackTargetingComponent, AttackComponent attackComponent) {
        this.attackTargetingComponent = attackTargetingComponent;
        this.attackComponent= attackComponent;
        this.canAttack=canAttack;
    }
    @Override
    public void update() {
        CanBeAttacked target = attackTargetingComponent.findTarget();
        if(target==null) {
            canAttack.noTarget();
        }
        else{
            Entity host = canAttack.getEntity();
            Vector2 targetV = target.getPosition().sub(canAttack.getPosition());
            host.setVelocity(targetV.nor().scl(canAttack.getEntity().getSpeed()));
            attackComponent.attack(target);
        }
    }



}
