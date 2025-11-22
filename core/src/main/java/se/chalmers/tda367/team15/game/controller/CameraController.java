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
            Vector2 screenDelta = new Vector2(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
            Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            
            // Calculate effective viewport size and convert screen delta to world delta
            Vector2 effectiveViewportSize = cameraView.getViewportSize().cpy().scl(1f / model.getZoom());
            Vector2 worldDelta = screenDelta.cpy().scl(effectiveViewportSize.x / screenSize.x, 
                                                       effectiveViewportSize.y / screenSize.y);

            // Move camera in opposite direction of mouse drag
            model.moveBy(worldDelta.scl(-1));
            // Apply constraints with viewport size to ensure corners stay within bounds
            model.applyConstraints(cameraView.getViewportSize());
        }
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Get cursor position in screen coordinates
        float screenX = Gdx.input.getX();
        float screenY = Gdx.input.getY();
        
        // Convert cursor position to world coordinates before zoom
        Vector2 worldPosBeforeZoom = cameraView.screenToWorld(screenX, screenY);
        
        // Apply zoom
        float currentZoom = model.getZoom();
        float zoomMultiplier = 1f + (-amountY * zoomSpeed);
        float newZoom = currentZoom * zoomMultiplier;
        model.zoomTo(newZoom);
        
        // Update camera view to reflect new zoom
        cameraView.updateCamera();
        
        // Convert cursor position to world coordinates after zoom
        Vector2 worldPosAfterZoom = cameraView.screenToWorld(screenX, screenY);
        
        // Adjust camera position so the cursor stays at the same world position
        Vector2 offset = worldPosBeforeZoom.cpy().sub(worldPosAfterZoom);
        model.moveBy(offset);
        
        // Apply constraints with viewport size to ensure corners stay within bounds
        // This will clamp the camera if zooming out caused it to go beyond bounds
        model.applyConstraints(cameraView.getViewportSize());
        
        return true;
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = speed;
    }
}
