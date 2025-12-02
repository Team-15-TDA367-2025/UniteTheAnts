package se.chalmers.tda367.team15.game.model.entity;

import se.chalmers.tda367.team15.game.model.CanBeAttacked;
import se.chalmers.tda367.team15.game.model.HasPosition;

public class AttackTarget {
    public CanBeAttacked canBeAttacked;
    public HasPosition hasPosition;
    public AttackTarget(CanBeAttacked h, HasPosition p) {
        this.canBeAttacked=h;
        this.hasPosition=p;
    }

}
