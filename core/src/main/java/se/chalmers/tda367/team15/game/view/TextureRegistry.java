package se.chalmers.tda367.team15.game.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Centralized texture management.
 * Loads textures from assets directory and caches them.
 * packs them into a runtime TextureAtlas for performance.
 */
public class TextureRegistry {
    private final Map<String, TextureRegion> textures = new HashMap<>();
    private TextureAtlas atlas;

    public TextureRegistry() {
        // 2048x2048 should be sufficient for our assets.
        // using 2 pixel padding to avoid bleeding
        PixmapPacker packer = new PixmapPacker(256, 256, Pixmap.Format.RGBA8888, 2, true);

        loadDirectory(packer, ".");
        loadDirectory(packer, "TopBar");
        loadDirectory(packer, "BottomBar");
        createPixelTexture(packer);

        // Generate the atlas
        atlas = packer.generateTextureAtlas(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, false);
        
        // Cache regions for O(1) lookup
        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            textures.put(region.name, region);
        }

        // We don't need the packer anymore
        packer.dispose();
    }

    /**
     * Loads all PNG files from the specified directory into the packer.
     */
    private void loadDirectory(PixmapPacker packer, String path) {
        FileHandle dir = Gdx.files.internal(path);
        if (!dir.exists())
            return;

        String prefix = (path.equals(".") || path.equals("./")) ? "" : path + "/";

        for (FileHandle file : dir.list()) {
            if (file.extension().equalsIgnoreCase("png") && !file.name().startsWith("c__")) {
                try {
                    String key = prefix + file.nameWithoutExtension();
                    Pixmap pixmap = new Pixmap(file);
                    packer.pack(key, pixmap);
                    pixmap.dispose();
                } catch (Exception e) {
                    // Skip files that can't be loaded as textures
                    System.err.println("Warning: Could not load texture: " + file.name());
                }
            }
        }
    }

    public TextureRegion get(String name) {
        TextureRegion region = textures.get(name);
        if (region == null) {
            throw new IllegalArgumentException("Texture not found: " + name);
        }
        return region;
    }

    public boolean has(String name) {
        return textures.containsKey(name);
    }

    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
        textures.clear();
    }

    private void createPixelTexture(PixmapPacker packer) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fill();
        packer.pack("pixel", pixmap);
        pixmap.dispose();
    }
}
