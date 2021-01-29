package de.Tuuby.AgentSimulator;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.graphics.StatusIconFactory;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;
import de.Tuuby.AgentSimulator.world.WorldGenerator;

import java.util.Properties;

public class main {

    public static void main(String[] args) {
        PropertiesLoader.loadConfig();
        StatusIconFactory.initStatuses();
        Renderer.init();
        WorldGenerator worldGen = new WorldGenerator(PropertiesLoader.getAppConfig());
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
