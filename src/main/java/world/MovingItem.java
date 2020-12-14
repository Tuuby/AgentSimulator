package world;

import graphics.Renderer;

// Class to represent a moving GameObject
// TODO: maybe change name to gameobject
public abstract class MovingItem extends GameObject {

    // Factor to increase or decrease moved units per time unit
    protected float speed;

    // Constructor for the new MovingItem
    public MovingItem(float x, float y, World w, float speed) {
        super(x, y, w);
        this.speed = speed;
    }

    // Constructor for the MovingItem class without speed as parameter
    public MovingItem(float x, float y, World w) {
        super(x, y, w);

        // TODO: update standard speed once the logic stands
        speed = 1;
    }

    // Getter and setter for speed
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // Method to update the position of the GameObject for the desired coordinates depending on the time passed
    public float updatePosition(float dx, float dy, float dt) {
        if (speed > 0 && (dx != 0 || dy != 0)) {
            // TODO: rethink world size after game world has been written
            float sizeX = Renderer.unitsWide;
            float sizeY = Renderer.unitsTall;

            if (dx > sizeX / 2)
                dx -= sizeX;
            else if (dx < -sizeX / 2)
                dx += sizeX;

            if (dy > sizeY / 2)
                dy -= sizeY;
            else if (dy < -sizeY / 2)
                dy += sizeY;

            // TODO: figure out how it works and adjust to world coordinates
            double distance = Math.sqrt(dx * dx + dy * dy);
            float wx = (dx * speed * dt) / 100 / (float)distance;
            float wy = (dy * speed * dt) / 100 / (float)distance;

            if (wx == 0 && wy == 0) {
                return 0;
            } else {
                if (Math.abs(wx) > Math.abs(dx))
                    wx = dx-1;
                if (Math.abs(wy) > Math.abs(dy))
                    wy = dy-1;
            }

            // TODO: uncomment if world is rewritten
            if (/*world.moveItemBy(this, wx, wy)*/true)
                return (float)Math.sqrt(wx * wx + wy * wy);
            else
                return 0;
        } else
            return 0;
    }
}
