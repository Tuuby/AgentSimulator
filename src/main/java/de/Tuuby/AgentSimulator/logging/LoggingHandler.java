package de.Tuuby.AgentSimulator.logging;

import de.Tuuby.AgentSimulator.world.Agent;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class LoggingHandler {

    private static HashMap<Long, ILogger> agentStatLogs = new HashMap<Long, ILogger>();
    private static HashMap<Long, ILogger> agentActionLog = new HashMap<Long, ILogger>();
    private static HashMap<Long, ILogger> worldLogger = new HashMap<Long, ILogger>();

    public static void addAgent(long agentId) {
        agentStatLogs.put(agentId, new AgentLogger("Stats", agentId));
        agentActionLog.put(agentId, new AgentLogger("Actions", agentId));
    }

    public static void addWorld(long worldId, ILogger logger) {
        worldLogger.put(worldId, logger);
    }

    public static void logAgentStat(long agentId, String stat, long worldAge) {
        agentStatLogs.get(agentId).log(stat, worldAge);
    }

    public static void logAgentAction(long agentId, String info, long worldAge) {
        agentActionLog.get(agentId).log(info, worldAge);
    }

    public static void logWorldEvent(long worldId, String info, long worldAge) {
        worldLogger.get(worldId).log(info, worldAge);
    }

    public static void agentDeath(Agent agent, String reason) {
        long agentId = agent.getUniqueID();

        AgentLogger logger =  (AgentLogger) agentStatLogs.get(agentId);
        logger.death();
        logger = (AgentLogger) agentActionLog.get(agentId);
        logger.death();

        agentStatLogs.remove(agentId);
        agentActionLog.remove(agentId);
        logWorldEvent(0, "Agent " + agentId + ": died from " + reason, agent.getWorld().getTime());
    }

    public static void saveAndExit() {
        for (ILogger logger : agentStatLogs.values()) {
            logger.writeToFile();
        }

        for (ILogger logger : agentActionLog.values()) {
            logger.writeToFile();
        }

        for (ILogger logger : worldLogger.values()) {
            logger.writeToFile();
        }
    }
}
