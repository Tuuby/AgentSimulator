package gameObjects;

import engine.GameLoop;
import graphics.Renderer;
import world.World;

public class goTest {
    public static void main(String[] args) {
        Renderer.init();
        GameLoop.start();
        World.addObject(new TestGO(Renderer.unitsWide / 2, Renderer.unitsTall / 2, 2, 2));
    }
}