package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.EventListener;
import de.Tuuby.AgentSimulator.graphics.Graphics;
import de.Tuuby.AgentSimulator.graphics.Renderer;

import java.awt.geom.Rectangle2D;

public class TextElement extends GuiElement{

    private String text;

    public TextElement(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        Rectangle2D textSize = EventListener.renderer.getBounds(text);
        Graphics.setColor(0, 0, 0, 1);
        Graphics.drawText(text, x, (int) (Renderer.unitsTall - (getY() + textSize.getHeight() / 2.f)), (int) Renderer.unitsWide, (int) Renderer.unitsTall);
    }

    public void update() {
        //nothing to update
    }
}
