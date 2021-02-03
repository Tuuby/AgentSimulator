package de.Tuuby.AgentSimulator.guis;

import java.util.LinkedList;
import java.util.List;

public class WindowManager {

    private static List<GuiElement> elements;

    public static void init() {
        elements = new LinkedList<GuiElement>();
    }

    public static boolean register(GuiElement element) {
        return elements.add(element);
    }

    public static boolean unregister(GuiElement element) {
        return elements.remove(element);
    }

    public static GuiElement targetElement(int x, int y) {
        for (GuiElement target : elements) {
            if (x >= target.getX() - target.getWidth() / 2 && x <= target.getX() + target.getWidth() / 2 &&
                y >= target.getY() - target.getHeight() / 2 && y <= target.getY() + target.getHeight() / 2)
                return target;
        }
        return null;
    }


}
