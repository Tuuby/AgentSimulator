package de.Tuuby.AgentSimulator.logging;

import javafx.util.Pair;

import java.util.ArrayList;

public class AgentLogger implements ILogger {

    private ArrayList<Pair<Integer, String>> logEntries;

    public AgentLogger() {
        logEntries = new ArrayList<Pair<Integer, String>>();
    }

    public void log(String data, int worldAge) {
        logEntries.add(new Pair<Integer, String>(worldAge, data));
    }

    public String get(int worldAge) {
        for (Pair<Integer, String> logEntry : logEntries) {
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
