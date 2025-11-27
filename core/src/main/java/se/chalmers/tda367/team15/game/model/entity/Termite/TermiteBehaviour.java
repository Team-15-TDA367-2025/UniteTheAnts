package se.chalmers.tda367.team15.game.model.entity.Termite;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Vector2;
import se.chalmers.tda367.team15.game.model.entity.Entity;
import se.chalmers.tda367.team15.game.model.structure.Colony;
import se.chalmers.tda367.team15.game.model.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TermiteBehaviour {
    Termite termite;
    TermiteBehaviour(Termite termite){
        this.termite = termite;
    }

    public void update(List<Entity> entities) {
        // check for hostile entities.there might be ants we can eat! *licks lips with devious smile* >:)
        List<Entity> targetEntities = hostileEntities(entities);
        Vector2 targetV = termite.getPosition().cpy();

        if (!targetEntities.isEmpty()) {
            Entity closestEntity = targetEntities.getFirst();

            for (Entity e : targetEntities) {
                float dst = e.getPosition().dst(termite.getPosition());
                if (dst < closestEntity.getPosition().dst(termite.getPosition())) {
                    closestEntity = e;
                }
            }
            targetV = closestEntity.getPosition().sub(termite.getPosition());
        } else {
            // TODO gameWorld might better as singleton
            List<Structure> structures = termite.getGameWorld().getStructures();
            for (Structure s : structures) {
                // TODO, TOO BAD!!!
                if (s.getClass().isInstance(new Colony(new GridPoint2(0, 0)))) {
                    targetV = s.getPosition().sub(termite.getPosition());
                }
            }
        }
        // move to target
        // TODO might overshoot, TOO BAD!
        if(targetV.len() > 0.01f) {
            termite.setVelocity(targetV.nor().scl(termite.getSpeed()));
        }
    }

        List<Entity> hostileEntities(List<Entity> entities) {
            List<Entity> targetEntities = new ArrayList<>();
            for (Entity e : entities) {
                if (e.getFaction() != termite.getFaction()) {
                    targetEntities.add(e);
                }
            }
            return targetEntities;
        }

}
