package UI;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.engine.WorldUpdater;
import de.Tuuby.AgentSimulator.graphics.Renderer;
import de.Tuuby.AgentSimulator.guis.Button;
import de.Tuuby.AgentSimulator.guis.GUIMouseListener;
import de.Tuuby.AgentSimulator.guis.WindowManager;
import de.Tuuby.AgentSimulator.resource.PropertiesManager;

public class ButtonTest {
    public static void main(String[] args) {
        PropertiesManager.loadConfig();
        Renderer.init();
        WindowManager.init();
        Button kevin = new Button(400, 400, 200, 200, "Button");
        kevin.setMouseListener(new GUIMouseListener() {
            public void onMouseButtonClick() {
                System.out.println("Button click worked");
            }

            public void onMouseButtonDown() {

            }

            public void onMouseButtonRelease() {

            }

            public void onMouseOver() {

            }
        });
        WindowManager.register(kevin);
        WorldUpdater.addGUI(kevin);
        GameLoop.start();
    }
}
