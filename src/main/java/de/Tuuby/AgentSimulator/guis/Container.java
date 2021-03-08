package de.Tuuby.AgentSimulator.guis;

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
        super.render();
        for (GuiElement element : contents) {
            element.render();
        }
    }

    public void update() {

    }
}
