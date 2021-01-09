package de.tuuby.AgentSimulator.world;

import de.tuuby.AgentSimulator.graphics.Animation;
import de.tuuby.AgentSimulator.graphics.Graphics;
import de.tuuby.AgentSimulator.resource.ImageResource;

public class Food extends GameObject {

    public static int ENERGY_MIN = 12000;
    public static int ENERGY_MAX = 15000;

    protected int energy;

    public Food(int initX, int initY, World w) {
        super(initX, initY, w);
        energy = (int)(Math.random() * (ENERGY_MAX - ENERGY_MIN)) + ENERGY_MIN;
        animations = new Animation[1];
        animations[0] = new Animation();
        animations[0].frames = new ImageResource[1];
        animations[0].frames[0] = new ImageResource("/GameObjects/Food.png");
    }

    public Food(int initX, int initY, World w, int initEnergy) {
        super(initX, initY, w);
        energy = initEnergy;
    }

    public String getType() {
        return "food";
    }

    public boolean extendsTo(int dx, int dy) {
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
        animations[currentAnimation].play();
        Graphics.setRotation(0);
        Graphics.drawImage(animations[currentAnimation].getImage(), x, y, 20, 20);
    }
}
