package se.chalmers.tda367.team15.game.model.entity.termite;

public class TermiteBehaviourManager {
    TermiteBehaviour attackBehaviour;
    TermiteBehaviourManager(TermiteBehaviour attackBehaviour) {
        this.attackBehaviour=attackBehaviour;
    }

    public void update() {
        attackBehaviour.update();
    }
}
