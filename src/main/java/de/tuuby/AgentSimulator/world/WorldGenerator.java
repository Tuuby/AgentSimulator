package de.tuuby.AgentSimulator.world;

import de.tuuby.AgentSimulator.world.enums.AgentSpecial;

public class WorldGenerator {
    private World world;

    private int nHills;
    private int nPortals;
    private int nFoodElems;
    private int nQuarries;
    private int nAgents;
    private int hillSize;

    private String[] portalHost;

    private int[] nAgentSpecials;

    public WorldGenerator(int width, int height, int hillCount, int portalCount, String[] hosts, int foodCount,
                          int quarryCount, int agentCount, int attackerCount, int paralyzerCount, int leaderCount,
                          int scavengerCount, int hillSize) {
        nHills = hillCount;
        nPortals = portalCount;
        portalHost = hosts;
        nFoodElems = foodCount;
        nQuarries = quarryCount;
        nAgents = agentCount;
        nAgentSpecials = new int[4];
        nAgentSpecials[AgentSpecial.ATTACKER.getSpecialNumber()] = attackerCount;
        nAgentSpecials[AgentSpecial.PARALYZER.getSpecialNumber()] = paralyzerCount;
        nAgentSpecials[AgentSpecial.LEADER.getSpecialNumber()] = leaderCount;
        nAgentSpecials[AgentSpecial.SCAVENGER.getSpecialNumber()] = scavengerCount;
        this.hillSize = hillSize;
        world = new World(width, height);
    }

    public void clear() {
        world.doClear();
    }

    public void generate() {
        Agent.initRun();
        MovingFood.initRun();
        Portal.initRun();
        int i;
        int xsize = world.getWidth();
        int ysize = world.getHeight();

        for (i = 0; i < nHills; i++) {
            int[] pos = newCoords(xsize, ysize);
            Hill h = new Hill(pos[0], pos[1], world, hillSize);
            world.addObject(h);
        }

        for (i = 0; i < nPortals; i++) {
            int[] pos = newCoords(xsize, ysize);
            Portal p = new Portal(pos[0], pos[1], world, portalHost[i]);
            world.addObject(p);
        }

        for (i = 0; i < nFoodElems; i++) {
            int[] pos = newCoords(xsize, ysize);
            Food f = new Food(pos[0], pos[1], world);
            world.addObject(f);
        }

        for (i = 0; i < nQuarries; i++) {
            int[] pos = newCoords(xsize, ysize);
            MovingFood q = new MovingFood(pos[0], pos[1], world);
            world.addObject(q);
        }

        for (i = 0; i < nAgents; i++) {
            DNA dna = new DNA();
            int[] pos = newCoords(xsize, ysize);
            Agent agent = new Agent(pos[0], pos[1], world, dna);
            world.addObject(agent);
        }

        if (nAgentSpecials != null) {
            for (int j = 0; j < nAgentSpecials.length; j++) {
                for (i = 0; i < nAgentSpecials[j]; i++) {
                    DNA dna;
                    switch (j) {
                        case 0:
                            dna = new DNA(AgentSpecial.ATTACKER);
                            break;
                        case 1:
                            dna = new DNA(AgentSpecial.PARALYZER);
                            break;
                        case 2:
                            dna = new DNA(AgentSpecial.LEADER);
                            break;
                        case 3:
                            dna = new DNA(AgentSpecial.SCAVENGER);
                            break;
                        default:
                            dna = new DNA();
                    }
                    int[] pos = newCoords(xsize, ysize);
                    Agent agent = new Agent(pos[0], pos[1], world, dna);
                    world.addObject(agent);
                }
            }
        }
    }

    public World getWorld() {
        return world;
    }

    private int[] newCoords(int xrange, int yrange) {
        int x;
        int y;

        do {
            x = (int)(Math.random() * xrange);
            y = (int)(Math.random() * yrange);
        } while (!world.isPositionFree(x, y));
        int[] res = {x, y};
        return res;
    }
}
