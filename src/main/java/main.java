import engine.GameLoop;
import engine.WorldUpdater;
import graphics.Renderer;
import world.WorldGenerator;

public class main {

    public static void main(String[] args) {
        Renderer.init();
        WorldGenerator worldGen = new WorldGenerator(800, 800, 10, 0, null,
                                            20, 2, 0, 0, 0,
                                            0,0, 20);
        worldGen.generate();
        WorldUpdater.addWorld(worldGen.getWorld());
        GameLoop.start();
    }
}
