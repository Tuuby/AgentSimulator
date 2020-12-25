package world;

import engine.JMTRemnants.Performative;
import engine.KQML;
import world.enums.AgentActions;
import world.enums.AgentSpecial;
import world.enums.AgentStates;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static world.enums.AgentStates.*;

public class Agent extends MovingItem implements IAgent{

    public static int cAgentNo;
    public static int cPortalUses;

    public static int TIME_OF_YEAR = 1000;
    public static int FOOD_INPUT_RATE = 50;
    public static int STAMINA_REGAIN_RATE = 2;
    public static int STAMINA_MIN = 50;
    public static int SPEED_MIN = 2;
    public static int ENVIRON_UPDATE_PERIOD = 500;
    public static int REPROD_PERIOD = 3000;
    public static int REPROD_INHIBITION = 4350;
    public static int CROWD_SIZE = 6;
    public static int CROWD_PENALTY = 9;
    public static int PARALYSE_DIST = 30;
    public static int HUNT_TIME_MAX = 4000;
    public static int TRANSPORTATION_TIME = 60;

    private long lastUpdate = -1000;
    private long lastEnvironUpdate = -1000;
    private long lastAgeUpdate = 0;
    private long lastReproductionTime = 0;
    private long lastPortalUse;

    protected int food;
    protected int foodInput = FOOD_INPUT_RATE;

    protected int stamina;
    protected int staminaRegen = STAMINA_REGAIN_RATE;

    protected int health;
    protected int age;
    protected int staminaMax;
    protected int reproductInstinct;
    protected int visibility;

    protected int reproductions;
    protected int paralysesOrKills;
    protected int portalUses;

    protected static int FOOD_CRITICAL = 4000;

    private DNA dna;

    private AgentSpecial special;
    private AgentActions currentNeed;
    private AgentStates currentState;

    private MovingFood targetToHunt;
    private Agent agentToRecruit;
    private Portal portal;

    private int amountToEat = 0;
    private int dirX;
    private int dirY;
    private int averageSuccess = 0;
    private int avSuccessPerTime = 0;
    private long startHuntTime;

    private AgentKomm komm;

    private List<GameObject> environment;

    private Agent reproductionPartner;

    private Random random;

    private GameObject blocked;
    private long blockTime;

    private int successDir = 0;

    public static void initRun() {
        cAgentNo = 1;
        cPortalUses = 0;
        AgentKomm.initRun();
    }

    public Agent(float x, float y, World w, DNA idna, int ifood) {
        super(x, y, w);
        age = 0;
        dna = idna;
        special = dna.special;
        health = dna.health;
        staminaMax = dna.stamina;
        reproductInstinct = dna.reproductInstinct;
        visibility = dna.visibility;
        food = ifood;
        speed = dna.speed;
        stamina = dna.stamina;
        if (!dna.male)
            staminaRegen++;

        portalUses = 0;
        initAgent(null);
        cAgentNo++;
    }

    protected void initAgent(Portal from) {
        KQML.register(this, null);
        komm = new AgentKomm(this);
        random = new Random();
        environment = new LinkedList<GameObject>();
        komm.updateLeader();
        portal = from;
        long time = world.getTime();
        lastReproductionTime = time + REPROD_PERIOD / 2;
        lastAgeUpdate = time;
        lastPortalUse = time - 2;
        lastUpdate = time - 1;
    }

    public Agent(float x, float y, World w, DNA idna) {
        this(x, y, w, idna, idna.foodSaturation);
    }

    public String getType() {
        return "agent";
    }

    public boolean extendsTo(float dx, float dy) {
        return dx == 0 && dy == 0;
    }

    public DNA getDna() {
        return dna;
    }

    public AgentKomm getKomm() {
        return komm;
    }

    public Object getAttrib(String name) throws NoSuchFieldException, IllegalAccessException
    {
        return getClass().getDeclaredField(name).get(this);
    }

    public void setAttrib(String name, Object value) throws NoSuchFieldException, IllegalAccessException
    {
        getClass().getDeclaredField(name).set(this, value);
    }

    // TODO: add all missing functions and check needs and actions in AgentActions
    public void update(long time) {
        int dt = (int)(time - lastUpdate);

        if (dt == 0)
            return;

        if (lastUpdate >= 0) {
            age += dt;
            updateAge(time);
        }
        lastUpdate = time;

        if (currentState == AgentStates.TRANSPORTING) {
            transport(0);
            return;
        }

        AgentActions need = nextNeed(time);
        if (need != currentNeed) {
            currentNeed = need;
            initState(time);
        }

        AgentActions action = nextAction(time);
        if (action != AgentActions.NONE)
            doAction(action, dt);

        if (--food < 0)
            health--;

        if (health <= 0) {
            world.removeObject(this);
            komm.death();
        }
    }

    public void render() {

    }

    public void setName(String name) {

    }

    public String getName() {
        return null;
    }

    public AgentSpecial getSpecial() {
        return special;
    }

    public AgentActions getNeed() {
        return currentNeed;
    }

    public AgentStates getState() {
        return currentState;
    }

    public void setState(AgentStates state) {
        currentState = state;
    }

    public List<GameObject> getEnvironment() {
        return environment;
    }

    public MovingFood getTarget()
    {
        return targetToHunt;
    }

    public boolean setTarget(MovingFood target, int amount) {
        if (currentState == REPRODUCING2)
            return false;

        if (currentState != REPRODUCING2 && (currentState != EATING || targetToHunt == target || targetIsBetter(target))) {
            currentState = EATING;
            targetToHunt = target;
            //notifyState(special, currentState);
            amountToEat = amount;
            updateSuccess(amountToEat);
            return true;
        } else if (currentState != REPRODUCING2 && stamina > staminaMax / 4) {
            currentState = HUNTING;
            targetToHunt = target;
            startHuntTime = lastUpdate;
            //notifyState(special, currentState);
            return true;
        }
        return false;
    }

    public int getSuccess() {
        return averageSuccess;
    }

    public void setSuccess(int success) {
        averageSuccess = success;
    }

    public void setFoodAmount() {
        int energy = targetToHunt.getEnergy();
        amountToEat = energy / (komm.partners.size() + 1) + 1;
        komm.notifyFoodAmount(targetToHunt, amountToEat);
    }

    public boolean conditionCritical() {
        return currentState != EATING && currentState != HUNTING && food < FOOD_CRITICAL;
    }

    public Agent getLeader() {
        return komm.leader;
    }

    public Agent getReproductionPartner() {
        return reproductionPartner;
    }

    public void initReproduction(Agent partner) {
        reproductionPartner = partner;
        currentState = REPRODUCING2;
        //notifyState(special, currentState);
    }
    public boolean readyForReproduction() {
        int nAgent = 0;
        for (GameObject go : environment) {
            if (go instanceof Agent)
                nAgent++;
        }
        int p = REPROD_INHIBITION;
        if (nAgent > CROWD_SIZE)
            p += CROWD_PENALTY * (nAgent * nAgent * nAgent);

        return currentState != REPRODUCING2 &&
                (currentState == AgentStates.REPRODUCING ||
                stamina > 2 * staminaMax / 5 &&
                (dna.male || food > dna.foodSaturation) &&
                lastUpdate - lastReproductionTime > REPROD_PERIOD &&
                Math.random() * p < reproductInstinct);
    }

    private AgentActions nextNeed(long time) {
        if (stamina < STAMINA_MIN || currentState == AgentStates.SLEEPING && stamina < staminaMax / 2)
            return AgentActions.SLEEP;
        else if (!conditionCritical() && currentState == REPRODUCING2)
            return currentNeed;
        else if (food < dna.foodSaturation || currentState == EATING ||  currentState == HUNTING)
            return AgentActions.EAT;
        else if (stamina < staminaMax / 4)
            return AgentActions.SLEEP;
        else if (readyForReproduction() || currentState == REPRODUCING2)
            return AgentActions.REPRODUCE;

        return AgentActions.EAT;
    }

    private void initState(long time) {
        if (currentNeed == AgentActions.SLEEP) {
            currentState = AgentStates.SLEEPING;
            //notifyState(special, currentState);
        } else if (currentNeed == AgentActions.EAT) {
            currentState = AgentStates.LOOKING;
            //notifyState(special, currentState);
        } else if (currentNeed == AgentActions.REPRODUCE)
            currentState = AgentStates.REPRODUCING;
        else
            currentState = AgentStates.NONE;
    }

    private AgentActions nextAction(long time) {
        MovingFood target;
        if (conditionCritical()) {
            if (time - lastPortalUse > 1000) {
                Portal p = findPortal();
                if (p != null) {
                    currentState = AgentStates.APPROACHING_PORTAL;
                    //notifyState(special, currentState);
                    portal = p;
                }
            }
        }

        switch (currentState) {
            case NONE:
            case SLEEPING:
                return AgentActions.SLEEP;
            case LOOKING:
                currentState = AgentStates.SEARCHING_FOOD;
                return AgentActions.LOOK;
            case HUNTING:
                if (time - startHuntTime > HUNT_TIME_MAX) {
                    currentNeed = AgentActions.NONE;
                    return AgentActions.LOOK;
                } else if (time - lastEnvironUpdate > ENVIRON_UPDATE_PERIOD)
                    return AgentActions.LOOK;
                else
                    return AgentActions.HUNT;
            case EATING:
                return AgentActions.EAT;
            case SEARCHING_FOOD:
                if (time - lastEnvironUpdate > ENVIRON_UPDATE_PERIOD)
                    return AgentActions.LOOK;
                else {
                    if (komm.leader == null) {
                        target = findTarget(true, false);
                        if (target != null) {
                            currentState = EATING;
                            targetToHunt = target;
                            amountToEat = target.getEnergy();
                            //notifyState(special, currentState);
                            return AgentActions.EAT;
                        }
                        target = findTarget(false, true);
                        if (target != null) {
                            if (special == AgentSpecial.ATTACKER && (reachable(target) || target.isParalyzed()) ||
                                special == AgentSpecial.PARALYZER && world.distance(this, target) < PARALYSE_DIST) {
                                targetToHunt = target;
                                return AgentActions.HUNT;
                            }
                        }
                        currentState = AgentStates.SEARCHING_AGENT;
                        //notifyState(special, currentState);
                    } else {
                        target = findTarget(false, true);
                        if (target != null) {
                            if (komm.groupSufficient()) {
                                currentState = HUNTING;
                                targetToHunt = target;
                                startHuntTime = time;
                                komm.startHunt(target);
                                //notifyState(special, currentState);
                                return AgentActions.HUNT;
                            } else {
                                currentState = AgentStates.SEARCHING_AGENT;
                                //notifyState(special, currentState);
                            }
                        } else {
                            komm.getEnvFromPartners(lastEnvironUpdate);
                            //notifyState(special, currentState);
                            return AgentActions.WALK;
                        }
                    }
                }
                break;
            case SEARCHING_AGENT:
                if (time - lastEnvironUpdate > ENVIRON_UPDATE_PERIOD)
                    return AgentActions.LOOK;
                else {
                    if (komm.groupSufficient())
                        currentState = AgentStates.SEARCHING_FOOD;

                    Agent agent = komm.findAgent(special != AgentSpecial.LEADER);
                    if (agent != null) {
                        agentToRecruit = agent;
                        return AgentActions.RECRUIT;
                    } else {
                        komm.getEnvFromPartners(lastEnvironUpdate);
                        return AgentActions.WALK;
                    }
                }
            case REPRODUCING:
                komm.broadcastReprodReq(time);
                return AgentActions.WALK;
            case REPRODUCING2:
                return AgentActions.REPRODUCE;
            case APPROACHING_PORTAL:
                return AgentActions.TRANSPORT;
        }
        return AgentActions.NONE;
    }

    private void doAction(AgentActions action, int dt) {
        komm.handleRequests();
        switch (action) {
            case SLEEP:		sleep(dt); break;
            case LOOK:		look(dt); break;
            case WALK:		walk(dt); break;
            case EAT:		eat(dt); break;
            case RECRUIT:	recruit(); break;
            case HUNT:		hunt(dt); break;
            case REPRODUCE:	reproduce(dt); break;
            case TRANSPORT:	transport(dt); break;
        }
    }

    private void sleep(int dt) {
        stamina += dt * staminaRegen;
        if (stamina > staminaMax)
            stamina = staminaMax;

        if (lastUpdate % 20 == 0)
            food--;
    }

    private void look(int dt) {
        environment = world.getEnvironment(Math.round(x), Math.round(y), visibility);
        lastEnvironUpdate = lastUpdate;
        food--;
    }

    private void walk(int dt) {
        int prevX = Math.round(x);
        int prevY = Math.round(y);
        move(dirX, dirY, dt);
        if (Math.round(x) == prevX && Math.round(y) == prevY)
            setDirection();
    }

    private void hunt(int dt) {
        MovingFood target = targetToHunt;
        if (special == AgentSpecial.ATTACKER) {
            moveToTarget(target, dt);
            if (reachable(target) && target.isAlive()) {
                target.decreaseHealth(10);
                if (!target.isAlive())
                    paralysesOrKills++;
            }
        } else if (special == AgentSpecial.PARALYZER) {
            moveToTarget(target, dt);
            if (world.distance(this, target) < PARALYSE_DIST) {
                if (!target.isParalyzed())
                    paralysesOrKills++;

                target.paralyze();
            }
        } else if (special == AgentSpecial.LEADER) {
            if (!target.isAlive()) {
                setFoodAmount();
                updateSuccess(amountToEat);
                currentState = EATING;
                //notifyState(special, currentState);
            }
        }
        else
            System.out.println("The Agent with the speciality: " + special + "can not hunt.");
    }

    private void updateSuccess(int currSuccess) {
        if (averageSuccess > 0)
            averageSuccess = (averageSuccess + currSuccess) / 2;
        else
            averageSuccess = currSuccess;
    }

    private void eat(int dt) {
         int energy;
         MovingFood target = targetToHunt;
         if (reachable(target)) {
             energy = target.getEnergy();
             if (energy > dt * foodInput) {
                 energy = dt * foodInput;
             } else
                 currentNeed = AgentActions.NONE;
             target.removeEnergy(energy);
             food += energy;
             amountToEat -= energy;
             if (amountToEat < 0)
                 currentNeed = AgentActions.NONE;

             if (food > dna.foodCapacity) {
                 currentNeed = AgentActions.NONE;
                 food = dna.foodCapacity;
             }
         } else {
             int prevX = Math.round(x);
             int prevY = Math.round(y);
             moveToTarget(target, dt);
             if (Math.round(x) == prevX && Math.round(y) == prevY) {
                 if (blocked == target && lastUpdate - blockTime > 100) {
                     environment.remove(target);
                     targetToHunt = null;
                     currentState = AgentStates.SEARCHING_FOOD;
                     //notifyState(special, currentState);
                     blocked = null;
                 } else if (blocked == null) {
                     blocked = target;
                     blockTime = lastUpdate;
                 }
             } else
                 blocked = null;
         }
    }

    private void recruit() {
        food -= 2;
        komm.recruit(agentToRecruit);
    }

    private void reproduce(int dt) {
        if (!reachable(reproductionPartner))
            moveToTarget(reproductionPartner, dt);
        else {
            reproductions++;
            lastReproductionTime = lastUpdate;
            if (!dna.male) {
                food /= 2;
                Agent child = new Agent(x, y, world, new DNA(dna, reproductionPartner.getDna()), food);
                world.addObject(child);
            } else {
                food -= 200;
            }
            stamina -= 20;
            reproductionPartner = null;
            currentNeed = AgentActions.NONE;
            currentState = AgentStates.NONE;
        }
    }

    private void transport(int dt) {
        if (!portal.isFunctionable()) {
            currentNeed = AgentActions.NONE;
            currentState = NONE;
        } else if (!reachable(portal))
            moveToTarget(portal, dt);
        else if (currentState != TRANSPORTING) {
            currentState = TRANSPORTING;
            lastPortalUse = lastUpdate + TRANSPORTATION_TIME;
        } else if (lastUpdate - lastPortalUse >= TRANSPORTATION_TIME) {
            currentState = NONE;
            currentNeed = AgentActions.NONE;
        } else if (lastUpdate == lastPortalUse) {
            environment = new LinkedList<GameObject>();
            reproductionPartner = null;
            targetToHunt = null;
            agentToRecruit = null;
            komm.updateLeader();
            portalUses++;
            cPortalUses++;
            portal.activate(this);
        }
    }

    public void objectRemovedFromWorld(GameObject gameObject) {
        environment.remove(gameObject);
        if ((currentState == HUNTING || currentState == EATING) && targetToHunt == gameObject) {
            targetToHunt = null;
            currentNeed = AgentActions.NONE;
            currentState = AgentStates.NONE;
        } else if (gameObject instanceof Agent) {
            komm.partnerWithdraw((Agent)gameObject);
            if (currentState == REPRODUCING2 && reproductionPartner == gameObject) {
                currentState = AgentStates.NONE;
                currentNeed = AgentActions.NONE;
                reproductionPartner = null;
            }
        }
    }

    private MovingFood findTarget(boolean dead, boolean nearest) {
        float minDist = Integer.MAX_VALUE;
        int maxEnergy = 0;
        MovingFood target = null;
        for (GameObject go : environment) {
            if (go instanceof MovingFood && ((MovingFood)go).isAlive() != dead) {
                if (nearest) {
                    float dist = world.distance(go, this);
                    if (dist < minDist) {
                        minDist = dist;
                        target = (MovingFood)go;
                    }
                } else {
                    int en = ((MovingFood)go).getEnergy();
                    if (en > maxEnergy) {
                        maxEnergy = en;
                        target = (MovingFood)go;
                    }
                }
            }
        }
        return target;
    }

    private Portal findPortal() {
        for (GameObject envItem : environment) {
            if (envItem instanceof Portal) {
                Portal p = (Portal)envItem;
                if (p.isFunctionable())
                    return p;
            }
        }
        return null;
    }

    public void performKQML(IAgent sender, Performative performative) {
        komm.handleRequest(performative);
    }

    public void decreaseHealth(int dh) {
        health -= dh;
        if (health < 0)
            world.removeObject(this);
        else if (targetToHunt != null) {
            environment.remove(targetToHunt);
            targetToHunt = null;
            currentNeed = AgentActions.NONE;
        }
    }

    private boolean targetIsBetter(MovingFood newTarget) {
        float dist1 = world.distance(targetToHunt, this);
        float dist2 = world.distance(newTarget, this);
        int energy1 = targetToHunt.getEnergy();
        int energy2 = newTarget.getEnergy();
        return energy2 - dist2 > energy1 - dist1;
    }

    public void setDirection() {
        int dir = random.nextInt() % 360;
        dirX = (int) (Math.cos(dir / 180.0 * Math.PI) * 100);
        dirY = (int) (Math.sin(dir / 180.0 * Math.PI) * 100);
    }

    public Hashtable<String, Integer> getInfo() {
        Hashtable<String, Integer> info = new Hashtable<String, Integer>();
        info.put("health", health);
        info.put("speed", Math.round(speed));
        info.put("visibility", visibility);
        info.put("stamina", dna.stamina);
        info.put("special", special.getSpecialNumber());
        return info;
    }

    private void moveToTarget(GameObject target, int dt) {
        float dx = target.getX() - x;
        float dy = target.getY() - y;
        if (dx != 0 || dy != 0)
            move(Math.round(dx), Math.round(dy), dt);
    }

    private void move(int dx, int dy, int dt) {
        float dist = updatePosition(dx, dy, dt);
        if (dist == 0) {
            if (successDir == 0) {
                dist = updatePosition(-dy, dx, dt);
                if (dist == 0) {
                    dist = updatePosition(dy, -dx, dt);
                    if (dist != 0)
                        successDir = 1;
                }
            } else {
                dist = updatePosition(dy, -dx, dt);
                if (dist == 0) {
                    dist = updatePosition(-dy, dx, dt);
                    if (dist != 0)
                        successDir = 0;
                }
            }
        }

        if (dist > 0) {
            stamina -= dist;
            food -= dt;
        }
    }

    public void updateAge(long time) {
        int dt = (int)((time - lastAgeUpdate) / TIME_OF_YEAR);
        if (dt > 0) {
            if (age > dna.highAge) {
                for (int i = 0; i < dt; i++) {
                    health -= 15;
                    staminaMax = (staminaMax * 98) / 100;
                    speed = (speed * 99) / 100;
                    visibility = (visibility * 98) / 100;
                    reproductInstinct = (reproductInstinct * 80) / 100;
                }
            }
            lastAgeUpdate = time;
        }
    }

    private boolean visible(GameObject go) {
        return world.visible(this, go, visibility);
    }

    private boolean reachable(GameObject go) {
        return world.visible(this, go, 6);
    }

    public String getStat() {
        float dist;
        if (currentState == HUNTING || currentState == EATING)
            dist = world.distance(targetToHunt, this);
        else if(currentState == REPRODUCING2)
            dist = world.distance(reproductionPartner, this);
        else dist = 0;
        return (dna.male ? "männlicher" : "weiblicher") + " Agent: " + this +
                        "\nx: "+ x + "  y: " + y +
                        "\nNahrungspegel: " + food + " (" + dna.foodCapacity + ")"+
                        "\nAusdauer: " + stamina + " (" + staminaMax + ")" +
                        "\nGeschwindigkeit: " + speed +
                        "\nSichtweite: " + visibility +
                        "\nFortpflanzungstrieb: " + reproductInstinct +
                        "\nSpezialfähigkeit: " + special +
                        "\nErfolg: " + averageSuccess +
                        "\nStatus: " + currentState + "  Bedürfnis: " + currentNeed +
                        "\nEntfernung Ziel: " + dist +
                        "\nGruppenführer: " + (komm.leader != null ? komm.leader.toString() : "---") +
                        "\nPartner: " + komm.partners +
                        "\n\nUmgebung: " + environment;
    }

    public String toString() {
        return getName().substring(getName().indexOf(':') + 1);
    }
}
