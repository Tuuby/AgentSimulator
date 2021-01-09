package de.tuuby.AgentSimulator.world;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Portal extends GameObject {

    static int cPortalNo;

    public static int TEST_PERIOD = 4000;

    boolean functionable;
    boolean running;
    String hostName;
    int port;
    int portalNo;
    Portal thisPortal;

    ServerSocket listenSocket;

    public static void initRun() {
        cPortalNo = 0;
    }

    public void run() {
        Socket socket;
        try {
            while (running) {
                try {
                    socket = listenSocket.accept();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    boolean transfering = in.readBoolean();
                    if (transfering) {
                        Agent agent = (Agent)in.readObject();
                        agent.setWorld(world);
                        agent.initAgent(thisPortal);
                        agent.moveTo(x, y);
                        world.addObject(agent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            listenSocket.close();
        } catch (IOException e) {
            if (running) {
                System.err.println("Error while reading on port " + e);
                e.printStackTrace();
            }
        }
    }

    public Portal(int initX, int initY, World w, String host) {
        super(initX, initY, w);
        thisPortal = this;
        portalNo = cPortalNo++;
        functionable = false;

        int hostStart = host.indexOf(':');
        int portStart = host.lastIndexOf(':');
        int listenPort = Integer.parseInt(host.substring(0, hostStart));
        port = Integer.parseInt(host.substring(portStart + 1));
        hostName = host.substring(hostStart + 1, portStart);
        running = true;
    }

    public void thisRemovedFromWorld() {
        super.thisRemovedFromWorld();
        running = false;
        functionable = false;
        try {
            listenSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activate(Agent agent) {
        try {
            Socket socket = new Socket(hostName, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeBoolean(true);
            world.removeObject(agent);
            agent.setWorld(null);
            out.writeObject(agent);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFunctionable() {
        return functionable;
    }

    public String getType() {
        return "portal";
    }

    public boolean extendsTo(int dx, int dy) {
        return false;
    }

    public void update(long time) {
        int d = (int) (time % TEST_PERIOD) - TEST_PERIOD * portalNo / cPortalNo;
        if (time > TEST_PERIOD && d >= 0 && d < World.TIME_UNIT) {
            try {
                System.out.println("Testing portal: " + portalNo);
                Socket socket = new Socket(hostName, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(false);
                socket.close();
                functionable = true;
                System.out.println("working");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void render() {

    }
}
