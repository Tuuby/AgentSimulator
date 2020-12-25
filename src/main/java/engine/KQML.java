package engine;

import engine.JMTRemnants.Performative;
import world.IAgent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Vector;

// Class to represent the KQML communication of Agents
public class KQML {
    private static Hashtable<String, IAgent> AGENTS = new Hashtable<String, IAgent>();
    private static int agentID = 0;

    // Constructor for a new KQML object
    public KQML() {
        AGENTS = new Hashtable<String, IAgent>();
        agentID = 0;
    }

    // Register a new Agent Object in the AGENTS hashtable and set the name of the agent
    public static boolean register(IAgent agent, String name) {
        if (name != null)
            if (AGENTS.get(name) != null)
                return false;
        else {
            try {
                name = InetAddress.getLocalHost().toString() + ":" + agentID;
            } catch (UnknownHostException e) {
                name = "localhost:" + agentID;
            }
        }

        agentID++;
        AGENTS.put(name, agent);
        agent.setName(name);
        return true;
    }

    // Unregister the specified Agent from the AGENTS hashtable
    public static void unregister(IAgent agent, String name) {
        AGENTS.remove(name);
    }

    // Puts two fields into the performative and then gives it to the receiving Agent
    public static void perform(IAgent sender, IAgent receiver, Performative performative) {
        performative.put(":sender", sender);
        performative.put(":receiver", receiver);

        receiver.performKQML(sender, performative);
    }

    // Method that gets the IAgent object from the specified name and then passes that to the other perform method
    public static void perform(IAgent sender, String receiverName, Performative performative) {
        IAgent receiver = (IAgent)AGENTS.get(receiverName);
        if (receiver != null)
            perform(sender, receiver, performative);
    }

    // Method to send a single Performative from a sender to multiple receivers
    public static void multicast(IAgent sender, Vector<IAgent> receivers, Performative performative) {
        for (IAgent iAgent : receivers) {
            Performative perf = (Performative)performative.clone();
            perform(sender, iAgent, perf);
        }
    }
}
