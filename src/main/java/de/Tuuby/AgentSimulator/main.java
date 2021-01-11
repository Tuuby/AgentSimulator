package de.Tuuby.AgentSimulator;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.world.WorldGenerator;

public class main {

    public static void main(String[] args) {
        Renderer.init();
        WorldGenerator worldGen = new WorldGenerator(800, 800, 15, 0, null,
                                            6, 10, 6, 0, 0,
                                            0,0, 20);
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
