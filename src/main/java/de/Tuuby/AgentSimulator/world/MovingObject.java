package de.Tuuby.AgentSimulator.world;

// Class to represent a moving GameObject
public abstract class MovingObject extends GameObject {

    // Factor to increase or decrease moved units per time unit
    protected int speed;

    // Constructor for the new MovingItem
    public MovingObject(int x, int y, World w, int speed) {
        super(x, y, w);
        this.speed = speed;
    }

    // Constructor for the MovingItem class without speed as parameter
    public MovingObject(int x, int y, World w) {
        super(x, y, w);
        speed = 1;
    }

    // Getter and setter for speed
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // Method to update the position of the GameObject for the desired coordinates depending on the time passed
    public int updatePosition(int dx, int dy, int dt) {
        if (speed > 0 && (dx != 0 || dy != 0)) {
            int sizeX = world.getWidth();
            int sizeY = world.getHeight();

            if (dx > sizeX / 2)
                dx -= sizeX;
            else if (dx < -sizeX / 2)
                dx += sizeX;

            if (dy > sizeY / 2)
                dy -= sizeY;
            else if (dy < -sizeY / 2)
                dy += sizeY;

            int distance = (int) Math.sqrt(dx * dx + dy * dy);
            int wx = (dx * speed * dt) / 100 / distance;
            int wy = (dy * speed * dt) / 100 / distance;

            if (wx == 0 && wy == 0) {
                return 0;
            } else {
                if (Math.abs(wx) > Math.abs(dx))
                    wx = dx-1;
                if (Math.abs(wy) > Math.abs(dy))
                    wy = dy-1;
            }

            if (world.moveObjectBy(this, wx, wy))
                return (int) world.distance(wx, wy);
            else
                return 0;
        } else
            return 0;
    }
}
