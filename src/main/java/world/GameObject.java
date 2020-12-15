package world;

import graphics.Animation;

import java.util.Arrays;

// Abstract class for all GameObjects
public abstract class GameObject {

    // Coordinates for the in world position
    protected float x;
    protected float y;

    // The world the GameObject is on, as any GameObject can only be on one world
    protected WorldUpdater world;

    // Dimensions for the in world size; Not ever GameObject has a width and a height
    //public float width;
    //public float height;

    // Orientation of the GameObject in degrees; mathematically positive direction
    public float rotation = 0;

    // List of available Animations and index of the current Animation
    public Animation[] animations = null;
    public int currentAnimation = 0;

    // Default constructor
    public GameObject() {
        x = 0;
        y = 0;
        world = null;
        rotation = 0;
    }

    // Constructor with the most attributes
    public GameObject(float x, float y, WorldUpdater world) {
        this.x = x;
        this.y = y;
        this.world = world;
    }

    // Getter for the coordinates, and Getter and Setter for the world
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public WorldUpdater getWorld() {
        return world;
    }

    public void setWorld(WorldUpdater world) {
        this.world = world;
    }

    // Method to check if the GameObject extends to a specific coordination
    public abstract boolean extendsTo(float dx, float dy);

    // Method to output the GameObject as a String
    @Override
    public String toString() {
        return "GameObject{" +
                "x=" + x +
                ", y=" + y +
                ", world=" + world +
                ", rotation=" + rotation +
                ", animations=" + Arrays.toString(animations) +
                ", currentAnimation=" + currentAnimation +
                '}';
    }

    // Method to update the GameObject each update cycle
    public abstract void update();

    // Method to draw the GameObject each render cycle
    public abstract void render();

    public abstract void thisRemovedFromWorld();

    public abstract void gameObjectRemovedFromWorld(GameObject go);
}
