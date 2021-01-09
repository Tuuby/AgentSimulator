package gameObjects;

import de.Tuuby.AgentSimulator.engine.GameLoop;
import de.Tuuby.AgentSimulator.graphics.Renderer;

public class goTest {
    public static void main(String[] args) {
        Renderer.init();
        GameLoop.start();
        //WorldUpdater.addObject(new TestGO((int) (Renderer.unitsWide / 2), (int) (Renderer.unitsTall / 2), 2, 2));
    }
}
