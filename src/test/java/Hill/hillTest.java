package Hill;

import de.tuuby.AgentSimulator.engine.GameLoop;
import de.tuuby.AgentSimulator.graphics.Renderer;

import java.util.Random;

public class hillTest {
    public static void main(String[] args) {
        Random rnd = new Random();

        Renderer.init();
        GameLoop.start();
        for (int i = 0; i < 10; i++) {
            //WorldUpdater.addObject(new Hill(rnd.nextInt(100) / 10, rnd.nextInt(200) / 10, null, 1));
        }
    }
}
