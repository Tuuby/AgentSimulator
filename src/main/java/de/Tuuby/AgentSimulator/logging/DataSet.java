package de.Tuuby.AgentSimulator.logging;

import de.Tuuby.AgentSimulator.world.Agent;
import de.Tuuby.AgentSimulator.world.enums.AgentSpecial;
import de.Tuuby.AgentSimulator.world.enums.AgentStates;

public class DataSet {

    // Data fields for the swing TabbedPane
    // "World"
    public long worldAge;
    public int numberHills;
    public int numberFood;
    public double avgNutriValue;
    public int numberHerbivore;
    public int numberAgent;

    // "Population"
    public int numberParalysedHerbivores;
    public int numberDeadHerbivores;
    public int numberHerbivoresAlltime;

    public double avgHerbEnergy;
    public int maxHerbEnergy;
    public double avgHerbFood;
    public int maxHerbFood;
    public double avgHerbSpeed;
    public int maxHerbSpeed;
    public double avgHerbVisionRange;
    public int maxHerbVisionRange;

    public int numberFemaleAgent;
    public int numberMaleAgent;
    public int numberAttacker;
    public int numberParalyzer;
    public int numberLeader;
    public int numberAgentsInGroup;
    public int numberAgentAlltime;

    public double avgGeneration;
    public int maxGeneration;
    public double avgAge;
    public int maxAge;
    public double avgAgentSpeed;
    public int maxAgentSpeed;
    public double avgAgentVisionRange;
    public int maxAgentVisionRange;
    public double avgAgentStamina;
    public int maxAgentStamina;
    public double avgAgentMaxStamina;
    public int maxAgentMaxStamina;
    public double avgAgentFood;
    public int maxAgentFood;
    public double avgAgentFoodCapacity;
    public int maxAgentFoodCapacity;
    public double avgPortalUsage;
    public int maxPortalUsage;
    public int portalUsageAlltime;

    // "Groups"
    public int numberGroups;
    public int numberGroupsAlltime;
    public int numberGroupsDissolved;

    public int maxNumberGroupMembers;
    public double avgNumberGroupMembers;
    public int maxNumberActiveGroupMembers;
    public double avgNumberActiveGroupMembers;

    public int maxGroupSuccess;
    public double avgGroupSuccess;

    public int numberAttackerBestGroup;
    public int numberParalyzerBestGroup;
    public double avgNumberAttacker;
    public double avgNumberParalyzer;

    // "Detailed"
    public Agent selectedAgent;
}
