package se.chalmers.tda367.team15.game.model.structure.resource;

import com.badlogic.gdx.math.GridPoint2;

import se.chalmers.tda367.team15.game.model.structure.Structure;

public class Resource extends Structure {
    private ResourceType type;
    private int amount;

    public Resource(GridPoint2 position, ResourceType type, int amount) {
        super(position, 1);
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String getTypeId() {
        return "resource_" + type.name().toLowerCase();
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
