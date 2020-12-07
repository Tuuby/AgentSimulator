package world;

import graphics.Animation;

// Interface for all GameObjects
public interface GameObject {

    // Coordinates for the in world position
    float x = 0;
    float y = 0;

    // Dimensions for the in world size
    float width = 1;
    float height = 1;

    // Orientation of the GameObject in degrees; mathematically positive direction
    float rotation = 0;

    // List of available Animations and index of the current Animation
    Animation[] animations = null;
    int currentAnimation = 0;

    // Method to update the GameObject each update cycle
    void update();

    // Method to draw the GameObject each render cycle
    void render();
}
