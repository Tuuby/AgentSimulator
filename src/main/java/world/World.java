package world;

import java.util.concurrent.ConcurrentLinkedQueue;

public class World {

    private static ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<GameObject>();

    public static void update() {
        for (GameObject go : gameObjects) {
            go.update();
        }
    }

    public static void render() {
        for (GameObject go : gameObjects) {
            go.render();
        }
    }

    public static void addObject(GameObject go) {
        gameObjects.offer(go);
    }
}
