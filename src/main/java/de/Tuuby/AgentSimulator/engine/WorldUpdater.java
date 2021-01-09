package de.Tuuby.AgentSimulator.engine;

import de.Tuuby.AgentSimulator.world.World;

import java.util.Vector;

// Prototype class that represents the de.Tuuby.AgentSimulator.world and all the GameObjects on it
public class WorldUpdater {

    // List of GameObjects that works well for multithreading
    private static Vector<World> worlds = new Vector<World>();

    // Method to call update for all the GameObjects in this de.Tuuby.AgentSimulator.world
    public static void update() {
        for (World wo : worlds) {
            wo.updateAll();
        }
    }

    // Method to call render for all the GameObjects in this de.Tuuby.AgentSimulator.world
    public static void render() {
        for (World wo : worlds) {
            wo.renderAll();
        }
    }

    // Method to add a new GameObject to the list during the Runtime
    public static void addWorld(World wo) {
        worlds.add(wo);
    }
}
