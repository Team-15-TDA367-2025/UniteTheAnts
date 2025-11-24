package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.Vector2;

public class Vec2i {
    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i add(Vec2i other) {
        return new Vec2i(x + other.x, y + other.y);
    }

    public Vec2i subtract(Vec2i other) {
        return new Vec2i(x - other.x, y - other.y);
    }

    public Vector2 toVector2() {
        return new Vector2(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vec2i vec2i = (Vec2i) obj;
        return x == vec2i.x && y == vec2i.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
