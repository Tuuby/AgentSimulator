package world;

import engine.JMTRemnants.Performative;

// Interface for the Agents
public interface IAgent {
    void setName(String name);
    String getName();
    void performKQML(IAgent sender, Performative performative);
}
