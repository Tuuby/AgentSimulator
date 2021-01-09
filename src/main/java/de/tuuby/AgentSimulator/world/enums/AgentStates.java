package de.tuuby.AgentSimulator.world.enums;

public enum AgentStates {
    NONE(0), SLEEPING(1), LOOKING(2), HUNTING(3), EATING(4),
    SEARCHING_FOOD(5), SEARCHING_AGENT(6), REPRODUCING(7),
    REPRODUCING2(8), APPROACHING_PORTAL(9), TRANSPORTING(10);

    private final int stateNumber;

    AgentStates(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    @Override
    public String toString() {
        return "AgentStates{" +
                "stateNumber=" + stateNumber +
                '}';
    }
}
