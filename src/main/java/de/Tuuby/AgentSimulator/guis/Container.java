package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Graphics;

import java.util.ArrayList;

public class Container extends GuiElement{

    protected ArrayList<GuiElement> contents;

    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
        contents = new ArrayList<GuiElement>();
    }

    private boolean isInBounds(GuiElement element) {
        return element.getX() > getX() - (getWidth() / 2) &&
                element.getX() < getX() + (getWidth() / 2) &&
                element.getY() > getY() - (getHeight() / 2) &&
                element.getY() < getY() + (getHeight() / 2);
    }

    private boolean isWithinSize(GuiElement element) {
        return element.getWidth() < getWidth() &&
                element.getHeight() < getHeight();
    }

    public void addElement(GuiElement element) {
        if (isInBounds(element) && isWithinSize(element)) {
            contents.add(element);
        } else {
            throw new IndexOutOfBoundsException("The element doesn't fit into the container");
        }
    }

    public GuiElement getElement(int index) {
        return contents.get(index);
    }

    public void removeElement(GuiElement element) {
        contents.remove(element);
    }

    @Override
    public void render() {
        Graphics.setColor(0.6f, 0.67f, 0.57f, 1);
        Graphics.fillRect(x, y, width, height);

        Graphics.setColor(1, 1, 1, 1);
        Graphics.drawLine(getX() - getWidth() / 2.f, getY() - getHeight() / 2.f, getX() + getWidth() / 2.f, getY() - getHeight() / 2.f, 1);
        Graphics.drawLine(getX() - getWidth() / 2.f, getY() - getHeight() / 2.f, getX() - getWidth() / 2.f, getY() + getHeight() / 2.f, 1);
        Graphics.drawLine(getX() - getWidth() / 2.f, getY() + getHeight() / 2.f, getX() + getWidth() / 2.f, getY() + getHeight() / 2.f, 1);
        Graphics.drawLine(getX() + getWidth() / 2.f, getY() - getHeight() / 2.f, getX() + getWidth() / 2.f, getY() + getHeight() / 2.f, 1);

        for (GuiElement element : contents) {
            element.render();
        }
    }

    public void update() {
        // nothing to update
    }
}
