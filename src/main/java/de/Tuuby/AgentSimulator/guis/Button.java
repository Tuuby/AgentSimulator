package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Graphics;

public class Button extends GuiElement{

    private String text;
    private float[] textColor;

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        textColor = new float[4];
    }

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
        textColor = new float[4];
    }

    public void setTextColor(float r, float g, float b, float a) {
        textColor[0] = r;
        textColor[1] = g;
        textColor[2] = b;
        textColor[3] = a;
    }

    @Override
    public void render() {
        Graphics.setRotation(0);
        if (texture != null) {
            texture.play();
            Graphics.drawImage(texture.getImage(), getX(), getY(), getWidth(), getHeight());
        } else {
            Graphics.setColor(1, 1, 1, 1);
            Graphics.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        Graphics.setColor(textColor[0], textColor[1], textColor[2], textColor[3]);
        Graphics.drawText(text, getX(), getY(), getWidth(), getHeight());
    }

    public void update() {
        // Nothing to update yet
    }
}
