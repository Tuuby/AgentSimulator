package de.tuuby.AgentSimulator.world;

import de.tuuby.AgentSimulator.graphics.Animation;

import java.util.Arrays;

// Abstract class for all GameObjects
public abstract class GameObject {

    // Coordinates for the in de.tuuby.AgentSimulator.world position
    protected int x;
    protected int y;

    // The de.tuuby.AgentSimulator.world the GameObject is on, as any GameObject can only be on one de.tuuby.AgentSimulator.world
    protected World world;

    // Dimensions for the in de.tuuby.AgentSimulator.world size; Not ever GameObject has a width and a height
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
    public GameObject(int x, int y, World world) {
        this.x = x;
        this.y = y;
        this.world = world;
    }

    // Getter for the coordinates, and Getter and Setter for the de.tuuby.AgentSimulator.world
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

    // Method to check if the GameObject extends to a specific coordination
    public abstract boolean extendsTo(int dx, int dy);

    public void moveTo(int xabs, int yabs) {
        x = xabs;
        y = yabs;
    }

    // Method to output the GameObject as a String
    @Override
    public String toString() {
        return "GameObject{" +
                "x=" + x +
                ", y=" + y +
                ", de.tuuby.AgentSimulator.world=" + world +
                ", rotation=" + rotation +
                ", animations=" + Arrays.toString(animations) +
                ", currentAnimation=" + currentAnimation +
                '}';
    }

    // Method to update the GameObject each update cycle
    public abstract void update(long time);

    // Method to draw the GameObject each render cycle
    public abstract void render();

    public void thisRemovedFromWorld() {}

    public void gameObjectRemovedFromWorld(GameObject go) {}
}
