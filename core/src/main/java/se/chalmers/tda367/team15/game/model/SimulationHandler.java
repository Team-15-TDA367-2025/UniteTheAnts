package se.chalmers.tda367.team15.game.model;

import se.chalmers.tda367.team15.game.model.entity.Entity;
import se.chalmers.tda367.team15.game.model.interfaces.Updatable;

import java.util.ArrayList;
import java.util.List;

public class SimulationHandler {
    private final TimeCycle timeCycle;

    private static final int baseTickPerSecond = 20;
    private static final double inGameTimePerTickMs = 1000.0 / baseTickPerSecond;

    private int iRLTicksPerSecond = baseTickPerSecond;
    private double accumulator = 0;
    private long previous = System.currentTimeMillis();

    private long now = System.currentTimeMillis();
    private boolean paused=false;

    private final List<Updatable> updateObservers = new ArrayList<>();


   public SimulationHandler(TimeCycle timeCycle) {
        this.timeCycle = timeCycle;
   }

   public void addUpdateObserver(Updatable u) {
       updateObservers.add(u);
   }

   public static double getInGameTimePerTickMs() {
       return inGameTimePerTickMs;
   }

    public void setTicksPerSecond(int ticksPerSecond) {
        if(ticksPerSecond < 0) {
            throw new IllegalArgumentException("ticks per second can't be negative");
        }
        this.iRLTicksPerSecond = ticksPerSecond;
    }

    public TimeCycle getTimeCycle() {
        return timeCycle;
    }

    public void handleSimulation() {
       boolean oldPause = paused;
       paused= iRLTicksPerSecond == 0;
        // Only update this.now if we aren't exiting out of pause, use old value instead.
        // This prevents "catch up" when exiting pause.
        if(!(oldPause && !paused)) {
            now=System.currentTimeMillis();
        }

        if(!(iRLTicksPerSecond==0)) {
            long mSPerTick = mSPerTick();
            long difference = now - previous;
            previous = now;

            accumulator += difference;
            while (accumulator >= mSPerTick) {
                float inGameTimeDifference = (float) inGameTimePerTickMs / 1000f;
                timeCycle.tick(); // time cycle needs to update first

                for(Updatable u: updateObservers) {
                    u.update();
                }

                accumulator -= mSPerTick;
            }
        }
    }
    private long mSPerTick() {
        if(iRLTicksPerSecond==0) {
            throw new IllegalStateException("infinite time for each frame is undefined so the game shouldn't run");
        }
        return 1000/iRLTicksPerSecond;
    }

}
