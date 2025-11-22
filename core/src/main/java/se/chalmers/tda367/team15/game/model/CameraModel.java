package se.chalmers.tda367.team15.game.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CameraModel {
  private Vector2 position = new Vector2(0, 0);
  private float zoom = 1f;
  private CameraConstraints constraints;

  public CameraModel(CameraConstraints constraints) {
    this.constraints = constraints;
  }

  public Vector2 getPosition() {
    return position;
  }

  public float getZoom() {
    return zoom;
  }

  public Rectangle getBounds() {
    return new Rectangle(position.x, position.y, zoom, zoom);
  }

  public void moveTo(Vector2 newPosition) {
    position.set(newPosition);
    applyConstraints();
  }

  public void moveBy(Vector2 delta) {
    position.add(delta);
    applyConstraints();
  }

  public void zoomTo(float newZoom) {
    this.zoom = MathUtils.clamp(newZoom, constraints.getMinZoom(), constraints.getMaxZoom());
  }

  private void applyConstraints() {
    Rectangle bounds = constraints.getBounds();
    position.x = MathUtils.clamp(position.x, bounds.x, bounds.x + bounds.width);
    position.y = MathUtils.clamp(position.y, bounds.y, bounds.y + bounds.height);
  }
}
