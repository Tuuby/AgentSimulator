package world;

import engine.JMTRemnants.Performative;
import world.enums.AgentSpecial;

import java.util.*;

public class AgentKomm {

    public static int cGroups;
    public static int cGroupsDissolved;
    public static int cMembers;
    public static int cMaxMembers;

    public int groupsJoined;
    public int groupsLeft;
    public int groupsDissolved;
    public int cancellations;

    protected Agent leader;
    protected List<GameObject> partners;

    private Agent thisAgent;

    private List<Performative> request;
    private List<Performative> recruitRequests;
    private List<Performative> joinRequests;
    private List<Agent> potentialPartners;
    private List<Agent> potentialReprodPartners;

    private long lastEnvFromPartners;

    public static void initRun() {
        cGroups = 0;
        cGroupsDissolved = 0;
        cMembers = 0;
        cMaxMembers = 0;
    }

    public AgentKomm(Agent agent) {
        if (agent.getSpecial() == AgentSpecial.LEADER) {
            cGroups++;
            cMembers++;
        }
        thisAgent = agent;
        leader = null;
        request = new ArrayList<Performative>();
        partners = new ArrayList<GameObject>();
        potentialPartners = new ArrayList<Agent>();
        recruitRequests = new ArrayList<Performative>();
        joinRequests = new ArrayList<Performative>();
        potentialReprodPartners = new ArrayList<Agent>();
    }

    public void death() {
        if (thisAgent.getSpecial() == AgentSpecial.LEADER)
            cGroupsDissolved++;
    }

    public void updateLeader() {
        AgentSpecial spec = thisAgent.getSpecial();

        cancelContracts();
        if (spec == AgentSpecial.LEADER) {
            leader = thisAgent;
        } else {
            leader = null;
        }
    }

    public void recruit(Agent agent) {
        Performative perf;
        if (leader == thisAgent) {
            perf = new Performative("ask-about", "attributes");
        } else {
            Hashtable<String, Integer> info = thisAgent.getInfo();
            perf = new Performative("advertise", info);
        }
        potentialPartners.add(agent);
    }


}
