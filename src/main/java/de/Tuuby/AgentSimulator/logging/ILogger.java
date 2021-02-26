package de.Tuuby.AgentSimulator.logging;

public interface ILogger {
    void log(String data, int worldAge);
    String get(int worldAge);
    void writeToFile();
    void writeToConsole();
    //void writeToWindow(int lines);
}
