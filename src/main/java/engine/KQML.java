package engine;

import engine.JMTRemnants.Performative;
import world.IAgent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class KQML {
    private static Hashtable<String, IAgent> AGENTS = new Hashtable<String, IAgent>();
    private static int agentID = 0;

    public KQML() {
        AGENTS = new Hashtable<String, IAgent>();
        agentID = 0;
    }

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

    public static void unregister(IAgent agent, String name) {
        AGENTS.remove(name);
    }

    public static void perform(IAgent sender, IAgent reciever, Performative performative) {
        performative.put(":sender", sender);
        performative.put(":reciever", reciever);

        reciever.performKQML(sender, performative);
    }

    public static void perform(IAgent sender, String recieverName, Performative performative) {
        IAgent reciever = (IAgent)AGENTS.get(recieverName);
        if (reciever != null)
            perform(sender, reciever, performative);
    }

    public static void multicast(IAgent sender, List<IAgent> recievers, Performative performative) {
        for (IAgent iAgent : recievers) {
            Performative perf = (Performative)performative.clone();
            perform(sender, iAgent, perf);
        }
    }
}
