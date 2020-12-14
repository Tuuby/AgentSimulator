package world;

import com.jogamp.nativewindow.OffscreenLayerOption;
import graphics.Renderer;

public abstract class MovingItem extends GameObject {
    protected float speed;

    public MovingItem(float x, float y, World w, float speed) {
        super(x, y, w);
        this.speed = speed;
    }

    public MovingItem(float x, float y, World w) {
        super(x, y, w);

        // TODO: update standard speed once the logic stands
        speed = 1;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

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
