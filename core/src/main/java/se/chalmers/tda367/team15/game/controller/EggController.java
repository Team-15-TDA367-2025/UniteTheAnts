package se.chalmers.tda367.team15.game.controller;

import se.chalmers.tda367.team15.game.model.GameModel;
import se.chalmers.tda367.team15.game.model.entity.ant.AntType;

public class EggController {
    private final GameModel model;

    public EggController(GameModel model) {
        this.model = model;
    }

    /**
     * Attempts to purchase an egg of the specified type.
     *
     * @param typeId the ID of the egg type to purchase
     * @return true if the purchase was successful, false otherwise
     */
    public boolean purchaseEgg(String typeId) {
        //TODO: this is kinda ugly
        AntType type = model.getAntTypeRegistry().get(typeId);

        if (type == null) {
            return false;
        }

        return model.getColonyUsageProvider().purchaseEgg(type);
    }
}
