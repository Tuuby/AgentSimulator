package de.Tuuby.AgentSimulator.guis;

import de.Tuuby.AgentSimulator.graphics.Graphics;

import java.util.ArrayList;

public class Graph extends GuiElement{

    int entryPoint;
    boolean autoMax;
    private ArrayList<Integer> values;
    int maxValue;

    public Graph(int x, int y, int width, int height, int entryPoint, boolean autoMax) {
        super(x, y, width, height);
        this.entryPoint = entryPoint;
        this.autoMax = autoMax;
        values = new ArrayList<Integer>(width);
    }

    public void addValue(int value) {
        entryPoint++;
        values.add(entryPoint, value);
        update();
    }

    public void update() {
        if (values.isEmpty())
            maxValue = 0;
        else {
            if (autoMax) {
                maxValue = values.get(0);
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) > maxValue)
                        maxValue = values.get(i);
                }
            } else
                maxValue = 50;
        }
    }

    @Override
    public void render() {
        for (int i = entryPoint; i < values.size() + entryPoint; i++) {
            //Graphics.fillRect(getX() - (getWidth() / 2) + (i - entryPoint), getY() + maxValue - values[entryPoint % values.size()], );
        }
    }
}
