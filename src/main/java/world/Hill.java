package world;

public class Hill extends GameObject {

    private float radius;

    public Hill(World w, int x, int y, float r) {
        super(x, y, w);
        radius = r;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Hill{" +
                "radius=" + radius +
                ", x=" + x +
                ", y=" + y +
                ", world=" + world +
                '}';
    }

    public boolean extendsTo(int dx, int dy) {
        return (dx * dx + dy * dy < radius * radius);
    }

    public void moveTo(int xabs, int yabs) {

    }

    public void update() {

    }

    public void render() {

    }
}
