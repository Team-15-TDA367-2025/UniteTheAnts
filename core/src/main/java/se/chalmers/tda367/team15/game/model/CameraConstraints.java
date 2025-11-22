package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CameraConstraints {
    private float minZoom;
    private float maxZoom;

    // You can never move the camera so that any part is outside of this rectangle.
    private Rectangle bounds;

    public CameraConstraints(Rectangle bounds, float minZoom, float maxZoom) {
        if (minZoom <= 0) {
            throw new IllegalArgumentException("Min zoom must be greater than 0");
        }
        if (maxZoom <= minZoom) {
            throw new IllegalArgumentException("Max zoom must be greater than min zoom");
        }

        this.bounds = bounds;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    public float getMinZoom() {
        return minZoom;
    }

    public float getMaxZoom() {
        return maxZoom;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isInBounds(Rectangle rect) {
        return bounds.contains(rect);
    }

    /**
     * Constrains a camera position considering the viewport size and zoom.
     * Ensures that the camera viewport corners stay within bounds when possible.
     * @param position The current camera position
     * @param zoom The current zoom level
     * @param viewportSize The viewport size in world units (before zoom)
     * @return A new Vector2 with the constrained position
     */
    public Vector2 constrainPosition(Vector2 position, float zoom, Vector2 viewportSize) {
        // Calculate effective viewport size (what we actually see in world coordinates)
        Vector2 effectiveViewportSize = viewportSize.cpy().scl(1f / zoom);
        Vector2 halfViewport = effectiveViewportSize.cpy().scl(0.5f);
        
        // Calculate valid center position bounds
        float minCenterX = bounds.x + halfViewport.x;
        float maxCenterX = bounds.x + bounds.width - halfViewport.x;
        float minCenterY = bounds.y + halfViewport.y;
        float maxCenterY = bounds.y + bounds.height - halfViewport.y;
        
        // If viewport exceeds bounds, center camera on bounds center
        Vector2 boundsCenter = new Vector2(bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);
        if (effectiveViewportSize.x > bounds.width) {
            minCenterX = maxCenterX = boundsCenter.x;
        }
        if (effectiveViewportSize.y > bounds.height) {
            minCenterY = maxCenterY = boundsCenter.y;
        }
        
        // Return constrained position
        return new Vector2(
            MathUtils.clamp(position.x, minCenterX, maxCenterX),
            MathUtils.clamp(position.y, minCenterY, maxCenterY)
        );
    }
}
