package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.graphics.Graphics;

// Class to represent to hills on the de.Tuuby.AgentSimulator.world than block pathing and vision I think
public class Hill extends GameObject {

    // Radius of the hill
    private float radius;

    // Constructor to make a new GameObject with a radius
    public Hill(int x, int y, World w, float r) {
        super(x, y, w);
        radius = r;
    }

    // Method to check the radius
    public float getRadius() {
        return radius;
    }

    // toString method to output the hill as a String
    @Override
    public String toString() {
        return "Hill{" +
                "radius=" + radius +
                ", x=" + x +
                ", y=" + y +
                ", de.Tuuby.AgentSimulator.world=" + world +
                '}';
    }

    // Inherited method to check if the hill reaches to a specific point
    public boolean extendsTo(int dx, int dy) {
        return (dx * dx + dy * dy < radius * radius);
    }

    // Method to update the hill every gameupdate tick
    public void update(long time) {
        // Probably not much to do here, as a hill just hangs around
    }

    // Method to render the hill every frame
    public void render() {
        Graphics.setColor(0.47f, 0.41f, 0.25f, 1);
        Graphics.fillCircle(x, y, radius);
    }

    public void renderDebug() {

    }
}
