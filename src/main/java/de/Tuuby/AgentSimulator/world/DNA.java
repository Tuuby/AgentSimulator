package de.Tuuby.AgentSimulator.world;

import de.Tuuby.AgentSimulator.world.enums.AgentSpecial;

import java.util.Random;

public class DNA {

    public static double MUTATION_RATE = 0.15;
    public static double MUTATION_EFFECT = 0.35;
    public static double PROBABILITY_MALE = 0.500;

//    public static int MATURITY_MIN = 13;
//    public static int MATURITY_MAX = 16;
//    public static int MIDAGE_MIN = 38;
//    public static int MIDAGE_MAX = 45;

    public static int HIGHAGE_MIN = 26000;
    public static int HIGHAGE_MAX = 33000;

    public static int HEALTH_MIN = 90;
    public static int HEALTH_MAX = 130;
    public static int REGENERATE_MIN = 40;
    public static int REGENERATE_MAX = 80;

    public static int FOOD_MIN = 48000;
    public static int FOOD_MAX = 69000;

    public static int STAMINA_MIN = 2800;
    public static int STAMINA_MAX = 4100;

    public static int REPROD_MIN = 100;
    public static int REPROD_MAX = 500;

    public static int VISIBILITY_MIN = 188;
    public static int VISIBILITY_MAX = 280;

    public static int SPEED_MIN = 40;
    public static int SPEED_MAX = 70;

    public static int SPECIAL_MIN = 0;
    public static int SPECIAL_MAX = 2;

    public boolean male;

    public int generationNo;

    public int maturityAge;
    public int middleAge;
    public int highAge;

    public int health;
    public int regenRate;

    public int foodCapacity;
    public int foodSaturation;

    public int stamina;

    public int reproductInstinct;

    public int visibility;

    public int speed;

    public AgentSpecial special;

    public int preferredNofAttacker;
    public int preferredNofParalyser;

    public int prevHighAge;
    public int prevFoodCap;
    public int prevStamina;
    public int prevReprod;
    public int prevVisib;
    public int prevSpeed;

    private double genomLeader;
    private double genomAttacker;
    private double genomParalyzer;
    private double genomScavenger;

    public DNA(AgentSpecial spec) {
        male = Math.random() < PROBABILITY_MALE;
        highAge = randInRange(HIGHAGE_MIN, HIGHAGE_MAX);
        health = randInRange(HEALTH_MIN, HEALTH_MAX);
        regenRate = randInRange(REGENERATE_MIN, REGENERATE_MAX);
        foodCapacity = randInRange(FOOD_MIN, FOOD_MAX);
        foodSaturation = randInRange(20, 50) * foodCapacity / 100; // 20%-50% of foodCapacity
        stamina = randInRange(STAMINA_MIN, STAMINA_MAX);
        reproductInstinct = randInRange(REPROD_MIN, REPROD_MAX);
        visibility = randInRange(VISIBILITY_MIN, VISIBILITY_MAX);
        speed = randInRange(SPEED_MIN, SPEED_MAX);
        special = spec;
        genomLeader = Math.random();
        genomScavenger = Math.random();
        preferredNofAttacker = randInRange(2, 5);
        preferredNofParalyser = randInRange(2, 5);
        generationNo = 0;
        prevHighAge = 0;
        prevFoodCap = 0;
        prevStamina = 0;
        prevReprod = 0;
        prevVisib = 0;
        prevSpeed = 0;
    }

    public DNA() {
        this(AgentSpecial.ATTACKER);

        double p = Math.random();
        if (p < 0.35)
            special = AgentSpecial.ATTACKER;
        else if (p < 0.8)
            special = AgentSpecial.PARALYZER;
        else
            special = AgentSpecial.LEADER;
    }

    public DNA(DNA dna1, DNA dna2) {
        int prevGenNo = Math.max(dna1.generationNo, dna2.generationNo);
        generationNo = prevGenNo + 1;

        highAge = (dna1.highAge + dna2.highAge) / 2;
        prevHighAge = ((dna1.prevHighAge + dna2.prevHighAge) * prevGenNo / 2 + highAge) / generationNo;
        highAge += randInRange(-2000, 2000);
        highAge = mutate(highAge);
        if (highAge < 1000)
            highAge = 1000;

        health = (dna1.health + dna2.health) / 2;
        health = mutate(health);

        regenRate = (dna1.regenRate + dna2.regenRate) / 2;
        regenRate = mutate(regenRate);

        foodCapacity = (dna1.foodCapacity + dna2.foodCapacity) / 2;
        prevFoodCap = ((dna1.prevFoodCap + dna2.prevFoodCap) * prevGenNo / 2 + foodCapacity) / generationNo;
        foodCapacity = mutate(foodCapacity);
        foodSaturation = randInRange(20, 50) * foodCapacity / 100; // 20-50% of foodCapacity

        stamina = (dna1.stamina + dna2.stamina) / 2;
        prevStamina = ((dna1.prevStamina + dna2.prevStamina) * prevGenNo / 2 + stamina) / generationNo;
        stamina = mutate(stamina);

        reproductInstinct = (dna1.reproductInstinct + dna2.reproductInstinct) / 2;
        prevReprod = ((dna1.prevReprod + dna2.prevReprod) * prevGenNo / 2 + reproductInstinct) / generationNo;
        reproductInstinct = mutate(reproductInstinct);

        visibility = (dna1.visibility+dna2.visibility) / 2;
        prevVisib = ((dna1.prevVisib + dna2.prevVisib) * prevGenNo / 2 + visibility) / generationNo;
        visibility = mutate(visibility);

        speed = (dna1.speed+dna2.speed) / 2; // - foodCapacity/3000; //+ randInRange(-foodCapacity, foodCapacity) / 10;
        prevSpeed = ((dna1.prevSpeed + dna2.prevSpeed) * prevGenNo / 2 + speed) / generationNo;
        speed = mutate(speed);

        genomLeader = (dna1.genomLeader + dna2.genomLeader) / 2;
        genomAttacker = (dna1.genomAttacker + dna2.genomAttacker) / 2;
        genomParalyzer = (dna1.genomParalyzer + dna2.genomParalyzer) / 2;
        genomScavenger = (dna1.genomScavenger + dna2.genomScavenger) / 2;

        double p = Math.random();
        if (p < 0.35)
            special = AgentSpecial.ATTACKER;
        else if (p < 0.8)
            special = AgentSpecial.PARALYZER;
        else
            special = AgentSpecial.LEADER;

        preferredNofAttacker = (dna1.preferredNofAttacker + dna2.preferredNofAttacker) / 2;
        preferredNofAttacker = mutate(preferredNofAttacker);

        preferredNofParalyser = (dna1.preferredNofParalyser+dna2.preferredNofParalyser) / 2;
        preferredNofParalyser = mutate(preferredNofParalyser);
    }

    public static int smaller = 0;
    public static int bigger = 0;
    public static int equal = 0;
    private int mutate(int attribute) {
        int newAttribute = attribute;

        if (Math.random() < MUTATION_RATE) {
            newAttribute = (int)((double)attribute * ((1.0 + MUTATION_EFFECT / 2) - MUTATION_EFFECT * Math.random()));

            if (newAttribute < attribute)
                smaller++;
            else if (newAttribute > attribute)
                bigger++;
            else
                equal++;
        }

        return newAttribute;
    }

    private int randInRange(int min, int max) {
        Random rnd = new Random();
        return min + rnd.nextInt(max - min + 1);
    }

    public boolean switchToLeader() {
        return Math.random() < genomLeader * (special == AgentSpecial.LEADER ? 0.00001 : 0.00004);
    }

    public boolean switchToScavenger() {
        return Math.random() < genomScavenger * (special == AgentSpecial.SCAVENGER ? 0.00001 : 0.00003);
    }

    public Object getAttribute(String name) throws NoSuchFieldException, IllegalAccessException {
        return getClass().getDeclaredField(name).get(this);
    }

    public void setAttribute(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        getClass().getDeclaredField(name).set(this, value);
    }
}
