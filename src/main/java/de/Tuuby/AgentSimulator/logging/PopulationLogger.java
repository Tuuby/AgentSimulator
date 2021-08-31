package de.Tuuby.AgentSimulator.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PopulationLogger implements ILogger {

    private final long worldId;
    private final ArrayList<LogEntry> logEntries;

    public PopulationLogger(long worldId) {
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
        File logFile =  new File(LoggingHandler.folderName, "Population_World " + worldId + ".txt");
        try {
            if (logFile.createNewFile()) {
                FileWriter writer = new FileWriter(logFile);
                for (LogEntry logEntry : logEntries) {
                    writer.write("WorldAge " + logEntry.getKey() + ": " + logEntry.getValue() + "\n");
                }
                writer.close();
            } else {
                System.out.println("Couldn't create file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToConsole() {

    }
}
