package de.Tuuby.AgentSimulator.logging;

import de.Tuuby.AgentSimulator.world.Agent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LoggingHandler {

    static String folderName;
    private static HashMap<Long, ILogger> agentStatLogs = new HashMap<Long, ILogger>();
    private static HashMap<Long, ILogger> worldLoggers = new HashMap<Long, ILogger>();
    private static HashMap<Long, ILogger> populationLoggers = new HashMap<Long, ILogger>();

    public static void init() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");
        String time = dateFormat.format(now);
        folderName = "logs/simulation" + time;
        File folder = new File(folderName);
        folder.mkdirs();
    }

    public static void addAgent(long agentId) {
        agentStatLogs.put(agentId, new AgentLogger("Stats", agentId));
    }

    public static void addWorld(long worldId, ILogger worldLogger, ILogger popLogger) {
        worldLoggers.put(worldId, worldLogger);
        populationLoggers.put(worldId, popLogger);
    }

    public static void logAgentStat(long agentId, String stat, long worldAge) {
        agentStatLogs.get(agentId).log(stat, worldAge);
    }

    public static void logWorldEvent(long worldId, String info, long worldAge) {
        worldLoggers.get(worldId).log(info, worldAge);
    }

    public static void logPopulationStats(long worldId, String data, long worldAge) {
        populationLoggers.get(worldId).log(data, worldAge);
    }

    public static void agentDeath(Agent agent, String reason) {
        long agentId = agent.getUniqueID();

        AgentLogger logger =  (AgentLogger) agentStatLogs.get(agentId);
        logger.death();
        agentStatLogs.remove(agentId);
        logWorldEvent(0, "Agent " + agentId + ": died from " + reason, agent.getWorld().getTime());
    }

    public static void saveAndExit() {
        for (ILogger logger : agentStatLogs.values()) {
            logger.writeToFile();
        }

        for (ILogger logger : worldLoggers.values()) {
            logger.writeToFile();
        }

        for (ILogger logger : populationLoggers.values()) {
            logger.writeToFile();
        }
    }
}
