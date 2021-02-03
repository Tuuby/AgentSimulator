package de.Tuuby.AgentSimulator;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.graphics.StatusIconFactory;
import de.Tuuby.AgentSimulator.guis.GuiFactory;
import de.Tuuby.AgentSimulator.guis.WindowManager;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;
import de.Tuuby.AgentSimulator.world.WorldGenerator;

public class main {

    public static WorldGenerator worldGen;

    public static void main(String[] args) {
        PropertiesLoader.loadConfig();
        StatusIconFactory.initStatuses();
        Renderer.init();
        WindowManager.init();
        GuiFactory.buildUI();
        worldGen = new WorldGenerator(PropertiesLoader.getAppConfig());
        worldGen.generate();
        WorldUpdater.setWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
