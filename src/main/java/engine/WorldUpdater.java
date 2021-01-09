package engine;

import world.World;

import java.util.Vector;

// Prototype class that represents the world and all the GameObjects on it
public class WorldUpdater {

    // List of GameObjects that works well for multithreading
    private static Vector<World> worlds = new Vector<World>();

    // Method to call update for all the GameObjects in this world
    public static void update() {
        for (World wo : worlds) {
            wo.updateAll();
        }
    }

    // Method to call render for all the GameObjects in this world
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
