package se.chalmers.tda367.team15.game.model.entity.Termite;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import se.chalmers.tda367.team15.game.model.GameWorld;
import se.chalmers.tda367.team15.game.model.faction.Faction;
import se.chalmers.tda367.team15.game.model.structure.Structure;
import se.chalmers.tda367.team15.game.model.structure.Colony;
import se.chalmers.tda367.team15.game.model.entity.Entity;


import java.util.ArrayList;
import java.util.List;

public class Termite extends Entity {
    private final Faction faction = Faction.TERMITE_PROTECTORATE;
    private final float SPEED = 4.5f;
    private final int visionRadius = 4;
    TermiteBehaviour termiteBehaviour;
    //AttackComponent attackComponent;

    public Termite(Vector2 position,GameWorld gameWorld) {

        super(position, "Termite", gameWorld);
        this.termiteBehaviour = new TermiteBehaviour(this);
    }

    @Override
    public void update(float deltaTime){
        List<Entity> entities = getGameWorld().getEntities();
        termiteBehaviour.update(entities);
        super.update(deltaTime);
    }

    public float getSpeed() {
        return SPEED;
    }

    @Override
    public Faction getFaction(){
        return faction;
    }

}


