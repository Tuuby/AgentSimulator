package de.Tuuby.AgentSimulator.engine;

import de.Tuuby.AgentSimulator.guis.GuiElement;
import de.Tuuby.AgentSimulator.world.World;

import javax.swing.*;
import java.util.Vector;

// Prototype class that represents the de.Tuuby.AgentSimulator.world and all the GameObjects on it
public class WorldUpdater {

    // List of worlds that need to be updated
    private static Vector<World> worlds = new Vector<World>();
    // List of all GUI Elements
    private static Vector<GuiElement> guiElements = new Vector<GuiElement>();

    // Method to call update for all the GameObjects in this world
    public static void update() {
        for (World wo : worlds) {
            wo.updateAll();
        }

        for (GuiElement element : guiElements) {
            element.update();
        }
    }

    // Method to call render for all the GameObjects in this world
    public static void render() {
        for (World wo : worlds) {
            wo.renderAll();
        }

        for (GuiElement element : guiElements) {
            element.render();
        }
    }

    // Method to add a new GameObject to the list during the Runtime
    public static void addWorld(World wo) {
        worlds.add(wo);
    }

    public static void addGUI(GuiElement gui) {
        guiElements.add(gui);
    }
}
