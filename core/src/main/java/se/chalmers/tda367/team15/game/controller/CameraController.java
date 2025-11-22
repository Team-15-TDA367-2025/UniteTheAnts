package se.chalmers.tda367.team15.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import se.chalmers.tda367.team15.game.model.CameraModel;
import se.chalmers.tda367.team15.game.view.CameraView;

public class CameraController extends InputAdapter {
    private CameraModel model;
    private CameraView cameraView;
    private float zoomSpeed = 0.1f;

    public CameraController(CameraModel model, CameraView cameraView) {
        this.model = model;
        this.cameraView = cameraView;
    }

    public void update() {
        handlePanInput();
    }

    private void handlePanInput() {
        // Panning the camera
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 delta = new Vector2(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());

            // Convert screen pixel movement to world coordinates
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            Vector2 effectiveViewportSize = cameraView.getViewportSize().scl(1 / model.getZoom());

            // Convert pixel delta to world delta
            Vector2 worldDelta = new Vector2((delta.x / screenWidth) * effectiveViewportSize.x,
                    (delta.y / screenHeight) * effectiveViewportSize.y);

            // Move camera in opposite direction of mouse drag
            model.moveBy(worldDelta.scl(-1));
        }
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float currentZoom = model.getZoom();
        float zoomMultiplier = 1f + (-amountY * zoomSpeed);
        float newZoom = currentZoom * zoomMultiplier;
        model.zoomTo(newZoom);
        return true;
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = speed;
    }
}
