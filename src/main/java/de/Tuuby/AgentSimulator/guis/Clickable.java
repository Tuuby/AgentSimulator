package de.Tuuby.AgentSimulator.guis;

public interface Clickable {
    boolean inBounds(int x, int y);
    void onClick();
}
