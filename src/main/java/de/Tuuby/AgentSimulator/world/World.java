package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.graphics.Graphics;
import de.Tuuby.AgentSimulator.guis.SwingManager;
import de.Tuuby.AgentSimulator.logging.DataSet;
import de.Tuuby.AgentSimulator.logging.LoggingHandler;
import de.Tuuby.AgentSimulator.world.enums.AgentSpecial;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

// Class that represents a world filled of hills and Agents
public class World {

    // dont know what that is tbh
    public final static int TIME_UNIT = 10;

    // Variable for the size of a single grid cell
    private final static int GRIDSIZE = 80;

    // 2-dimensional array of Lists of GameObjects that can be modified while a thread is running
    private ConcurrentLinkedQueue<GameObject>[][] world;

    // Attributes of a world width & height are the size; time is the elapsed time in the world
    private int width;
    private int height;
    private int wspeed;
    private long time;

    private int foodSpawnCount = 0;

    private int foodCounter;
    private int herbivoreCounter;
    private int agentCounter;

    private Queue<Double> foodPopValues;
    private Queue<Double> herbivorePopValues;
    private Queue<Double> agentPopValues;

    private int paralyzedHerbivoreCounter;
    private int deadHerbivoreCounter;
    private int totalHerbivores;
    private int femaleAgentCounter;
    private int attackerCounter;
    private int paralyzerCounter;
    private int leaderCounter;
    private int inGroupCounter;
    private int totalAgents;

    private double avgHerbEnergy;
    private int maxHerbEnergy;
    private double avgHerbFood;
    private int maxHerbFood;
    private double avgHerbSpeed;
    private int maxHerbSpeed;
    private double avgAgentSpeed;
    private int maxAgentSpeed;
    private double avgHerbVisionRange;
    private int maxHerbVisionRange;
    private double avgGeneration;
    private int maxGeneration;
    private double avgAge;
    private int maxAge;
    public double avgAgentVisionRange;
    public int maxAgentVisionRange;
    public double avgAgentStamina;
    public int maxAgentStamina;
    public double avgAgentMaxStamina;
    public int maxAgentMaxStamina;
    public double avgAgentFood;
    public int maxAgentFood;
    public double avgAgentFoodCapacity;
    public int maxAgentFoodCapacity;

    public int maxGroupMembers;
    public double avgGroupMembers;
    public int maxGroupSuccess;
    public double avgGroupSuccess;

    private DataSet infoData;

    private WorldGenerator generator;

    // Constructor that mainly initializes the 2d array
    public World(int width, int height, WorldGenerator generator) {
        time = 0;
        wspeed = 5;
        this.width = width;
        this.height = height;
        world = new ConcurrentLinkedQueue[width / GRIDSIZE][height / GRIDSIZE];
        //world = new Vector[width / GRIDSIZE][height / GRIDSIZE];
        for (int j = 0; j < world[0].length; j++)
            for (int i = 0; i < world.length; i++)
                world[i][j] = new ConcurrentLinkedQueue<GameObject>();

        foodPopValues = new ConcurrentLinkedQueue<Double>();
        herbivorePopValues = new ConcurrentLinkedQueue<Double>();
        agentPopValues = new ConcurrentLinkedQueue<Double>();

        totalHerbivores = 0;

        infoData = new DataSet();
        this.generator = generator;
    }

    // Getter and Setter
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getTime() {
        return time;
    }

    public void setWspeed(int wspeed) {
        this.wspeed = wspeed;
    }

    public void setFoodCount(int foodCount) {
        this.foodCounter = foodCount;
    }

    public void setHerbivoreCount(int herbivoreCount) {
        this.herbivoreCounter = herbivoreCount;
    }

    public void setAgentCount(int agentCount) {
        this.agentCounter = agentCount;
    }

    public void setFoodSpawnCount(int foodSpawnCount) {
        this.foodSpawnCount = foodSpawnCount;
    }

    public int getFoodCount() {
        return foodCounter;
    }

    public int getHerbivoreCount() {
        return herbivoreCounter;
    }

    public int getAgentCount() {
        return agentCounter;
    }

    public double getAvgHerbEnergy() {
        return avgHerbEnergy;
    }

    public Agent getAgentById(long agentId) {
        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent) {
                        if (go.getUniqueID() == agentId)
                            return (Agent) go;
                    }
                }
            }
        }
        return null;
    }

    // Method to clear the entire world of GameObjects
    public void doClear() {
        for (int j = 0; j < world[0].length; j++)
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j])
                    go.thisRemovedFromWorld();
                world[i][j].clear();
            }
        time = 0;

        foodPopValues = new ConcurrentLinkedQueue<Double>();
        herbivorePopValues = new ConcurrentLinkedQueue<Double>();
        agentPopValues = new ConcurrentLinkedQueue<Double>();
    }

    // Method to update all GameObjects
    public void updateAll() {
        if (Math.random() < 0.01f)
            spawnRandomFood(foodSpawnCount);

        for (int j = 0; j < world[0].length; j++)
            for (int i = 0; i < world.length; i++)
                for (GameObject go : world[i][j])
                    go.update(time);
        time += TIME_UNIT;

        countGameObjects();
        calculateData();

    }

    public void renderAll() {
        Graphics.setColor(0.4f, 0.75f, 0.35f, 1);
        Graphics.fillRect(width / 2.0f, height / 2.0f, width, height);
        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent && infoData.selectedAgent != null && go.getUniqueID() == infoData.selectedAgent.uniqueID) {
                        ((Agent) go).renderHighlight();
                    }
                    go.render();
                }
            }
        }
    }

    public void renderDebug() {
        for (int j = 0; j < world[0].length; j++)
            for (int i = 0; i < world.length; i++)
                for (GameObject go : world[i][j])
                    go.renderDebug();
    }

    // Method to add an object to the array of GameObjects at the world position
    public void addObject(GameObject go) {
        int cellX = go.getX() / GRIDSIZE;
        int cellY = go.getY() / GRIDSIZE;

        world[cellX][cellY].offer(go);
        if (go instanceof MovingFood) {
            totalHerbivores++;
        } else if (go instanceof Agent) {
            totalAgents++;
        }
    }

    // Method to remove an object from the array of Gameobjects and notify the other objects about it
    public void removeObject(GameObject go) {
        int cellX = go.getX() / GRIDSIZE;
        int cellY = go.getY() / GRIDSIZE;

        world[cellX][cellY].remove(go);
        go.thisRemovedFromWorld();

        for (int j = 0; j < world[0].length; j++)
            for (int i = 0; i < world.length; i++)
                for (GameObject gameObject : world[i][j])
                    gameObject.gameObjectRemovedFromWorld(go);
    }

    // Method to return the GameObject closest to the coordinates
    public GameObject getObjectAt(int x, int y) {
        float min_dist2 = Float.MAX_VALUE;
        GameObject retObject = null;
        Vector<GameObject> env = getEnvironment(x, y, 2 * GRIDSIZE);

        for (GameObject go : env) {
            float dx = go.getX() - x;
            float dy = go.getY() - y;
            float dist2 = dx * dx + dy * dy;

            if (dist2 < min_dist2) {
                min_dist2 = dist2;
                retObject = go;
            }
        }
        return retObject;
    }

    // Method to get all GameObjects in a radius around the coordinates
    public Vector<GameObject> getEnvironment(int x, int y, int radius) {
        Vector<GameObject> objects = new Vector<GameObject>();
        int cellX = y / GRIDSIZE;
        int cellY = x / GRIDSIZE;
        int r2 = radius * radius;
        int neighb = 1;

        while (radius > GRIDSIZE) {
            neighb++;
            radius -= GRIDSIZE;
        }

        for (int j = cellY - neighb; j <= cellY + neighb; j++)
            for (int i = cellX - neighb; i <=  cellX + neighb; i++) {
                int cx = (i + world.length) % world.length;
                int cy = (j + world[0].length) % world[0].length;
                for (GameObject gameObject : world[cy][cx]) {
                    float dx = gameObject.getX() - x;
                    float dy = gameObject.getY() - y;
                    if (dx * dx + dy * dy <= r2)
                        objects.add(gameObject);
                }
            }
        return objects;
    }

    // Probably a method to get all GameObjects
    public Enumeration<GameObject> getItems() {
        return new Enumeration<GameObject>() {
            int cx = 0;
            int cy = 0;
            int ci = 0;
            ConcurrentLinkedQueue<GameObject> cell = world[0][0];
            GameObject next = null;
            public boolean hasMoreElements() {
                while(ci >= cell.size()) {
                    ci = 0;
                    cx++;
                    if (cx >= world[0].length) {
                        cx = 0;
                        cy++;
                        if (cy >= world[0].length)
                            return false;
                    }
                    cell = world[cx][cy];
                }
                next = cell.poll();
                return true;
            }

            public GameObject nextElement() {
                return next;
            }
        };
    }

    // Method to get the X coordinate of the cell, the Gameobject is in
    public int getCellX(GameObject gameObject) {
        return gameObject.getX() / GRIDSIZE;
    }

    // Method to get the Y coordinate of the cell, the GameObject is in
    public int getCellY(GameObject gameObject) {
        return gameObject.getY() / GRIDSIZE;
    }

    // Method to check if a GameObject from around the cell extends to the coordinates
    public boolean isPositionFree(int x, int y) {
        int cellX = x / GRIDSIZE;
        int cellY = y / GRIDSIZE;
        for (int j = cellY - 1; j <= cellY + 1; j++)
            for (int i = cellX - 1; i <= cellX + 1; i++) {
                int cx = (i + world.length) % world.length;
                int cy = (j + world[0].length) % world[0].length;

                for (GameObject go : world[cx][cy]) {
                    if (go.extendsTo(go.getX() - x, go.getY() - y))
                        return false;
                }
            }
        return true;
    }

    // Method to move a GameObject by a specific amount if the target position is free
    public boolean moveObjectBy(GameObject go, int dx, int dy) {
        int x = go.getX();
        int y = go.getY();
        int newX = (x + width + dx) % width;
        int newY = (y + width + dy) % height;

        if (isPositionFree(newX, newY)) {
            int cellX = x / GRIDSIZE;
            int cellY = y / GRIDSIZE;

            go.moveTo(newX, newY);

            int ncellX = newX / GRIDSIZE;
            int ncellY = newY / GRIDSIZE;

            if (ncellX != cellX || ncellY != cellY) {
                if (!world[cellX][cellY].contains(go))
                    System.out.println("Serious world error!");

                world[cellX][cellY].remove(go);
                world[ncellX][ncellY].add(go);
            }
            return true;
        } else
            return false;
    }

    // Method to get the most efficient direction for a target position
    public int getD1(int a, int b) {
        int dx = b - a;
        if (dx > width / 2)
            dx -= width;
        else if (dx < -width / 2)
            dx += width;

        return dx;
    }

    // Method to calculate the distance between two GameObjects
    // + 0.5f was used in integer Number Space to round up; can be discarded here
    public int distance(GameObject go0, GameObject go1) {
        int dx = getD1(go0.getX(), go1.getX());
        int dy = getD1(go0.getY(), go1.getY());

        return (int)(Math.sqrt(dx * dx + dy * dy) + 0.5f);
    }

    // Method to calculate the distance between two coordinates
    public int distance(int dx, int dy) {
        return (int)(Math.sqrt(dx * dx + dy * dy) + 0.5f);
    }

    // Method to check if a GameObject is visible from another one
    public boolean visible(GameObject go, GameObject target, int visibility) {
        int dx = target.getX() - go.getX();
        int dy = target.getY() - go.getY();

        return  distance(dx, dy) <= visibility;
    }

    private void spawnRandomFood(int foodAmount) {

        for (int i = 0; i < foodAmount; i++) {

            int x;
            int y;

            do {
                x = (int)(Math.random() * width);
                y = (int)(Math.random() * height);
            } while (!isPositionFree(x, y));

            addObject(new Food(x, y, this));
        }
    }

    private void countGameObjects() {
        foodCounter = 0;
        herbivoreCounter = 0;
        agentCounter = 0;
        paralyzedHerbivoreCounter = 0;
        deadHerbivoreCounter = 0;
        femaleAgentCounter = 0;
        attackerCounter = 0;
        paralyzerCounter = 0;
        leaderCounter = 0;
        inGroupCounter = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Food)
                        foodCounter++;
                    else if (go instanceof MovingFood) {
                        if (((MovingFood) go).isAlive()) {
                            herbivoreCounter++;
                        } else {
                            deadHerbivoreCounter++;
                        }
                        if (((MovingFood) go).isParalyzed())
                            paralyzedHerbivoreCounter++;
                    } else if (go instanceof Agent) {
                        Agent agent = (Agent)go;
                        agentCounter++;
                        if (!agent.getDna().male) {
                            femaleAgentCounter++;
                        }
                        if (agent.getSpecial() == AgentSpecial.ATTACKER) {
                            attackerCounter++;
                        } else if (agent.getSpecial() == AgentSpecial.PARALYZER) {
                            paralyzerCounter++;
                        } else {
                            leaderCounter++;
                        }
                        if (agent.getKomm().partners.size() > 1) {
                            inGroupCounter++;
                        }
                    }
                }
            }
        }

        foodPopValues.offer((double) foodCounter);
        if (foodPopValues.size() > 60) {
            foodPopValues.poll();
        }
        Double[] tempFoodData = new Double[foodPopValues.size()];
        foodPopValues.toArray(tempFoodData);
        double[] foodData = convertArray(tempFoodData);

        herbivorePopValues.offer((double) herbivoreCounter);
        if (herbivorePopValues.size() > 60) {
            herbivorePopValues.poll();
        }
        Double[] tempHerbData = new Double[herbivorePopValues.size()];
        herbivorePopValues.toArray(tempHerbData);
        double[] herbData = convertArray(tempHerbData);


        agentPopValues.offer((double) agentCounter);
        if (agentPopValues.size() > 60) {
            agentPopValues.poll();
        }
        Double[] tempAgentData = new Double[agentPopValues.size()];
        agentPopValues.toArray(tempAgentData);
        double[] agentData = convertArray(tempAgentData);

        double[] worldAgeData = new double[foodPopValues.size()];
        for (int i = 0; i < worldAgeData.length; i++) {
            worldAgeData[i] = (double) (time + ((i - worldAgeData.length) * 10L));
        }

        SwingManager.updatePopulation(worldAgeData, foodData, herbData, agentData);
        SwingManager.updateLabels(infoData);

        String data = "Populations: " + "Food: " + foodCounter +
                "Herbivores: " + herbivoreCounter +
                "Carnivores: " + agentCounter;
        LoggingHandler.logPopulationStats(0, data, time);
    }

    public void calculateData() {
        calculateFood();
        calculateSpeeds();
        calculateVisions();
        calculateGenerations();
        calculateStamina();
        calculateEnergy();
        calculateGroups();

        updateDataSet();
    }

    private void calculateEnergy() {
        avgHerbEnergy = 0;
        maxHerbEnergy = 0;
        int herbEnergySum = 0;


        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof MovingFood) {
                        int energy = ((MovingFood) go).getEnergy();
                        herbEnergySum += energy;
                        if (energy > maxHerbEnergy) {
                            maxHerbEnergy = energy;
                        }
                    }
                }
            }
        }

        avgHerbEnergy = herbEnergySum * 1.0 / herbivoreCounter;
    }

    private void calculateFood() {
        avgAgentFood = 0;
        maxAgentFood = 0;
        int agentFoodSum = 0;
        avgAgentFoodCapacity = 0;
        maxAgentFoodCapacity = 0;
        int agentFoodCapacitySum = 0;
        avgHerbFood = 0;
        maxHerbFood = 0;
        int herbFoodSum = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent) {
                        int food = ((Agent) go).getFood();
                        int foodCap = ((Agent) go).getDna().foodCapacity;
                        agentFoodSum += food;
                        agentFoodCapacitySum += foodCap;
                        if (food > maxAgentFood) {
                            maxAgentFood = food;
                        }
                        if (foodCap > maxAgentFoodCapacity) {
                            maxAgentFoodCapacity = foodCap;
                        }
                    } else if (go instanceof MovingFood) {
                        int food = ((MovingFood) go).getFood();
                        herbFoodSum += food;
                        if (food > maxHerbFood) {
                            maxHerbFood = food;
                        }
                    }
                }
            }
        }

        avgAgentFood = agentFoodSum * 1.0 / agentCounter;
        avgAgentFoodCapacity = agentFoodCapacitySum * 1.0 / agentCounter;
        avgHerbFood = herbFoodSum * 1.0 / herbivoreCounter;
    }

    private void calculateSpeeds() {
        avgHerbSpeed = 0;
        maxHerbSpeed = 0;
        int herbSpeedSum = 0;
        avgAgentSpeed = 0;
        maxAgentSpeed = 0;
        int agentSpeedSum = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof MovingFood) {
                        int speed = ((MovingFood) go).getSpeed();
                        herbSpeedSum += speed;
                        if (speed > maxHerbSpeed)
                            maxHerbSpeed = speed;
                    } else if (go instanceof Agent) {
                        int speed = ((Agent) go).getSpeed();
                        agentSpeedSum += speed;
                        if (speed > maxAgentSpeed) {
                            maxAgentSpeed = speed;
                        }
                    }
                }
            }
        }

        avgHerbSpeed = herbSpeedSum * 1.0 / herbivoreCounter;
        avgAgentSpeed = agentSpeedSum * 1.0 / agentCounter;
    }

    private void calculateVisions() {
        avgHerbVisionRange = 0;
        maxHerbVisionRange = 0;
        int herbVisionSum = 0;
        avgAgentVisionRange = 0;
        maxAgentVisionRange = 0;
        int agentVisionSum = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof MovingFood) {
                        int vision = ((MovingFood) go).getVisibility();
                        herbVisionSum += vision;
                        if (vision > maxHerbVisionRange) {
                            maxHerbVisionRange = vision;
                        }
                    } else if (go instanceof Agent) {
                        int vision = ((Agent) go).getDna().visibility;
                        agentVisionSum += vision;
                        if (vision > maxAgentVisionRange) {
                            maxAgentVisionRange = vision;
                        }
                    }
                }
            }
        }
        avgHerbVisionRange = herbVisionSum * 1.0 / herbivoreCounter;
        avgAgentVisionRange = agentVisionSum * 1.0  / agentCounter;
    }

    private void calculateGenerations() {
        avgGeneration = 0;
        maxGeneration = 0;
        int genSum = 0;
        avgAge = 0;
        maxAge = 0;
        int ageSum = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent) {
                        int gen = ((Agent) go).getDna().generationNo;
                        int age = ((Agent) go).getAge();
                        genSum += gen;
                        ageSum += age;
                        if (gen > maxGeneration) {
                            maxGeneration = gen;
                        }
                        if (age > maxAge) {
                            maxAge = age;
                        }
                    }
                }
            }
        }

        avgGeneration = genSum * 1.0 / agentCounter;
        avgAge = ageSum * 1.0 / agentCounter;
    }

    private void calculateStamina() {
        avgAgentStamina = 0;
        maxAgentStamina = 0;
        int agentStaminaSum = 0;
        avgAgentMaxStamina = 0;
        maxAgentMaxStamina = 0;
        int agentMaxStaminaSum = 0;

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent) {
                        int stamina = ((Agent) go).getStamina();
                        int maxStamina = ((Agent) go).getDna().stamina;
                        agentStaminaSum += stamina;
                        agentMaxStaminaSum += maxStamina;
                        if (stamina > maxAgentStamina) {
                            maxAgentStamina = stamina;
                        }
                        if (maxStamina > maxAgentMaxStamina) {
                            maxAgentMaxStamina = maxStamina;
                        }
                    }
                }
            }
        }

        avgAgentStamina = agentStaminaSum * 1.0 / agentCounter;
        avgAgentMaxStamina = agentMaxStaminaSum * 1.0 / agentCounter;
    }

    // Average calculation is very flawed, needs an efficient way of only counting each group once
    private void calculateGroups() {
        avgGroupMembers = 0;
        maxGroupMembers = 0;
        int groupMemberSum = 0;
        avgGroupSuccess = 0;
        maxGroupSuccess = 0;
        int groupSuccessSum = 0;
        List<AgentKomm> checkedGroups = new ArrayList<>();

        for (int j = 0; j < world[0].length; j++) {
            for (int i = 0; i < world.length; i++) {
                for (GameObject go : world[i][j]) {
                    if (go instanceof Agent) {
                        if (((Agent) go).inGroup()) {
                            AgentKomm group = ((Agent) go).getKomm();
                            if (!checkedGroups.contains(group)) {
                                int members = group.partners.size();
                                int success = ((Agent) go).getSuccess();
                                groupMemberSum += members;
                                groupSuccessSum += success;
                                if (members > maxGroupMembers) {
                                    maxGroupMembers = members;
                                }
                                if (success > maxGroupSuccess) {
                                    maxGroupSuccess = success;
                                }
                                checkedGroups.add(group);
                            }
                        }
                    }
                }
            }
        }

        avgGroupMembers = groupMemberSum * 1.0 / (AgentKomm.cGroups - AgentKomm.cGroupsDissolved);
        avgGroupSuccess = groupSuccessSum * 1.0 / (AgentKomm.cGroups - AgentKomm.cGroupsDissolved);
    }

    private double[] convertArray(Double[] inputArray) {
        double[] doubleArray = new double[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            doubleArray[i] = (double) inputArray[i];
        }
        return doubleArray;
    }

    // TODO: dont hold all values twice; integrate infoData into all calculating methods
    private void updateDataSet() {
        infoData.worldAge = time;
        infoData.numberHills = generator.nHills;
        infoData.numberFood = foodCounter;
        infoData.avgNutriValue = avgHerbEnergy;
        infoData.numberHerbivore = herbivoreCounter;
        infoData.numberAgent = agentCounter;

        infoData.numberParalysedHerbivores = paralyzedHerbivoreCounter;
        infoData.numberDeadHerbivores = deadHerbivoreCounter;
        infoData.numberHerbivoresAlltime = totalHerbivores;
        infoData.avgHerbEnergy = avgHerbEnergy;
        infoData.maxHerbEnergy = maxHerbEnergy;
        infoData.avgHerbFood = avgHerbFood;
        infoData.maxHerbFood = maxHerbFood;
        infoData.avgHerbSpeed = avgHerbSpeed;
        infoData.maxHerbSpeed = maxHerbSpeed;
        infoData.avgHerbVisionRange = avgHerbVisionRange;
        infoData.maxHerbVisionRange = maxHerbVisionRange;
        infoData.numberFemaleAgent = femaleAgentCounter;
        infoData.numberMaleAgent = agentCounter - femaleAgentCounter;
        infoData.numberAttacker = attackerCounter;
        infoData.numberParalyzer = paralyzerCounter;
        infoData.numberLeader = leaderCounter;
        infoData.numberAgentsInGroup = inGroupCounter;
        infoData.numberAgentAlltime = totalAgents;
        infoData.avgGeneration = avgGeneration;
        infoData.maxGeneration = maxGeneration;
        infoData.avgAge = avgAge;
        infoData.maxAge = maxAge;
        infoData.avgAgentSpeed = avgAgentSpeed;
        infoData.maxAgentSpeed = maxAgentSpeed;
        infoData.avgAgentVisionRange = avgAgentVisionRange;
        infoData.maxAgentVisionRange = maxAgentVisionRange;
        infoData.avgAgentStamina = avgAgentStamina;
        infoData.maxAgentStamina = maxAgentStamina;
        infoData.avgAgentMaxStamina = avgAgentMaxStamina;
        infoData.maxAgentMaxStamina = maxAgentMaxStamina;
        infoData.avgAgentFood = avgAgentFood;
        infoData.maxAgentFood = maxAgentFood;
        infoData.avgAgentFoodCapacity = avgAgentFoodCapacity;
        infoData.maxAgentFoodCapacity = maxAgentFoodCapacity;

        infoData.numberGroupsAlltime = AgentKomm.cGroups;
        infoData.numberGroups = AgentKomm.cGroups - AgentKomm.cGroupsDissolved;
        infoData.numberGroupsDissolved = AgentKomm.cGroupsDissolved;
        infoData.maxNumberGroupMembers = AgentKomm.cMaxMembers;
        infoData.avgNumberGroupMembers = AgentKomm.cMembers / (double) AgentKomm.cGroups;
        infoData.maxNumberActiveGroupMembers = maxGroupMembers;
        infoData.avgNumberActiveGroupMembers = avgGroupMembers;
        infoData.maxGroupSuccess = maxGroupSuccess;
        infoData.avgGroupSuccess = avgGroupSuccess;

        infoData.selectedAgent = getAgentById(Long.valueOf((Integer) SwingManager.AgentNumberSpinner.getValue()));
    }
}
