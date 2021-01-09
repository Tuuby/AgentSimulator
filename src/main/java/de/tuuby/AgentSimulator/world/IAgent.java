package de.tuuby.AgentSimulator.world;

import de.tuuby.AgentSimulator.engine.JMTRemnants.Performative;

// Interface for the Agents
public interface IAgent {
    void setName(String name);
    String getName();
    void performKQML(IAgent sender, Performative performative);
}
