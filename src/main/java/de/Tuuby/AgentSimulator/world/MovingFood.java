package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.graphics.Animation;
import de.Tuuby.AgentSimulator.graphics.Graphics;
import de.Tuuby.AgentSimulator.resource.ImageResource;

import java.util.Vector;

public class MovingFood extends MovingItem {

    public static int cQuarryNo;

    public static double HEALTH_MUTATION = 0.05;
    public static double REGEN_MUTATION = 0.1;
    //public static double ENERGY_MUTATION = 0.1;
    public static double SPEED_MUTATION = 0.04;
    public static double VISIB_MUTATION = 0.06;

    public static int HEALTH_MIN = 20;
    public static int HEALTH_MAX = 30;
    public static int REGEN_MIN = 8;
    public static int REGEN_MAX = 10;
    public static int FOOD_MIN = 1800;
    public static int FOOD_MAX = 2900;
    public static int ENERGY_MIN = 20000;
    public static int ENERGY_MAX = 28000;
    public static int SPEED_MIN = 60;
    public static int SPEED_MAX = 86;
    public static int VISIB_MIN = 200;
    public static int VISIB_MAX = 400;
    public static int REPROD_TIME_MIN = 7000;
    public static int REPROD_TIME_MAX = 10000;
    public static int LIFE_TIME_MIN = 22500;
    public static int LIFE_TIME_MAX = 30000;
    public static int FOOD_INPUT_RATE = 15;
    public static int FOOD_CONVERSION_RATE = 4;
    public static int ENERGY_DECAY = 60;
    public static int DECAY_START = 200;
    public static int ENVIRON_UPDATE_PERIOD = 500;
    public static int CROWD_PENALTY = 10;
    public static int DANGER_DIST = 30;
    public static int PARALYZE_TIME = 200;

    protected int health;
    protected int maxHealth;
    protected int regenRate;
    protected int speedInc;
    protected int food;
    protected int energy;
    protected int visibility;
    protected int homeDX;
    protected int homeDY;
    protected int orgSpeed;

    private final long lifeTime;
    private long reprodTime;
    private long deathTime;
    private long lastUpdate;
    private long lastEnvironUpdate;
    private long lastRegeneration;

    private Food target = null;
    private Vector<GameObject> environment;

    private static Class foodClass;
    private static Class agentClass;

    static {
        try {
            foodClass = Class.forName("de.Tuuby.AgentSimulator.world.Food");
            agentClass = Class.forName("de.Tuuby.AgentSimulator.world.Agent");
        } catch (ClassNotFoundException e) {
            System.out.println("Error initializing mMovingFOod: " + e);
        }
    }

    public static int acRangeNatDeath = 0;
    public static int acNatDeath = 0;
    public static int acRangeDeath;
    public static int acDeath = 0;
    private boolean natural = false;

    public static void initRun() {
        cQuarryNo = 1;
    }

    public MovingFood(int initX, int initY, World w, int startHealth, int startRegenRate,
                      int startFood, int startEnergy, int startSpeed, int startVisibility) {
        super(initX, initY, w, startSpeed);
        health = startHealth;
        maxHealth = startHealth;
        regenRate = startRegenRate;
        food = startFood;
        energy = startEnergy;
        orgSpeed = startSpeed;
        speedInc = 0;
        visibility = startVisibility;
        setHomeDir();
        lastUpdate = world.getTime();
        lastEnvironUpdate = lastUpdate - ENVIRON_UPDATE_PERIOD - 1;
        lastRegeneration = lastUpdate;
        reprodTime = lastUpdate + randomAttrib(REPROD_TIME_MIN, REPROD_TIME_MAX) / 2;
        lifeTime = lastUpdate = randomAttrib(LIFE_TIME_MIN, LIFE_TIME_MAX);
        cQuarryNo++;
        animations = new Animation[1];
        animations[0] = new Animation();
        animations[0].frames = new ImageResource[1];
        animations[0].frames[0] = new ImageResource("/GameObjects/MovingFood.png");
    }

    public MovingFood(int initX, int initY, World w) {
        this(initX, initY, w,
                randomAttrib(HEALTH_MIN, HEALTH_MAX),
                randomAttrib(REGEN_MIN, REGEN_MAX),
                randomAttrib(FOOD_MIN, FOOD_MAX),
                randomAttrib(ENERGY_MIN, ENERGY_MAX),
                randomAttrib(SPEED_MIN, SPEED_MAX),
                randomAttrib(VISIB_MIN, VISIB_MAX));
    }

    private static int randomAttrib(int min, int max) {
        return (int)(Math.random() * (max - min) + 0.5) + min;
    }

    public String getType() {
        return "quarry";
    }

    public boolean extendsTo(int dx, int dy) {
        return dx == 0 && dy == 0;
    }

    public void update(long time) {
        int dt = (int) (time - lastUpdate);

        if (dt == 0)
            return;

        lastUpdate = time;
        if (isAlive()) {
            food -= dt / 5;
            if (speedInc > 0) {
                orgSpeed += dt;
                speedInc -= dt;
                if (speedInc <= 0) {
                    orgSpeed += speedInc;
                    speedInc = 0;
                }
            }
            speed = orgSpeed - energy / 5000;

            if (time - lastEnvironUpdate > ENVIRON_UPDATE_PERIOD) {
                environment = world.getEnvironment(x, y, visibility);
                lastEnvironUpdate = time;
            }

            if (time - lastRegeneration > 1000 / regenRate) {
                lastRegeneration = time;
                if (health < maxHealth)
                    health++;
            }

            if (time > lifeTime) {
                natural = true;
                decreaseHealth(100);
                return;
            } else if (target != null) {
                if (reachable(target)) {
                    GameObject enemy = getObject(agentClass);
                    if (enemy != null && world.distance(x - enemy.getX(), y - enemy.getY()) < DANGER_DIST) {
                        environment.remove(target);
                        target = null;
                        move(dt);
                    } else
                        eat (dt);
                } else
                    move(dt);
            } else {
                target = (Food)getObject(foodClass);
                move(dt);
            }
            if (food > 0) {
                int de = FOOD_CONVERSION_RATE * dt / 10;
                food -= de;
                energy += de;
                if (time >= reprodTime) {
                    int nquarry = 0;
                    for (GameObject go : environment) {
                        if (go instanceof MovingFood)
                            nquarry++;
                    }
                    if (Math.random() < (double) CROWD_PENALTY / (nquarry * nquarry * nquarry))
                        reproduce();

                    setReprodTime();
                }
            } else {
                food = 0;
                decreaseHealth(1);
            }
        } else if (time - deathTime > DECAY_START) {
            energy -= ENERGY_DECAY * dt / 10;
            if (energy < 0)
                world.removeObject(this);
        }
    }

    private GameObject getObject(Class t) {
        GameObject go = null;
        float min_dist2 = Float.MAX_VALUE;
        for (GameObject envObject : environment) {
            if (envObject.getClass() == t) {
                float dx = world.getD1(envObject.getX(), x);
                float dy = world.getD1(envObject.getY(), y);
                float dist2 = dx * dx + dy * dy;
                if (dist2 < min_dist2) {
                    min_dist2 = dist2;
                    go = envObject;
                }
            }
        }
        return go;
    }

    private void eat(int dt) {
        int en = target.getEnergy();
        if (en > dt * FOOD_INPUT_RATE) {
            en = dt * FOOD_INPUT_RATE;
            target.removeEnergy(en);
        } else {
            target.removeEnergy(en);
            target = null;
        }
        food += en;
    }

    private void reproduce() {
        energy /= 2;
        food /= 2;
        int newHealth = mutated(health, HEALTH_MUTATION);
        int newRegen = mutated(regenRate, REGEN_MUTATION);
        int newFood = food;
        int newEnergy = energy;
        int newSpeed = mutated(orgSpeed + speedInc, SPEED_MUTATION);
        int newVisib = mutated(visibility, VISIB_MUTATION);
        if (newEnergy > 0) {
            MovingFood clone = new MovingFood(x, y, world, newHealth, newRegen, newFood, newEnergy, newSpeed, newVisib);
            world.addObject(clone);
        }
    }

    public static int smaller = 0;
    public static int equal = 0;
    public static int bigger = 0;

    private int mutated(int attrib, double factor) {
        int na = (int)(attrib * (1.0 + factor * (Math.random() - 0.5)) + 0.5);
        if (na < attrib)
            smaller++;
        else if (na > attrib)
            bigger++;
        else
            equal++;
        return na;
    }

    private void move(int dt) {
        int dx;
        int dy;
        if (target != null) {
            dx = target.getX() - x;
            dy = target.getY() - y;
        } else {
            dx = homeDX;
            dy = homeDY;
        }

        int dist = updatePosition(dx, dy, dt);
        if (speed > 0 && dist == 0) {
            if (target != null) {
                environment.remove(target);
                target = null;
            } else
                setHomeDir();
        } else
            food -= dist;
    }

    private void setReprodTime() {
        reprodTime = lastUpdate = randomAttrib(REPROD_TIME_MIN, REPROD_TIME_MAX);
    }

    private void setHomeDir() {
        homeDX = (int)(Math.random() * 20) - 10;
        if (homeDX < 0)
            homeDX -= 50;
        else
            homeDX += 50;

        homeDY = (int)(Math.random() * 20) - 10;
        if (homeDY < 0)
            homeDY -= 50;
        else
            homeDY += 50;
    }

    private void objectRemovedFromWorld(GameObject go) {
        if (environment != null)
            environment.remove(go);

        if (target == go)
            target = null;
    }

    private boolean visible(GameObject go) {
        return world.visible(this, go, visibility);
    }

    private boolean reachable(GameObject go) {
        return world.visible(this, go, 2);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void decreaseHealth(int dh) {
        if (health > 0) {
            health -= dh;
            if (health <= 0) {
                if (natural) {
                    acRangeNatDeath += visibility;
                    acNatDeath++;
                } else {
                    acRangeDeath += visibility;
                    acDeath++;
                }
                deathTime = lastUpdate;
                health = 0;
                energy += food;
                food = 0;

            }
        }
        natural = false;
    }

    public int getFood() {
        return food;
    }

    public int getEnergy() {
        return energy;
    }

    public void removeEnergy(int de) {
        energy -= de;
        if (energy <= 0)
            world.removeObject(this);
    }

    public void paralyze() {
        if (orgSpeed > 0 && isAlive()) {
            speedInc += orgSpeed + PARALYZE_TIME;
            orgSpeed = -PARALYZE_TIME;
            reprodTime += PARALYZE_TIME;
            lastRegeneration += PARALYZE_TIME;
        }
    }

    public boolean isParalyzed() {
        return orgSpeed <= 0;
    }

    public String getStat() {
        float dist = (target != null ? world.distance(target.getX()-x, target.getY()-y) : 0);
        return "Schwärmer " + toString() +
                "\nx: "+ x + "  y: " + y +
                "\nGesundheit: " + health +
                "\nRegenerationsrate: " + regenRate +
                "\nNahrung: " + food +
                "\nEnergie: " + energy +
                "\nGeschwindigkeit: " + speed +
                "\nSichtradius: " + visibility +
                "\nZiel: " + target + "  Distanz: " + dist +
                "\nletzte Aktualisierung: " + lastEnvironUpdate +
                "\nnächste Reproduktion: " + reprodTime;
    }

    public Object getAttrib(String name) throws NoSuchFieldException, IllegalAccessException {
        return getClass().getDeclaredField(name).get(this);
    }

    public void setAttrib(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        getClass().getDeclaredField(name).set(this, value);
    }

    public void render() {
        animations[currentAnimation].play();
        Graphics.setRotation(0);
        Graphics.drawImage(animations[currentAnimation].getImage(), x, y, 20, 20);
    }
}
