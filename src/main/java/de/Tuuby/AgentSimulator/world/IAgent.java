package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.engine.Performative;

// Interface for the Agents
public interface IAgent {
    void setName(String name);
    String getName();
    void performKQML(IAgent sender, Performative performative);
}
