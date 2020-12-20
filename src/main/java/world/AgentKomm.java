package world;

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

    private List<Hashtable<String, GameObject>> request;
    private List<Hashtable<String, GameObject>> recruitRequests;
    private List<Hashtable<String, GameObject>> joinRequests;
    private List<GameObject> potentialPartners;
    private List<Hashtable<String, GameObject>> potentialReprodPartners;

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
        request = new ArrayList<Hashtable<String, GameObject>>();
        partners = new ArrayList<GameObject>();
        potentialPartners = new ArrayList<GameObject>();
        recruitRequests = new ArrayList<Hashtable<String, GameObject>>();
        joinRequests = new ArrayList<Hashtable<String, GameObject>>();
        potentialReprodPartners = new ArrayList<Hashtable<String, GameObject>>();
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
        potentialPartners.add(agent);
    }


}
