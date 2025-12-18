package se.chalmers.tda367.team15.game.model.structure;

import com.badlogic.gdx.math.GridPoint2;

import se.chalmers.tda367.team15.game.model.AttackCategory;
import se.chalmers.tda367.team15.game.model.DestructionListener;
import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.Inventory;
import se.chalmers.tda367.team15.game.model.faction.Faction;
import se.chalmers.tda367.team15.game.model.interfaces.CanBeAttacked;
import se.chalmers.tda367.team15.game.model.interfaces.ColonyDataProvider;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.Home;
import se.chalmers.tda367.team15.game.model.interfaces.TimeObserver;
import se.chalmers.tda367.team15.game.model.structure.resource.ResourceType;

public class Colony extends Structure implements CanBeAttacked, Home, TimeObserver, ColonyDataProvider {
    private Inventory inventory;
    private float health;
    private float MAX_HEALTH = 600;
    private Faction faction;
    private final EntityQuery entityQuery;
    private final DestructionListener destructionListener;

    public Colony(GridPoint2 position, EntityQuery entityQuery,
            DestructionListener destructionListener, int initialFood) {
        super(position, "colony", 4);
        this.health = MAX_HEALTH;
        this.faction = Faction.DEMOCRATIC_REPUBLIC_OF_ANTS;
        this.inventory = new Inventory(1000000); // test value for now
        this.inventory.addResource(ResourceType.FOOD, initialFood);
        this.entityQuery = entityQuery;
        this.destructionListener = destructionListener;
    }

    @Override
    public boolean depositResources(Inventory otherInventory) {
        boolean deposited = false;

        for (ResourceType type : ResourceType.values()) {
            int total = otherInventory.getAmount(type);
            if (total > 0) {
                inventory.addResource(type, total);
                deposited = true;
            }
        }
        return deposited;
    }

    @Override
    public int getConsumption() {
        int total = 0;
        for (Ant ant : entityQuery.getEntitiesOfType(Ant.class)) {
            total += ant.getHunger();
        }
        return total;
    }

    public void applyConsumption(int amount) {
        inventory.addResource(ResourceType.FOOD, -amount);
    }

    public int getTotalResources(ResourceType type) {
        return inventory.getAmount(type);
    }

    public boolean spendResources(ResourceType type, int amount) {
        return inventory.addResource(type, -amount);
    }

    public void onDayStart() {
        applyConsumption(getConsumption());
    }

    @Override
    public Faction getFaction() {
        return faction;
    }

    @Override
    public void takeDamage(float amount) {
        health = Math.max(0f, health - amount);
        if (health == 0f) {
            die();
        }
    }

    @Override
    public void die() {
        destructionListener.notifyStructureDeathObservers(this);
    }

    @Override
    public AttackCategory getAttackCategory() {
        return AttackCategory.ANT_COLONY;
    }

    @Override
    public int getTotalAnts() {
        // TODO: fix this
        return entityQuery.getEntitiesOfType(Ant.class).size();
    }
}
