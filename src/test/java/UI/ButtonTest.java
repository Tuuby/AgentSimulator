package UI;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.guis.Button;
import de.Tuuby.AgentSimulator.resource.PropertiesLoader;

public class ButtonTest {
    public static void main(String[] args) {
        PropertiesLoader.loadConfig();
        Renderer.init();
        WorldUpdater.addGUI(new Button(400, 400, 200, 200, "Button"));
        GameLoop.start();
    }
}
