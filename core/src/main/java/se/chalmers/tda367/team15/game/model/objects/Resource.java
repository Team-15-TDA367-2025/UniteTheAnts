package se.chalmers.tda367.team15.game.model.objects;

public class Resource extends WorldObject {
    private ResourceType type;
    private int quantity;

    public Resource(int x, int y, ResourceType type, int quantity) {
        super(x, y);
        this.type = type;
        this.quantity = quantity;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
