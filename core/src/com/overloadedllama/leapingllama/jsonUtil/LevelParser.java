package com.overloadedllama.leapingllama.jsonUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.*;


/**
 * QUEUE
 *
 * gets all the arrays from game.json or wherever they are,
 * now i try with simple double arrays, where even position = x-coordinate, odd position = object length for Ground and Platforms,
 * while odd position = number of items for Money and Munitions
 * Once arrays are built starts Queue filling
 *
 * In the end the queue is completed, and through getQueue() method it is passed to GameScreen
 */

public class LevelParser {

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


    int levelNumber;

    String[] actorStrings = {
            "enemies",
            "money",
            "munitions",
            "platforms",
            "platformsII",
            "grounds" };

    String[] actorSupportStrings = {
            "moneyNum",
            "munitionsNum",
            "platformsLength",
            "platformsIILength",
            "groundsLength"  };

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
            double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).asDoubleArray();
            for (double d : doublesArray) {
                switch (actorString) {
                    case "grounds":
                        grounds.add(d);
                        break;
                    case "enemies":
                        enemies.add(d);
                        break;
                    case "platforms":
                        platforms.add(d);
                        break;
                    case "platformsII":
                        platformsII.add(d);
                        break;
                    case "munitions":
                        munitions.add(d);
                        break;
                    case "money":
                        money.add(d);
                        break;
                }
            }
        }

        for (String actorSupportString : actorSupportStrings) {
            double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorSupportString).asDoubleArray();
            for (double d : doublesArray) {
                switch (actorSupportString) {
                    case "groundsLength":
                        groundsLen.add(d);
                        break;
                    case "platformsLength":
                        platformsLen.add(d);
                        break;
                    case "platformsIILength":
                        platformsIILen.add(d);
                        break;
                    case "munitionsNum":
                        munitionsNum.add(d);
                        break;
                    case "moneyNum":
                        moneyNum.add(d);
                        break;
                }
            }
        }

        if (  moneyNum.size() != money.size() ||
                munitionsNum.size() != munitions.size() ||
                grounds.size() != groundsLen.size() ||
                platformsIILen.size() != platformsII.size() ||
                platforms.size() !=  platformsLen.size()) {
            throw new IllegalArgumentException();
        }

        // for each actor, gets the relative array and check is not null,
        // if its element must be considered as "single value", such as enemies which
        // since now, has just x-coordinate, length and numItem are set -1,
        // else it check how to go on with checking if this actor has length or not
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
            case "enemies":
                return enemies;
            case "grounds":
                return grounds;
            case "money":
                return money;
            case "munitions":
                return munitions;
            case "platforms":
                return platforms;
            case "platformsII":
                return platformsII;
        }
        return null;
    }

    private ArrayList<Double> getSupportArray(String actorString) {
        switch (actorString) {
            case "grounds":
                return groundsLen;
            case "money":
                return moneyNum;
            case "munitions":
                return munitionsNum;
            case "platforms":
                return platformsLen;
            case "platformsII":
                return platformsIILen;
        }
        return null;
    }

    private boolean hasLength(String actorString) {
        return actorString.equals("grounds") || actorString.equals("platforms") || actorString.equals("platformsII");
    }

    private boolean isBasicArray(String actorString) {
        return actorString.equals("enemies");
    }

    // should be checked if the queue isn't empty?
    public PriorityQueue<QueueObject> getQueue() {
        return queue;
    }
}
