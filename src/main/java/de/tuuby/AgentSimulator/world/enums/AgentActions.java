package de.tuuby.AgentSimulator.world.enums;

public enum AgentActions {
    NONE(0), SLEEP(1), EAT(2), REPRODUCE(3), LOOK(4),
    WALK(5), RECRUIT(6), HUNT(7), TRANSPORT(8);

    private final int actionNumber;

    AgentActions(int actionNumber) {
        this.actionNumber = actionNumber;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    @Override
    public String toString() {
        return "AgentActions{" +
                "actionNumber=" + actionNumber +
                '}';
    }
}
