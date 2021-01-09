package de.tuuby.AgentSimulator;

import de.tuuby.AgentSimulator.engine.GameLoop;
import de.tuuby.AgentSimulator.engine.WorldUpdater;
import de.tuuby.AgentSimulator.graphics.Renderer;
import de.tuuby.AgentSimulator.world.WorldGenerator;

public class main {

    public static void main(String[] args) {
        Renderer.init();
        WorldGenerator worldGen = new WorldGenerator(800, 800, 15, 0, null,
                                            30, 10, 6, 0, 0,
                                            0,0, 20);
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
