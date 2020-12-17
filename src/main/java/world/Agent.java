package world;

import world.enums.AgentActions;
import world.enums.AgentSpecial;
import world.enums.AgentStates;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    //private MovingFood targetToHunt;
    private Agent agentToRecruit;
    //private Portal portal;

    private int amountToEat = 0;
    private int dirX;
    private int dirY;
    private int averageSuccess = 0;
    private int avSuccessPerTime = 0;
    private long startHuntTime;

    // TODO: Write the AgentKomm class
    private AgentKommm komm;

    private List<GameObject> environment;

    private Agent reproductionPartner;

    private Random random;

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
        initAgent(/*null*/);
        cAgentNo++;
    }

    protected void initAgent(/*Portal from*/) {
        //KQML.register(this, null);
        //komm = new AgentKomm(this);
        random = new Random();
        environment = new LinkedList<GameObject>();
        //komm.UpdateListener();
        //portal = from;
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

    public AgentKommm getKomm() {
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
        if (need != currentNeeds) {
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
}
