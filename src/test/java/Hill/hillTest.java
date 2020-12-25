package Hill;

import engine.GameLoop;
import graphics.Renderer;
import world.Hill;
import world.WorldUpdater;

import java.util.Random;

public class hillTest {
    public static void main(String[] args) {
        Random rnd = new Random();

        Renderer.init();
        GameLoop.start();
        for (int i = 0; i < 10; i++) {
            WorldUpdater.addObject(new Hill(rnd.nextInt(100) / 10f, rnd.nextInt(200) / 10f, null, 1));
        }
    }
}
