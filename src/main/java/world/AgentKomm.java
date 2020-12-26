package world;

import engine.JMTRemnants.Performative;
import engine.KQML;
import sun.misc.Perf;
import world.enums.AgentSpecial;
import world.enums.AgentStates;

import java.util.*;
import java.util.prefs.PreferenceChangeEvent;

// Java class to represent an agent in a group
public class AgentKomm {

    // Fields to count global statistics
    public static int cGroups;
    public static int cGroupsDissolved;
    public static int cMembers;
    public static int cMaxMembers;

    // Fields to count individual statistics
    public int groupsJoined;
    public int groupsLeft;
    public int groupsDissolved;
    public int cancellations;

    // Fields to hold the other people in the group and the leader of the group
    protected Agent leader;
    protected Vector<IAgent> partners;

    // Field to hold the agent this instance belongs to
    private Agent thisAgent;

    // Fields to hold requests and other Agents for joining the group
    private Vector<Performative> requests;
    private Vector<Performative> recruitRequests;
    private Vector<Performative> joinRequests;
    private Vector<Agent> potentialPartners;
    private Vector<Agent> potentialReprodPartners;

    // Field to hold the point in time when the last environmentUpdate from Partners arrived
    private long lastEnvFromPartners;

    // Fields to hold requests and offers for reproduction
    private Vector<Agent> reprodOffered = new Vector<Agent>();
    private long lastReprodOffered = 0;
    private Vector<Performative> reproduceRequests = new Vector<Performative>();
    private Vector<Agent> gotReprodOfferFrom = new Vector<Agent>();
    private Agent acceptReprodOffers = null;

    // Field to hold the success of the group
    private static final int SUCCESS_EPS = 500;

    // Method to start counting the global Statistics
    public static void initRun() {
        cGroups = 0;
        cGroupsDissolved = 0;
        cMembers = 0;
        cMaxMembers = 0;
    }

    // Constructor for a new Group of Agents
    public AgentKomm(Agent agent) {
        if (agent.getSpecial() == AgentSpecial.LEADER) {
            cGroups++;
            cMembers++;
        }
        thisAgent = agent;
        leader = null;
        requests = new Vector<Performative>();
        partners = new Vector<IAgent>();
        potentialPartners = new Vector<Agent>();
        recruitRequests = new Vector<Performative>();
        joinRequests = new Vector<Performative>();
        potentialReprodPartners = new Vector<Agent>();
    }

    // Method that deals with the leader of a group dying
    public void death() {
        if (thisAgent.getSpecial() == AgentSpecial.LEADER)
            cGroupsDissolved++;
    }

    // Method to update the leader of a group
    public void updateLeader() {
        AgentSpecial spec = thisAgent.getSpecial();

        cancelContracts();
        if (spec == AgentSpecial.LEADER) {
            leader = thisAgent;
        } else {
            leader = null;
        }
    }

    // Method to recruit a new Agent to the group
    public void recruit(Agent agent) {
        Performative performative;
        if (leader == thisAgent) {
            performative = new Performative("ask-about", "attributes");
        } else {
            Hashtable<String, Integer> info = thisAgent.getInfo();
            performative = new Performative("advertise", info);
        }
        potentialPartners.add(agent);
        KQML.perform(thisAgent, agent, performative);
    }

    // Method to tell the Agents in Partners what target should be hunted
    public void startHunt(GameObject target) {
        Performative performative = new Performative("achieve", target);
        KQML.multicast(thisAgent, partners, performative);
    }

    // Method to tell the Agents in partners how much food can be expected from the target
    public void notifyFoodAmount(GameObject target, int energy) {
        Performative performative = new Performative("achieve", target);
        performative.put(":amount", energy);
        KQML.multicast(thisAgent, partners, performative);
    }

    // Method to look for an agent in the environment
    public Agent findAgent(boolean recruiting) {
        Vector<GameObject> envObjects = thisAgent.getEnvironment();
        for (GameObject object : envObjects) {
            if (object instanceof Agent && object != thisAgent) {
                Agent agent = (Agent)object;
                if (agent != leader && !partners.contains(agent) && !potentialPartners.contains(agent) && (agent.getSpecial() == AgentSpecial.LEADER) == recruiting)
                    return agent;
            }
        }
        return null;
    }

    // Method to receive Environment infos from partners und apply them
    public void getEnvFromPartners(long lastUpdate) {
        if (thisAgent == leader && lastUpdate > lastEnvFromPartners) {
            lastEnvFromPartners = lastUpdate;
            Performative performative = new Performative("ask-about", "environment");
            KQML.multicast(thisAgent, partners, performative);
        }
    }

    // Method to broadcast to all agents in the environment, that reproduction is requested
    public void broadcastReprodReq(long time) {
        if (time - lastReprodOffered >= 40) {
            reprodOffered = new Vector<Agent>();
            lastReprodOffered = time;
            gotReprodOfferFrom = new Vector<Agent>();
        }
        boolean male = thisAgent.getDna().male;
        Vector<GameObject> envObjects = thisAgent.getEnvironment();
        for (GameObject envObject : envObjects) {
            if (envObject instanceof Agent && ((Agent)envObject).getDna().male != male && !reprodOffered.contains(envObject) && !gotReprodOfferFrom.contains(envObject)) {
                Performative request = new Performative("reproduce", thisAgent.getInfo());
                KQML.perform(thisAgent, (Agent)envObject, request);
                reprodOffered.add((Agent) envObject);
                lastReprodOffered = time;
            }
        }
    }

    // Method to add a received performative to the list of requests
    public void handleRequest(Performative performative) {
        requests.add(performative);
    }

    // Method to defer requests to the list of deferredRequests
    public void handleRequests() {
        Vector<Performative> deferredRequests = new Vector<Performative>();
        for (Performative request : requests) {
            Agent sender = (Agent)request.get(":sender");
            Performative deferred = handleRequest(sender, request);
            if (deferred != null)
                deferredRequests.add(deferred);
        }
        requests = deferredRequests;
        handleJoinRequests();
    }

    // Method to handle a specific request from a specific sender
    private Performative handleRequest(Agent sender, Performative perf) {
        String type = perf.getType();
        Object content = perf.get(":content");
        if (type.equals("accept"))
        {
            Performative what = (Performative)content;

            if (recruitRequests.contains(what)) {
                recruitRequests.remove(what);
                potentialPartners.remove(sender);

                if (leader == thisAgent) {
                    partners.add(sender);
                    //thisAgent.notifyGroupState();
                    cMembers++;

                    if (partners.size() > cMaxMembers)
                        cMaxMembers++;

                    if (thisAgent.getState() == AgentStates.HUNTING)
                        KQML.perform(thisAgent, sender, new Performative("achieve", thisAgent.getTarget()));
                } else // we are not leading any more, so cancel
                    KQML.perform(thisAgent, sender, new Performative("cancel"));
            } else
                KQML.perform(thisAgent, sender, new Performative("sorry", perf));
        } else if(type.equals("achieve")) {
            if(sender == leader || partners.contains(sender)) {
                Integer amount = (Integer)perf.get(":amount");
                MovingFood target = (MovingFood)content;
                if(!thisAgent.setTarget(target, amount))
                    return perf;
            } else
                KQML.perform(thisAgent, sender, new Performative("sorry", perf));
        } else if(type.equals("advertise")) {
            boolean reqHanging = potentialPartners.contains(sender);

            if (isRecruitable(sender, content)) {
                Performative reply;
                if (leader == thisAgent) {
                    reply = new Performative("join", partners);
                    reply.put(":success", thisAgent.getSuccess());
                    recruitRequests.add(reply);
                    //potentialPartners.addElement(sender);
                    KQML.perform(thisAgent, sender, reply);
                } else
                    KQML.perform(thisAgent, sender, new Performative("sorry", perf));
            } else
                KQML.perform(thisAgent, sender, new Performative("sorry", perf));
        } else if (type.equals("ask-about")) {
            if (content.equals("environment")) {
                if (isTrustworthy(sender)) {
                    Performative reply = new Performative("tell");
                    reply.put(":subject", "environment");
                    reply.put(":content", thisAgent.getEnvironment());
                    KQML.perform(thisAgent, sender, reply);
                } else
                    KQML.perform(thisAgent, sender, new Performative("sorry", perf));
            } else if (content.equals("partners")) {
                if (leader == thisAgent && isTrustworthy(sender)) {
                    Performative reply = new Performative("tell");
                    reply.put(":subject", "partners");
                    reply.put(":content", partners);
                    KQML.perform(thisAgent, sender, reply);
                } else
                    KQML.perform(thisAgent, sender, new Performative("sorry", perf));
            } else if (content.equals("abilities")) {
                KQML.perform(thisAgent, sender, new Performative("advertise", thisAgent.getInfo()));
            }
        } else if (type.equals("cancel")) {
            partnerWithdraw(sender);
        } else if (type.equals("decline")) {
            Performative what = (Performative)content;
            if (recruitRequests.contains(what)) {
                recruitRequests.remove(what);
                potentialPartners.remove(sender);
            } else
                KQML.perform(thisAgent, sender, new Performative("error", perf));
        } else if (type.equals("join")) {
            if (content instanceof Vector) {
                if (leader == thisAgent) {
                    if (giveUpOwnRecruitingFor(perf)) {
                        cancelContracts();
                        leader = sender;
                        KQML.perform(thisAgent, sender, new Performative("accept", perf));
                        partners = (Vector<IAgent>) content;
                    } else
                        KQML.perform(thisAgent, sender, new Performative("decline", perf));
                } else {
                    joinRequests.add(perf);
                }
            } else
                KQML.perform(thisAgent, sender, new Performative("error", perf));
        } else if (type.equals("reproduce")) {
            if (thisAgent.getReproductionPartner() == null && (acceptReprodOffers == null || acceptReprodOffers == sender)) {
                gotReprodOfferFrom.add(sender);
                if (content != null) {
                    Hashtable<String, Integer> myInfo = thisAgent.getInfo();
                    int qual = getQuality((Hashtable)content);
                    int myQual = getQuality(myInfo);
                    if (potentialReprodPartners.contains(sender)) {
                        //KQML.perform(thisAgent, sender, new Performative("reproduce")); // final comm. step
                        initReproduction(sender);
                        sender.getKomm().initReproduction(thisAgent);
                    } else if (reprodOffered.contains(sender) || thisAgent.readyForReproduction() && thisAgent.getEnvironment().contains(sender) && (qual >= myQual || myQual-qual < Math.random()*50)) {
                        Performative reply = new Performative("reproduce", myInfo);
                        KQML.perform(thisAgent, sender, reply);
                        reproduceRequests.add(reply);
                        potentialReprodPartners.add(sender);
                        acceptReprodOffers = sender;
                    } else
                        KQML.perform(thisAgent, sender, new Performative("sorry", perf));
                } else if (potentialReprodPartners.contains(sender))
                    initReproduction(sender);
                else
                    KQML.perform(thisAgent, sender, new Performative("sorry", perf));
            } else
                KQML.perform(thisAgent, sender, new Performative("sorry", perf));
        } else if (type.equals("sorry") && content instanceof Performative && ((Performative)content).getType().equals("reproduce")) {
            if (gotReprodOfferFrom.contains(sender))
                gotReprodOfferFrom.remove(sender);

            if (reproduceRequests.contains(content)) {
                potentialReprodPartners.remove(sender);
                reproduceRequests.remove(content);
            }

            if (sender == acceptReprodOffers)
                acceptReprodOffers = null;
        } else if (type.equals("tell")) {
            Object subject = perf.get(":subject");
            if (subject.equals("environment")) {
                if (content instanceof Vector) {
                    Vector<GameObject> env = thisAgent.getEnvironment();
                    Vector v = (Vector)content;
                    Enumeration elems = v.elements();
                    while(elems.hasMoreElements()) {
                        Object item = elems.nextElement();
                        if (!env.contains(item))
                            env.add((GameObject)item);
                    }
                } else
                    KQML.perform(thisAgent, sender, new Performative("error", perf));
            } else if (subject.equals("partners")) {
                if (content instanceof Vector) {
                    if (sender == leader) {
                        partners = (Vector)content;
                        //lastPartnersUpdate = time;
                    }
                } else
                    KQML.perform(thisAgent, sender, new Performative("error", perf));
            }
        }
        return null;
    }

    // Method to remove an Agent from the group
    public void partnerWithdraw(Agent agent) {
        if (leader == thisAgent && partners.contains(agent)) {
            AgentStates state = thisAgent.getState();
            partners.remove(agent);
            //thisAgent.notifyGroupState();
            cancellations++;
            if (state == AgentStates.HUNTING && !groupSufficient())
                thisAgent.setState(AgentStates.SEARCHING_AGENT);
            else if (state == AgentStates.EATING)
                thisAgent.setFoodAmount();
        } else if (agent == leader) {
            leader = null;
            partners = new Vector<IAgent>();
            //thisAgent.notifyGroupState();
            cancellations++;
        }

        if (reprodOffered.contains(agent))
            reprodOffered.remove(agent);

        if (gotReprodOfferFrom.contains(agent))
            gotReprodOfferFrom.remove(agent);

        if (acceptReprodOffers == agent)
            acceptReprodOffers = null;

        if (potentialReprodPartners.contains(agent)) {
            potentialReprodPartners.remove(agent);
            reproduceRequests = new Vector<Performative>();
        }
    }

    // Method to check if the group has enough different Specialities
    public boolean groupSufficient() {
        if (leader == thisAgent) {
            int attacker = 0;
            int paralyzer = 0;
            for (IAgent agent : partners) {
                Agent p = (Agent)agent;
                if (p.getSpecial() == AgentSpecial.ATTACKER)
                    attacker++;
                else if (p.getSpecial() == AgentSpecial.PARALYZER)
                    paralyzer++;
            }
            return attacker >= 1 && paralyzer >= 1;
        }
        return false;
    }

    // Method to handle the join Requests
    private void handleJoinRequests() {
        int nrequests = joinRequests.size();
        if (leader != thisAgent) {
            Performative bestRequest = null;
            Performative currRequest;
            if (leader != null) {
                currRequest = new Performative("pseudo", partners);
                currRequest.put(":success", thisAgent.getSuccess());
            } else
                currRequest = null;
            for (int i = 0; i < nrequests; i++) {
                Performative performative = joinRequests.get(i);
                Agent sender = (Agent)performative.get(":sender");
                if (groupIsBetter(performative, currRequest)) {
                    currRequest = performative;
                    bestRequest = performative;
                }
            }
            if (bestRequest != null) {
                cancelContracts();
                joinRequests.remove(bestRequest);
                nrequests--;
                Agent sender = (Agent)bestRequest.get(":sender");
                KQML.perform(thisAgent, sender, new Performative("accept", bestRequest));
                leader = sender;
                groupsJoined++;
                partners = (Vector<IAgent>) bestRequest.get(":content");
                thisAgent.setSuccess(getSuccess(bestRequest));
            }
        }
        for (int i = 0; i < nrequests; i++) {
            Performative performative = joinRequests.get(i);
            Agent sender = (Agent)performative.get(":sender");
            KQML.perform(thisAgent, sender, new Performative("decline", performative));
        }
        joinRequests = new Vector<Performative>();
    }

    // Method to cancel all contracts and empty the list of partners
    private void cancelContracts() {
        Performative cancelPerf = new Performative("cancel");
        if (leader == thisAgent) {
            groupsDissolved++;
            cGroupsDissolved++;
            KQML.multicast(thisAgent, partners, cancelPerf);
            //thisAgent.notifyGroupState();
        } else if (leader != null) {
            groupsLeft++;
            KQML.perform(thisAgent, leader, cancelPerf);
            leader = null;
        }
        partners = new Vector<IAgent>();
    }

    // Dont know
    private boolean giveUpOwnRecruitingFor(Performative performative) {
        return false;
    }

    // Method to check if the success of a group is better than then of another
    private boolean groupIsBetter(Performative performative1, Performative performative2) {
        if (performative2 == null) {
            return true;
        }
        int succ1 = getSuccess(performative1);
        int succ2 = getSuccess(performative2);

        return succ1 - succ2 > SUCCESS_EPS;
    }

    // Method to check if a specific agent is recruitable
    private boolean isRecruitable(Agent agent, Object info) {
        Hashtable<String, Integer>infoTable = (Hashtable<String, Integer>)info;
        int subGroupSize = 0;
        AgentSpecial special = agent.getSpecial();
        Hashtable<String, Integer> worstInfo = null;
        Agent worstAgent = null;
        for (IAgent iAgent : partners) {
            Agent p = (Agent)iAgent;
            if (p.getSpecial() == special) {
                subGroupSize++;
                Hashtable<String, Integer> pinfo = p.getInfo();
                if (worstInfo == null || info1IsBetter(worstInfo, pinfo)) {
                    worstInfo = pinfo;
                    worstAgent = p;
                }
            }
        }

        DNA dna = thisAgent.getDna();

        if (special == AgentSpecial.ATTACKER && subGroupSize < dna.preferredNofAttacker) {
            return true;
        } else if (special == AgentSpecial.PARALYZER && subGroupSize < dna.preferredNofParalyser) {
            return true;
        } else if (info1IsBetter(infoTable, worstInfo)) {
            if (worstAgent != null) {
                KQML.perform(thisAgent, worstAgent, new Performative("cancel"));
                partners.remove(worstAgent);
                //thisAgent.notifyGroupState();
            }
            return true;
        } else
            return false;
    }

    // Method to compare two infos to each other
    private boolean info1IsBetter(Hashtable<String, Integer> info1, Hashtable<String, Integer> info2) {
        if (info2 == null)
            return true;
        else
            return (info1.get("speed") > info2.get("speed"));
    }

    // Method to check if and agent is the leader or in the group
    private boolean isTrustworthy(Agent agent) {
        return agent == leader || partners.contains(agent);
    }

    // Method to read the success value out of a performative
    private int getSuccess(Performative perf) {
        Integer success = (Integer)perf.get(":success");
        return success != null ? success : 0;
    }

    // Method to initiate the reproduction with a new partner
    protected void initReproduction(Agent partner) {
        potentialReprodPartners = new Vector<Agent>();
        reproduceRequests = new Vector<Performative>();
        gotReprodOfferFrom = new Vector<Agent>();
        thisAgent.initReproduction(partner);
        acceptReprodOffers = null;
    }

    // Method to calculate a quality value for the agent
    private int getQuality(Hashtable<String, Integer> info) {
        Integer healthObj = (Integer)info.get("health");
        Integer speedObj = (Integer)info.get("speed");
        Integer visibleObj = (Integer)info.get("visibility");
        Integer staminaObj = (Integer)info.get("stamina");
        int health = healthObj != null ? healthObj : 0;
        int speed = speedObj != null ? speedObj : 0;
        int visibility = visibleObj != null ? visibleObj : 0;
        int stamina = staminaObj != null ? staminaObj : 0;
        int qual = health * 5 + speed * 10 + visibility + stamina / 10;
        return qual / 10 - 150;
    }

    // Method to check if the agent is ready for Reproduction
    private boolean readyForReproduction() {
        return thisAgent.getState() == AgentStates.REPRODUCING || !thisAgent.conditionCritical();
    }
}
