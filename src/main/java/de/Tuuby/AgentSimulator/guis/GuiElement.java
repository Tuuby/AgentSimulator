package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Animation;
import de.Tuuby.AgentSimulator.graphics.Graphics;

public abstract class GuiElement {

    protected Animation texture;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void render() {
        Graphics.setRotation(0);
        if (texture != null) {
            texture.play();
            Graphics.drawImage(texture.getImage(), x, y, width, height);
        } else {
            Graphics.setColor(1, 1, 1, 1);
            Graphics.fillRect(x, y, width, height);
        }
    }

    public abstract void update();
}
