package gameObjects;

import graphics.Animation;
import graphics.Graphics;
import resource.ImageResource;
import world.GameObject;

public class TestGO extends GameObject {

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
