package se.chalmers.tda367.team15.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import se.chalmers.tda367.team15.game.model.CameraModel;

public class CameraView {
  private OrthographicCamera camera;
  private CameraModel model;
  private float viewportWidth;
  private float viewportHeight;

  public CameraView(CameraModel model, float viewportWidth, float viewportHeight) {
    this.model = model;

    // Create camera with aspect ratio consideration
    this.camera = new OrthographicCamera(0, 0);
    this.setViewport(viewportWidth, viewportHeight);
    updateCamera();
  }

  public void updateCamera() {
    camera.position.set(new Vector3(model.getPosition(), 0));
    camera.zoom = 1f / model.getZoom();
    camera.update();
  }

  public void setViewport(float width, float height) {
    this.viewportWidth = width;
    this.viewportHeight = height;

    float aspectRatio = height / width;
    camera.viewportWidth = width;
    camera.viewportHeight = width * aspectRatio;
    camera.update();
  }

  public Matrix4 getCombinedMatrix() {
    return camera.combined;
  }

  public OrthographicCamera getCamera() {
    return camera;
  }

  public Vector2 getViewportSize() {
    // We copy the vector to avoid modifying it
    return new Vector2(viewportWidth, viewportHeight).cpy();
  }
}
