package UI;

import de.Tuuby.AgentSimulator.graphics.Graphics;
import de.Tuuby.AgentSimulator.guis.GuiElement;

public class LineObject extends GuiElement {
    public LineObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update() {
        // Nix hier zu tun
    }

    @Override
    public void render() {
        Graphics.setColor(1, 0, 0, 1);
        Graphics.drawLine(getX(), getY(), getWidth(), getHeight(), 5);
    }
}
