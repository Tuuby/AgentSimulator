package de.Tuuby.AgentSimulator.logging;

import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorldLogger implements ILogger {

    private long worldId;
    private ArrayList<Pair<Long, String>> logEntries;

    public WorldLogger(long worldId) {
        this.worldId = worldId;
        logEntries = new ArrayList<Pair<Long, String>>();
    }

    public void log(String data, long worldAge) {
        logEntries.add(new Pair<Long, String>(worldAge, data));
    }

    public String get(long worldAge) {
        for (Pair<Long, String> logEntry : logEntries) {
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
            for (Pair<Long, String> logEntry : logEntries) {
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
