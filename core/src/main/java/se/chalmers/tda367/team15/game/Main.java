package se.chalmers.tda367.team15.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import se.chalmers.tda367.team15.game.controller.CameraController;
import se.chalmers.tda367.team15.game.controller.ViewportListener;
import se.chalmers.tda367.team15.game.model.GameWorld;
import se.chalmers.tda367.team15.game.model.camera.CameraConstraints;
import se.chalmers.tda367.team15.game.model.camera.CameraModel;
import se.chalmers.tda367.team15.game.model.entity.Ant;
import se.chalmers.tda367.team15.game.view.CameraView;
import se.chalmers.tda367.team15.game.view.GridView;
import se.chalmers.tda367.team15.game.view.HUDView;
import se.chalmers.tda367.team15.game.view.SceneView;
import se.chalmers.tda367.team15.game.view.TextureRegistry;

public class Main extends ApplicationAdapter {
    // World bounds - adjust these to match your game world size
    private static final float WORLD_SIZE = 200f;
    private static final float WORLD_VIEWPORT_WIDTH = 30f;

    private static final float MIN_ZOOM = 0.15f;
    private static final float MAX_ZOOM = 4.0f;

    // MVC components
    private GameWorld gameWorld;
    private CameraModel cameraModel;
    private CameraView worldCameraView;
    private OrthographicCamera hudCamera;
    private CameraController cameraController;
    private ViewportListener viewportListener;
    
    // Views
    private SceneView sceneView;
    private GridView gridView;
    private HUDView hudView;
    private TextureRegistry textureRegistry;

    @Override
    public void create() {
        // Initialize world bounds and constraints
        Rectangle worldBounds = new Rectangle(-WORLD_SIZE / 2f, -WORLD_SIZE / 2f, WORLD_SIZE, WORLD_SIZE);
        CameraConstraints constraints = new CameraConstraints(worldBounds, MIN_ZOOM, MAX_ZOOM);

        // Initialize model
        cameraModel = new CameraModel(constraints);
        gameWorld = new GameWorld();
        gameWorld.addEntity(new Ant(new Vector2(0, 0)));
        gameWorld.addEntity(new Ant(new Vector2(0, 0)));
        gameWorld.addEntity(new Ant(new Vector2(0, 0)));
        gameWorld.addEntity(new Ant(new Vector2(0, 0)));
        gameWorld.addEntity(new Ant(new Vector2(0, 0)));

        // Initialize cameras
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float aspectRatio = screenHeight / screenWidth;

        // World camera
        worldCameraView = new CameraView(cameraModel, WORLD_VIEWPORT_WIDTH, WORLD_VIEWPORT_WIDTH * aspectRatio);

        cameraController = new CameraController(cameraModel, worldCameraView);
        Gdx.input.setInputProcessor(cameraController);

        // HUD camera
        hudCamera = new OrthographicCamera(screenWidth, screenHeight);
        hudCamera.setToOrtho(false, screenWidth, screenHeight);

        // Setup viewport listener with resize handlers
        viewportListener = new ViewportListener();
        // World camera: maintain aspect ratio with fixed viewport width
        viewportListener.addResizeHandler((width, height) -> {
            float worldAspectRatio = (float) height / (float) width;
            worldCameraView.setViewport(WORLD_VIEWPORT_WIDTH, WORLD_VIEWPORT_WIDTH * worldAspectRatio);
        });

        // HUD camera: match screen dimensions exactly
        viewportListener.addResizeHandler((width, height) -> {
            hudCamera.setToOrtho(false, width, height);
            hudCamera.update();
            hudView.updateProjectionMatrix(hudCamera);
        });

        // Setup Rendering System
        textureRegistry = new TextureRegistry();
        sceneView = new SceneView(worldCameraView, textureRegistry);
        gridView = new GridView(worldCameraView, 5f);
        hudView = new HUDView(cameraModel, worldCameraView, hudCamera);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // Update
        cameraController.update();
        worldCameraView.updateCamera();
        gameWorld.update(dt);

        // Render
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // World Render
        sceneView.render(gameWorld);
        
        // Debug Grid
        gridView.render();

        // HUD Render
        hudView.render();
    }

    @Override
    public void resize(int width, int height) {
        viewportListener.resize(width, height);
    }

    @Override
    public void dispose() {
        sceneView.dispose();
        gridView.dispose();
        hudView.dispose();
        textureRegistry.dispose();
    }
}
