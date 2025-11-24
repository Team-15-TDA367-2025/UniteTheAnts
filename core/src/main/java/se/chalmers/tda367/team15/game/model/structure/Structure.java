package se.chalmers.tda367.team15.game.model.structure;

import com.badlogic.gdx.math.Vector2;

import se.chalmers.tda367.team15.game.model.Drawable;
import se.chalmers.tda367.team15.game.model.Vec2i;

public class Structure implements Drawable {
    private Vec2i position;
    private String textureName;
    private int radius;

    public Structure(Vec2i position, String textureName, int radius) {
        this.position = position;
        this.textureName = textureName;
        this.radius = radius;
    }
    
    @Override
    public Vector2 getPosition() {
        return position.toVector2();
    }

    @Override
    public String getTextureName() {
        return textureName;
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(radius * 2, radius * 2);
    }
}
