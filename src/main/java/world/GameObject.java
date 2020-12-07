package world;

import graphics.Animation;

// Interface for all GameObjects
public abstract class GameObject {

    // Coordinates for the in world position
    public float x = 0;
    public float y = 0;

    // Dimensions for the in world size
    public float width = 1;
    public float height = 1;

    // Orientation of the GameObject in degrees; mathematically positive direction
    public float rotation = 0;

    // List of available Animations and index of the current Animation
    public Animation[] animations = null;
    public int currentAnimation = 0;

    // Method to update the GameObject each update cycle
    public abstract void update();

    // Method to draw the GameObject each render cycle
    public abstract void render();
}
