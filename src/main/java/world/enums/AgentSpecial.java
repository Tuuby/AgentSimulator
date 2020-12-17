package world.enums;

public enum AgentSpecial {
    ATTACKER(0), PARALYZER(1), LEADER(2), SCAVENGER(3);

    private final int specialNumber;

    AgentSpecial(int specialNumber) {
        this.specialNumber = specialNumber;
    }

    public int getSpecialNumber() {
        return specialNumber;
    }

    @Override
    public String toString() {
        return "AgentSpecial{" +
                "specialNumber=" + specialNumber +
                '}';
    }
}
