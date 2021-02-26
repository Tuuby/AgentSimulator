package de.Tuuby.AgentSimulator.logging;

import java.util.Dictionary;
import java.util.Hashtable;

public class LoggingHandler {

    private static Dictionary<Integer, ILogger> agentStatLogs = new Hashtable<Integer, ILogger>();
    private static Dictionary<Integer, ILogger> agentActionLog = new Hashtable<Integer, ILogger>();
    private static Dictionary<Integer, ILogger> worldLogger = new Hashtable<Integer, ILogger>();

    public static void addAgent(int agentId) {
        agentStatLogs.put(agentId, new AgentLogger());
        agentActionLog.put(agentId, new AgentLogger());
    }

    public static void addWorld(int worldId, ILogger logger) {
        worldLogger.put(worldId, logger);
    }
}
