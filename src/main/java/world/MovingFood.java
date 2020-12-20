package world;

public class MovingFood extends MovingItem {

    public MovingFood() {
        super(0, 0, null);
    }

    public boolean extendsTo(float dx, float dy) {
        return false;
    }

    public void update(long time) {

    }

    public void render() {

    }
}
