package com.overloadedllama.leapingllama.jsonUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.*;


public class LevelParser {
    int levelNumber;

    PriorityQueue<QueueObject> queue;
    ArrayList<Double> enemies;
    ArrayList<Double> grounds;
    ArrayList<Double> groundsLen;
    ArrayList<Double> platforms;
    ArrayList<Double> platformsLen;
    ArrayList<Double> platformsII;
    ArrayList<Double> platformsIILen;
    ArrayList<Double> money;
    ArrayList<Double> moneyNum;
    ArrayList<Double> munitions;
    ArrayList<Double> munitionsNum;

    public final String ENEMIES = "enemies";
    public final String MONEY = "money";
    public final String MUNITIONS = "munitions";
    public final String PLATFORM1 = "platformI";
    public final String PLATFORM2 = "platformII";
    public final String GROUND = "grounds";
    String[] actorStrings = {
            "enemies",
            "money",
            "munitions",
            "platformI",
            "platformII",
            "grounds" };

    public final String MONEY_NUM = "moneyNum";
    public final String MUNITIONS_NUM = "munitionsNum";
    public final String PLATFORM1_LEN = "platformILength";
    public final String PLATFORM2_LEN = "platformIILength";
    public final String GROUND_LEN = "groundsLength";
    String[] actorSupportStrings = {
            "moneyNum",
            "munitionsNum",
            "platformILength",
            "platformIILength",
            "groundsLength"  };


    /**
     * First create the arrays from json file game.json, divided into actor and supports, which contains info for relative actor
     * Then, if is not detected any issue, all the elements generate a new QueueObject which is added to the PriorityQueue
     *
     * @param levelNumber the level
     */
    public LevelParser(int levelNumber) {
        this.levelNumber = levelNumber;

        queue = new PriorityQueue<>();

        enemies = new ArrayList<>();
        grounds = new ArrayList<>();
        groundsLen  = new ArrayList<>();
        platforms = new ArrayList<>();
        platformsLen  = new ArrayList<>();
        platformsII = new ArrayList<>();
        platformsIILen = new ArrayList<>();
        money = new ArrayList<>();
        moneyNum = new ArrayList<>();
        munitions = new ArrayList<>();
        munitionsNum  = new ArrayList<>();

        JsonValue root = new JsonReader().parse(Gdx.files.internal("game.json"));

        // get the actors arrays from json file
        for (String actorString : actorStrings) {
            try {
                double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).asDoubleArray();

                for (double d : doublesArray) {
                    switch (actorString) {
                        case GROUND:
                            grounds.add(d);
                            break;
                        case ENEMIES:
                            enemies.add(d);
                            break;
                        case PLATFORM1:
                            platforms.add(d);
                            break;
                        case PLATFORM2:
                            platformsII.add(d);
                            break;
                        case MUNITIONS:
                            munitions.add(d);
                            break;
                        case MONEY:
                            money.add(d);
                            break;
                    }
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("String '" + actorString + "' doesn't match in json file.");
            }
        }

        for (String actorSupportString : actorSupportStrings) {
            try {
                double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorSupportString).asDoubleArray();
                for (double d : doublesArray) {
                    switch (actorSupportString) {
                        case GROUND_LEN:
                            groundsLen.add(d);
                            break;
                        case PLATFORM1_LEN:
                            platformsLen.add(d);
                            break;
                        case PLATFORM2_LEN:
                            platformsIILen.add(d);
                            break;
                        case MUNITIONS_NUM:
                            munitionsNum.add(d);
                            break;
                        case MONEY_NUM:
                            moneyNum.add(d);
                            break;
                    }
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("String '" + actorSupportString + "' doesn't match in json file.");
            }
        }


        if (    moneyNum.size() != money.size() ||
                munitionsNum.size() != munitions.size() ||
                grounds.size() != groundsLen.size() ||
                platformsIILen.size() != platformsII.size() ||
                platforms.size() !=  platformsLen.size()) {
            throw new IllegalArgumentException();
        }

        for (String actorString : actorStrings) {
            ArrayList<Double> array = getActorArray(actorString);

            if (array == null)
                continue;

            if (isBasicArray(actorString)) {
                for (Double aDouble : array) {
                    queue.add(new QueueObject(actorString, aDouble, -1, -1));
                }
            } else {
                ArrayList<Double> arraySupport = getSupportArray(actorString);
                if (arraySupport == null)
                    throw new RuntimeException();

                for (int i = 0; i < array.size(); ++i) {
                    if (hasLength(actorString)) {
                        queue.add(new QueueObject(actorString, array.get(i), arraySupport.get(i), -1));
                    } else {
                        queue.add(new QueueObject(actorString, array.get(i), -1, arraySupport.get(i).intValue()));
                    }
                }
            }
        }

    }

    private ArrayList<Double> getActorArray(String actorString) {
        switch (actorString) {
            case ENEMIES:
                return enemies;
            case GROUND:
                return grounds;
            case MONEY:
                return money;
            case MUNITIONS:
                return munitions;
            case PLATFORM1:
                return platforms;
            case PLATFORM2:
                return platformsII;
        }
        return null;
    }

    private ArrayList<Double> getSupportArray(String actorString) {
        switch (actorString) {
            case GROUND:
                return groundsLen;
            case MONEY:
                return moneyNum;
            case MUNITIONS:
                return munitionsNum;
            case PLATFORM1:
                return platformsLen;
            case PLATFORM2:
                return platformsIILen;
        }
        return null;
    }

    private boolean hasLength(String actorString) {
        return actorString.equals("grounds") || actorString.equals("platformI") || actorString.equals("platformII");
    }

    private boolean isBasicArray(String actorString) {
        return actorString.equals("enemies");
    }

    // should be checked if the queue isn't empty?
    public PriorityQueue<QueueObject> getQueue() {
        return queue;
    }
}
