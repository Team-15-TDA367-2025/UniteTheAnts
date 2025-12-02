package se.chalmers.tda367.team15.game.model;

import se.chalmers.tda367.team15.game.model.faction.Faction;

/**
 * Represents the notion that the Object can be destroyed or die in the game world.
 */
public interface CanBeAttacked {
    /**
     * Instructs the object to take damage.
     *
     * @param amount the damage taken.
     */
    void takeDamage(float amount);

    /**
     * Instructs the object to notify the {@link se.chalmers.tda367.team15.game.model.DestructionListener} to remove it from the {@link se.chalmers.tda367.team15.game.model.GameWorld}
     */
    void die();

    AttackCategory getAttackCategory();

    Faction getFaction();
}
