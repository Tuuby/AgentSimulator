package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Graphics;

public class ProgressBar extends GuiElement{

    private int maxValue;
    private int currentValue;
    private int percentage;

    private float red;
    private float green;
    private float blue;
    private float alpha;

    public ProgressBar(int x, int y, int width, int height, int maxValue) {
        this(x, y, width, height);
        this.maxValue = maxValue;
        this.currentValue = 0;
        this.percentage = 0;
    }

    public ProgressBar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        update();
    }

    public void setColor(float r, float g, float b, float a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public void update() {
        percentage = (int)((float)currentValue / (float) maxValue * 100);
    }

    @Override
    public void render() {
        Graphics.setColor(0, 0, 0, 1);
        Graphics.fillRect(x + width / 2, y + height / 2, width, height);

        int barWidth = (int)(width / 100.0 * percentage);

        Graphics.setColor(red, green, blue, alpha);
        Graphics.fillRect(x + barWidth / 2, y + height / 2, barWidth, height);
    }
}
