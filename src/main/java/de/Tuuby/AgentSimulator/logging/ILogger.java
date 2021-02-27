package de.Tuuby.AgentSimulator.logging;

public interface ILogger {
    void log(String data, long worldAge);
    String get(long worldAge);
    void writeToFile();
    void writeToConsole();
    //void writeToWindow(int lines);
}
