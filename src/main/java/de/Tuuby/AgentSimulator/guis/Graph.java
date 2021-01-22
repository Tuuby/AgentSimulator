package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Graphics;
import java.util.ArrayList;

public class Graph extends GuiElement{

    private int entryPoint;
    private final boolean autoMax;
    private final int[] values;
    private int maxValue;
    private final int barWidth;
    private float red;
    private float green;
    private float blue;
    private float alpha;

    public Graph(int x, int y, int width, int height, boolean autoMax, int barWidth) {
        super(x, y, width, height);
        entryPoint = -1;
        this.autoMax = autoMax;
        values = new int[width / barWidth];
        this.barWidth = barWidth;
    }

    public Graph(int x, int y, int width, int height, boolean autoMax) {
        this(x, y, width, height, autoMax, 1);
    }

    public void addValue(int value) {
        if (++entryPoint >= values.length)
            entryPoint = 0;
        values[entryPoint] = value;
        update();
    }

    public void setColor(float r, float g, float b, float a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public void update() {
        if (autoMax) {
            maxValue = values[0];
            for (Integer value : values) {
                if (value > maxValue)
                    maxValue = value;
            }
        } else
            maxValue = 50;
    }

    @Override
    public void render() {
        // Draw the bars
        Graphics.setColor(red, green, blue, alpha);
        for (int i = 0; i < values.length; i++) {
            int barX = (getX() + i * barWidth) + barWidth / 2;
            int barY = getY() + getHeight();
            int arrayPosition = (i + entryPoint) % values.length;
            int barHeight = (int) (values[arrayPosition] / (float)maxValue * getHeight());
            Graphics.drawLine(barX, barY, barX, barY - barHeight, barWidth);
        }

        // Draw the UI elements Borders
        Graphics.setColor(1, 1, 1, 1);
        Graphics.drawLine(getX(), getY(), getX() + getWidth(), getY(), 1);
        Graphics.drawLine(getX(), getY(), getX(), getY() + getHeight(), 1);
        Graphics.drawLine(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight(), 1);
        Graphics.drawLine(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight(), 1);

    }
}
