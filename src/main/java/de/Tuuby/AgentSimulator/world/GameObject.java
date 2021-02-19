package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.graphics.Animation;

// Abstract class for all GameObjects
public abstract class GameObject {

    protected static long nextID = 0;
    protected long uniqueID;

    // Coordinates for the in world position
    protected int x;
    protected int y;

    // The world the GameObject is on, as any GameObject can only be on one world
    protected World world;

    // Orientation of the GameObject in degrees; mathematically positive direction
    public float rotation = 0;

    // List of available Animations and index of the current Animation
    public Animation[] animations = null;
    public int currentAnimation = 0;

    // Default constructor
    public GameObject() {
        this(0, 0, null);
    }

    // Constructor with the most attributes
    public GameObject(int x, int y, World world) {
        this.x = x;
        this.y = y;
        this.world = world;
        rotation = 0;
        uniqueID = nextID++;
    }

    // Getter for the coordinates, and Getter and Setter for the world
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public long getUniqueID() {
        return uniqueID;
    }

    // Method to check if the GameObject extends to a specific coordination
    public abstract boolean extendsTo(int dx, int dy);

    public void moveTo(int xabs, int yabs) {
        x = xabs;
        y = yabs;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "uniqueID=" + uniqueID +
                ", x=" + x +
                ", y=" + y +
                ", world=" + world +
                '}';
    }

    // Method to update the GameObject each update cycle
    public abstract void update(long time);

    // Method to draw the GameObject each render cycle
    public abstract void render();

    public abstract void renderDebug();

    public void thisRemovedFromWorld() {}

    public void gameObjectRemovedFromWorld(GameObject go) {}
}
