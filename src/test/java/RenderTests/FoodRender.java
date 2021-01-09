package RenderTests;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.graphics.Renderer;

public class FoodRender {
    public static void main(String[] args) {
        Renderer.init();
        GameLoop.start();
        //WorldUpdater.addObject(new Food(1, 1, null));
    }
}
