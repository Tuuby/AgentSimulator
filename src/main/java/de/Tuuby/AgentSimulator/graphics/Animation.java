package de.Tuuby.AgentSimulator.graphics;

import de.Tuuby.AgentSimulator.resource.ImageResource;

// Class to hold a list of Images and play it as an Animation
public class Animation {

    // List of images that make up the Animation
    public ImageResource[] frames;

    // Counter to track which image is the current Frame
    private int currentFrame = 0;

    // Variables to control how often the frame gets switched
    public int fps = 8;
    private long lastFrameTime = 0;

    // Variable to determine whether the Animation ist looped or stopped
    public boolean loop = true;

    // Calculate which image is displayed at the current FrameTime
    public void play() {
        long currentTime = System.nanoTime();

        if (currentTime > lastFrameTime + 1000000000 / fps) {
            currentFrame++;

            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame--;
                }
            }

            lastFrameTime = currentTime;
        }
    }

    // Return the current image for the Animation
    public ImageResource getImage() {
        return frames[currentFrame];
    }
}
