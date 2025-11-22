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
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector2 mouseDelta = new Vector2(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
            Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Vector2 viewportSize = cameraView.getViewportSize();
            
            Vector2 worldDelta = model.screenDeltaToWorldDelta(mouseDelta, screenSize, viewportSize);
            model.pan(worldDelta);
            model.applyConstraints(viewportSize);
        }
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Vector2 screenPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector2 viewportSize = cameraView.getViewportSize();
        
        model.zoomAround(screenPos, amountY, zoomSpeed, screenSize, viewportSize);
        model.applyConstraints(viewportSize);
        
        // Update camera view to reflect new zoom
        cameraView.updateCamera();
        
        return true;
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = speed;
    }
}
