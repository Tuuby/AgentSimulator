import engine.GameLoop;
import engine.WorldUpdater;
import graphics.Renderer;
import world.WorldGenerator;

public class main {

    public static void main(String[] args) {
        Renderer.init();
        GameLoop.start();
        WorldGenerator worldGen = new WorldGenerator(800, 800, 10, 0, null,
                                            10, 8, 6, 2, 2,
                                            2,0, 1);
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
    }
}
