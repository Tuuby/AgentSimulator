package world;

import java.util.concurrent.ConcurrentLinkedQueue;

// TODO: Call updateAll for all Worlds
// Prototype class that represents the world and all the GameObjects on it
public class WorldUpdater {

    // List of GameObjects that works well for multithreading
    private static ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<GameObject>();

    // Method to call update for all the GameObjects in this world
    public static void update() {
        for (GameObject go : gameObjects) {
            go.update(0);
        }
    }

    // Method to call render for all the GameObjects in this world
    public static void render() {
        for (GameObject go : gameObjects) {
            go.render();
        }
    }

    // Method to add a new GameObject to the list during the Runtime
    public static void addObject(GameObject go) {
        gameObjects.offer(go);
    }
}
