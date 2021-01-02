import engine.GameLoop;
import engine.WorldUpdater;
import graphics.Renderer;
import world.WorldGenerator;

public class main {

    public static void main(String[] args) {
        Renderer.init();
        WorldGenerator worldGen = new WorldGenerator(800, 800, 10, 0, null,
                                            10, 1, 0, 0, 0,
                                            0,0, 10);
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
