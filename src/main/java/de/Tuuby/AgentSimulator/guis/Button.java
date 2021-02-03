package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.EventListener;
import de.Tuuby.AgentSimulator.graphics.Graphics;
import de.Tuuby.AgentSimulator.graphics.Renderer;

import java.awt.geom.Rectangle2D;

public class Button extends GuiElement{

    private String text;
    private float[] textColor;
    private GUIMouseListener listener;

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        textColor = new float[4];
    }

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
        textColor = new float[4];
        textColor[3] = 1;
    }

    public void setMouseListener(GUIMouseListener listener) {
        this.listener = listener;
    }

    public void onClick() {
        if (listener != null)
            listener.onMouseButtonClick();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(float r, float g, float b, float a) {
        textColor[0] = r;
        textColor[1] = g;
        textColor[2] = b;
        textColor[3] = a;
    }

    @Override
    public void render() {
        Rectangle2D textSize = EventListener.renderer.getBounds(text);

        Graphics.setRotation(0);
        if (texture != null) {
            texture.play();
            Graphics.drawImage(texture.getImage(), getX(), getY(), getWidth(), getHeight());
        } else {
            Graphics.setColor(1, 1, 1, 1);
            Graphics.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        Graphics.setColor(textColor[0], textColor[1], textColor[2], textColor[3]);
        Graphics.drawText(text, getX() - (int)textSize.getWidth() / 2,
                (int) (Renderer.unitsTall - (getY() + (int)textSize.getHeight() / 3)),
                (int)Renderer.unitsWide, (int)Renderer.unitsTall);
    }

    public void update() {
        // Nothing to update yet
    }
}
