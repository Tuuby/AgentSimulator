package de.Tuuby.AgentSimulator.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorldLogger implements ILogger {

    private long worldId;
    private ArrayList<LogEntry> logEntries;

    public WorldLogger(long worldId) {
        this.worldId = worldId;
        logEntries = new ArrayList<LogEntry>();
    }

    public void log(String data, long worldAge) {
        logEntries.add(new LogEntry(worldAge, data));
    }

    public String get(long worldAge) {
        for (LogEntry logEntry : logEntries) {
            if (logEntry.getKey() == worldAge) {
                return logEntry.getValue();
            }
        }
        return "";
    }

    public void writeToFile() {
        File logFile = new File(LoggingHandler.folderName, "World " + worldId + ".txt");
        try {
            logFile.createNewFile();

            FileWriter writer = new FileWriter(logFile);
            for (LogEntry logEntry : logEntries) {
                writer.write("WorldAge " + logEntry.getKey() + ": " + logEntry.getValue() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToConsole() {

    }
}
