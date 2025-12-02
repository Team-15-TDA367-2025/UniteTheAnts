package se.chalmers.tda367.team15.game.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import se.chalmers.tda367.team15.game.model.world.Tile;
import se.chalmers.tda367.team15.game.model.world.WorldMap;

public class TerrainRenderer {
    private final TextureRegistry textureRegistry;
    private final List<String> textureNames;

    public TerrainRenderer(TextureRegistry textureRegistry) {
        this.textureRegistry = textureRegistry;
        this.textureNames = new ArrayList<>(List.of("grass1", "grass2", "grass3"));
    }

    public void render(SpriteBatch batch, WorldMap worldMap, CameraView cameraView) {
        final GridPoint2 size = worldMap.getSize();
        final float offsetX = -size.x / 2f;
        final float offsetY = -size.y / 2f;

        Vector2 cameraPos = cameraView.getPosition();
        Vector2 viewportSize = cameraView.getEffectiveViewportSize();

        Vector2 halfViewport = new Vector2(viewportSize).scl(0.5f);
        Vector2 leftBottom = new Vector2(cameraPos).sub(halfViewport).sub(1, 1);
        Vector2 rightTop = new Vector2(cameraPos).add(halfViewport).add(1, 1);

        GridPoint2 startTile = worldMap.worldToTile(leftBottom);
        GridPoint2 endTile = worldMap.worldToTile(rightTop);

        int startX = Math.max(0, startTile.x);
        int endX = Math.min(size.x, endTile.x + 1);
        int startY = Math.max(0, startTile.y);
        int endY = Math.min(size.y, endTile.y + 1);

        for (String textureName : textureNames) {
            TextureRegion texture = textureRegistry.get(textureName);
            renderTilesOfTexture(batch, worldMap, startX, endX, startY, endY, offsetX, offsetY, textureName, texture);
        }
    }

    private void renderTilesOfTexture(SpriteBatch batch, WorldMap worldMap, int startX, int endX, int startY, int endY,
            float offsetX, float offsetY, String textureName, TextureRegion texture) {
        GridPoint2 tilePos = new GridPoint2();

        for (int y = startY; y < endY; y++) {
            tilePos.y = y;
            float worldY = y + offsetY;
            for (int x = startX; x < endX; x++) {
                tilePos.x = x;
                Tile tile = worldMap.getTile(tilePos);
                if (tile != null && textureName.equals(tile.getTextureName())) {
                    float worldX = x + offsetX;
                    batch.draw(texture, worldX, worldY, 1, 1);
                }
            }
        }
    }
}
