package de.tuuby.AgentSimulator.world;

// Interface for food gameobjects to include energy functions
public interface IFood {
    int getEnergy();
    void removeEnergy(int de);
}
