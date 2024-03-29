package UI;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

public class LineTest {
    public static void main(String[] args) {
        PropertiesLoader.loadConfig();
        Renderer.init();
        WorldUpdater.addGUI(new LineObject(100, 100, 100, 700));
        GameLoop.start();
    }
}
