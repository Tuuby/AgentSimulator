package RenderTests;

import engine.GameLoop;
import engine.WorldUpdater;
import graphics.Renderer;
import world.Food;

public class FoodRender {
    public static void main(String[] args) {
        Renderer.init();
        GameLoop.start();
        //WorldUpdater.addObject(new Food(1, 1, null));
    }
}
