package de.Tuuby.AgentSimulator.logging;

public class LogEntry {
    private final long key;
    private final String value;

    public LogEntry(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public long getKey() {
        return key;
    }
}
