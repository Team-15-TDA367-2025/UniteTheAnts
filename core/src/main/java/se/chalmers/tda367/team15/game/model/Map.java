package se.chalmers.tda367.team15.game.model;

import java.util.List;

import se.chalmers.tda367.team15.game.model.objects.WorldObject;

public class Map {
    private int size;
    private int discoveredArea;
    private List<WorldObject> objects;
    private Tile[][] tiles;

    public Map(int size, Tile[][] tiles, List<WorldObject> objects) {
        this.size = size;
        this.tiles = tiles;
        this.objects = objects;
        this.discoveredArea = 0;
    }
}
