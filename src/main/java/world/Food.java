package world;

public class Food extends GameObject {

    public static int ENERGY_MIN = 12000;
    public static int ENERGY_MAX = 15000;

    protected int energy;

    public Food(int initX, int initY, World w) {
        super(initX, initY, w);
        energy = (int)(Math.random() * (ENERGY_MAX - ENERGY_MIN)) + ENERGY_MIN;
    }

    public Food(int initX, int initY, World w, int initEnergy) {
        super(initX, initY, w);
        energy = initEnergy;
    }

    public String getType() {
        return "food";
    }

    public boolean extendsTo(float dx, float dy) {
        return false;
    }

    public void update(long time) {
        // Nothing to update yet; maybe let it grow in the future
    }

    public int getEnergy() {
        return energy;
    }

    public void removeEnergy(int de) {
        energy -= de;
        if (energy <= 0)
            world.removeObject(this);
    }

    public String getStat() {
        return "Nahrungselement " + toString() + "\nx: "+ x + "  y: " + y + "\nEnergie: " + energy;
    }

    public void render() {

    }
}
