package de.Tuuby.AgentSimulator.logging;

import javafx.util.Pair;

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

    }

    public void writeToConsole() {

    }
}
