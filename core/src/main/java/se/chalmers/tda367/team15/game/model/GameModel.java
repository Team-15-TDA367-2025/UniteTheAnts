package se.chalmers.tda367.team15.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.chalmers.tda367.team15.game.model.egg.EggManager;
import se.chalmers.tda367.team15.game.model.entity.Entity;
import se.chalmers.tda367.team15.game.model.entity.ant.Ant;
import se.chalmers.tda367.team15.game.model.entity.ant.AntTypeRegistry;
import se.chalmers.tda367.team15.game.model.fog.FogProvider;
import se.chalmers.tda367.team15.game.model.interfaces.ColonyUsageProvider;
import se.chalmers.tda367.team15.game.model.interfaces.EntityQuery;
import se.chalmers.tda367.team15.game.model.interfaces.GameObject;
import se.chalmers.tda367.team15.game.model.interfaces.PheromoneUsageProvider;
import se.chalmers.tda367.team15.game.model.interfaces.TimeCycleDataProvider;
import se.chalmers.tda367.team15.game.model.managers.StructureManager;
import se.chalmers.tda367.team15.game.model.world.MapProvider;

public class GameModel {
    private final ColonyUsageProvider colonyUsageProvider;
    // TODO: Fix
    private final TimeCycleDataProvider timeProvider;
    private final FogProvider fogProvider;
    private final SimulationProvider simulationProvider;
    private final PheromoneUsageProvider pheromoneUsageProvider;
    private final MapProvider mapProvider;
    private final AntTypeRegistry antTypeRegistry;
    private final StructureManager structureManager;
    private final EntityQuery entityQuery;

    public GameModel(SimulationProvider simulationProvider, TimeCycleDataProvider timeProvider,
            FogProvider fogProvider, ColonyUsageProvider colonyUsageProvider,
            PheromoneUsageProvider pheromoneUsageProvider,
            MapProvider mapProvider, AntTypeRegistry antTypeRegistry, StructureManager structureManager,
            EntityQuery entityQuery) {
        this.simulationProvider = simulationProvider;
        this.colonyUsageProvider = colonyUsageProvider;
        this.timeProvider = timeProvider;
        this.fogProvider = fogProvider;
        this.pheromoneUsageProvider = pheromoneUsageProvider;
        this.mapProvider = mapProvider;
        this.antTypeRegistry = antTypeRegistry;
        this.structureManager = structureManager;
        this.entityQuery = entityQuery;
    }

    public Collection<GameObject> getDrawables() {
        List<GameObject> allDrawables = new ArrayList<>(structureManager.getStructures());
        allDrawables.addAll(entityQuery.getEntitiesOfType(Entity.class));
        return Collections.unmodifiableList(allDrawables);
    }

    public ColonyUsageProvider getColonyUsageProvider() {
        return colonyUsageProvider;
    }

    public TimeCycleDataProvider getTimeProvider() {
        return timeProvider;
    }

    public PheromoneUsageProvider getPheromoneUsageProvider() {
        return pheromoneUsageProvider;
    }

    // --- FACADE METHODS (Actions) ---

    public void setTimeFast() {
        simulationProvider.setTimeFast();
    }

    public void setTimeNormal() {
        simulationProvider.setTimeNormal();
    }

    public void setTimePaused() {
        simulationProvider.setTimePaused();
    }

    public void update() {
        simulationProvider.handleSimulation();
    }

    public FogProvider getFogProvider() {
        return fogProvider;
    }

    public AntTypeRegistry getAntTypeRegistry() {
        return antTypeRegistry;
    }

    public MapProvider getMapProvider() {
        return mapProvider;
    }

    public EggManager getEggManager() {
        return colonyUsageProvider.getEggManager();
    }

    public int getTotalAnts() {
        return entityQuery.getEntitiesOfType(Ant.class).size();
    }
}
