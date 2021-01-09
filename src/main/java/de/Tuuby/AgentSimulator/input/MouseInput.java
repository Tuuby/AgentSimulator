package de.Tuuby.AgentSimulator.input;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import de.Tuuby.AgentSimulator.graphics.Renderer;

public class MouseInput implements MouseListener {

    private static int x = 0;
    private static int y = 0;

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static float getWorldX() {
        return (Renderer.unitsWide / Renderer.getWindowWidth() * x - Renderer.unitsWide / 2);
    }

    public static float getWorldY() {
        return (Renderer.unitsTall / Renderer.getWindowHeight() * y - Renderer.unitsTall / 2);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        x = mouseEvent.getX();
        y = mouseEvent.getY();
    }

    public void mouseDragged(MouseEvent mouseEvent) {

    }

    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
