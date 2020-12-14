package gameObjects;

import graphics.Animation;
import graphics.Graphics;
import resource.ImageResource;
import world.GameObject;

public class TestGO extends GameObject {

    private float width;
    private float height;

    public TestGO(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        animations = new Animation[1];
        animations[0] = new Animation();
        animations[0].frames = new ImageResource[1];
        animations[0].frames[0] = new ImageResource("/caroldanvers.png");
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean extendsTo(float dx, float dy) {
        return false;
    }

    public void moveTo(int xabs, int yabs) {
    }

    public void update() {
        rotation++;
    }

    public void render() {
        animations[currentAnimation].play();
        Graphics.setRotation(-rotation);
        Graphics.drawImage(animations[currentAnimation].getImage(), x, y, width, height);
        Graphics.setRotation(0);
    }
}
