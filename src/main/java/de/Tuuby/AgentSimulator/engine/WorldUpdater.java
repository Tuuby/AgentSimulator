package de.Tuuby.AgentSimulator.engine;

import com.jogamp.newt.event.KeyEvent;
import de.Tuuby.AgentSimulator.guis.GuiElement;
import de.Tuuby.AgentSimulator.input.KeyInput;
import de.Tuuby.AgentSimulator.world.World;

import java.util.Vector;

// Prototype class that represents the world and all the GameObjects on it
public class WorldUpdater {

    // List of worlds that need to be updated
    //private static Vector<World> worlds = new Vector<World>();

    // World that needs to be updated
    private static World world;
    // List of all GUI Elements
    private static Vector<GuiElement> guiElements = new Vector<GuiElement>();

    // Say that extra debug info should be rendered
    public static boolean debugMode = false;

    public static void toggleDebug() {
        debugMode = !debugMode;
        System.out.println("Debug Mode is now " + debugMode);
    }

    // Method to call update for all the GameObjects in this world
    public static void update() {
//        for (World wo : worlds) {
//            wo.updateAll();
//        }

        world.updateAll();

        for (GuiElement element : guiElements) {
            element.update();
        }
    }

    // Method to call render for all the GameObjects in this world
    public static void render() {
//        for (World wo : worlds) {
//            wo.renderAll();
//        }
        if (world != null) {
            world.renderAll();
            if (debugMode) {
                world.renderDebug();
            }
        }

        for (GuiElement element : guiElements) {
            element.render();
        }
    }

    // Method to set a new world during the Runtime
    public static void setWorld(World wo) {
        world = wo;
    }

    // Method to add GUI elements to the list
    public static void addGUI(GuiElement gui) {
        guiElements.add(gui);
    }
}
