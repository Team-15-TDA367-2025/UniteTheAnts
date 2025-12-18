package se.chalmers.tda367.team15.game;

import com.badlogic.gdx.math.GridPoint2;

public record GameLaunchConfiguration(boolean unlimitedFps, boolean noFog, int startWorkers, int seed,
        int startResources, GridPoint2 mapSize) {

    private static GameLaunchConfiguration current;

    public static void setCurrent(GameLaunchConfiguration gameLaunchConfiguration) {
        current = gameLaunchConfiguration;
    }

    public static GameLaunchConfiguration getCurrent() {
        return current;
    }
}
