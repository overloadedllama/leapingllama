package com.overloadedllama.leapingllama.jsonUtil;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.overloadedllama.leapingllama.LlamaConstants;

import java.util.*;



public class LevelParser implements LlamaConstants {
    int levelNumber;

    int totalCoinsSpawned;
    int totalAmmosSpawned;
    int totalEnemiesSpawned;

    double levelLength;

    PriorityQueue<QueueObject> queue;
    ArrayList<Double> enemies;
    ArrayList<Double> grounds;
    ArrayList<Double> groundsLen;
    ArrayList<Double> platforms;
    ArrayList<Double> platformsLen;
    ArrayList<Double> platformsII;
    ArrayList<Double> platformsIILen;
    ArrayList<Double> coins;
    ArrayList<Double> coinsNum;
    ArrayList<Double> ammos;
    ArrayList<Double> ammoNum;
    ArrayList<Double> obstacles;

    String[] actorStrings = {
            ENEMIES,
            COINS,
            AMMO,
            PLATFORM1,
            PLATFORM2,
            GROUND,
            OBSTACLES
    };

    String[] actorSupportStrings = {
            COINS_NUM,
            AMMO_NUM,
            PLATFORM1_LEN,
            PLATFORM2_LEN,
            GROUND_LEN
    };


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
        coins = new ArrayList<>();
        coinsNum = new ArrayList<>();
        ammos = new ArrayList<>();
        ammoNum  = new ArrayList<>();
        obstacles = new ArrayList<>();

        JsonValue root = new JsonReader().parse(Gdx.files.internal("game.json"));

        // gets the level length
        try {
            levelLength = root.get("levels").get(String.valueOf(levelNumber)).get(LEVEL_LENGTH).asDouble();
        } catch (java.lang.Exception e) {
            System.out.println("String '" + LEVEL_LENGTH + "' doesn't match in json file.");
        }

        // gets the actors arrays from json file, both for actors and supports
        for (String actorString : actorStrings) {
            try {
                double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).asDoubleArray();

                for (double d : doublesArray) {
                    switch (actorString) {
                        case GROUND:
                            grounds.add(d);
                            break;
                        case PLATFORM1:
                            platforms.add(d);
                            break;
                        case PLATFORM2:
                            platformsII.add(d);
                            break;
                        case ENEMIES:
                            enemies.add(d);
                            break;
                        case AMMO:
                            ammos.add(d);
                            break;
                        case COINS:
                            coins.add(d);
                            break;
                        case OBSTACLES:
                            obstacles.add(d);
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
                        case AMMO_NUM:
                            ammoNum.add(d);
                            break;
                        case COINS_NUM:
                            coinsNum.add(d);
                            break;
                    }
                }
            } catch (java.lang.NullPointerException e) {
                System.out.println("String '" + actorSupportString + "' doesn't match in json file.");
            }
        }

/*
        if (    coins.size() != coinsNum.size() ||
                ammos.size() != ammoNum.size() ||
                grounds.size() != groundsLen.size() ||
                platformsII.size() != platformsIILen.size() ||
                platforms.size() !=  platformsLen.size()) {
            throw new IllegalArgumentException();
        }
*/
        queueParser();

        totalAmmosSpawned = ammos.size();
        totalCoinsSpawned = coins.size();
        totalEnemiesSpawned = enemies.size();

    }


    public LevelParser(float difficulty, int levelLength){
        enemies = new ArrayList<>();
        grounds = new ArrayList<>();
        groundsLen  = new ArrayList<>();
        platforms = new ArrayList<>();
        platformsLen  = new ArrayList<>();
        platformsII = new ArrayList<>();
        platformsIILen = new ArrayList<>();
        coins = new ArrayList<>();
        coinsNum = new ArrayList<>();
        ammos = new ArrayList<>();
        ammoNum  = new ArrayList<>();
        obstacles = new ArrayList<>();

        InfRunParser(difficulty, levelLength);
    }

    private void InfRunParser(float difficulty, int levelLength) {

        queue = new PriorityQueue<>();
        LevelCreator levelCreator = new LevelCreator(1, levelLength, 0, difficulty);

        enemies = levelCreator.getListEnemies();
        grounds = levelCreator.getListGrounds();
        groundsLen  = levelCreator.getListGroundsLength();
        platforms = levelCreator.getListPlatformsI();
        platformsLen  = levelCreator.getListPlatformsILength();
        platformsII = levelCreator.getListPlatformsII();
        platformsIILen = levelCreator.getListPlatformsIILength();
        coins = levelCreator.getListMoney();
        obstacles = levelCreator.getListObstacles();
        ammos = levelCreator.getListAmmo();

        coinsNum = new ArrayList<>();
        ammoNum  = new ArrayList<>();

        for (Integer i : levelCreator.getListAmmoNum())
            ammoNum.add((double) i);

        for (Integer i : levelCreator.getListMoneyNum())
            coinsNum.add((double) i);

        queueParser();

        totalAmmosSpawned = ammos.size();
        totalCoinsSpawned = coins.size();
        totalEnemiesSpawned = enemies.size();

    }

    private void queueParser(){
        for (String actorString : actorStrings) {
            ArrayList<Double> array = getActorArray(actorString);

            if (array == null)
                continue;

            if (isBasicArray(actorString)) {
                for (Double aDouble : array) {
                    queue.add(new QueueObject(actorString, aDouble, 0, -1));
                }
            } else {
                ArrayList<Double> arraySupport = getSupportArray(actorString);
                if (arraySupport == null)
                    throw new RuntimeException();

                for (int i = 0; i < array.size(); ++i) {
                    if (hasLength(actorString)) {
                        queue.add(new QueueObject(actorString, array.get(i), arraySupport.get(i), -1));
                    } else {
                        queue.add(new QueueObject(actorString, array.get(i), 0, arraySupport.get(i).intValue()));
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
            case COINS:
                return coins;
            case AMMO:
                return ammos;
            case PLATFORM1:
                return platforms;
            case PLATFORM2:
                return platformsII;
            case OBSTACLES:
                return obstacles;
        }
        return null;
    }

    private ArrayList<Double> getSupportArray(String actorString) {
        switch (actorString) {
            case GROUND:
                return groundsLen;
            case COINS:
                return coinsNum;
            case AMMO:
                return ammoNum;
            case PLATFORM1:
                return platformsLen;
            case PLATFORM2:
                return platformsIILen;
        }
        return null;
    }

    private boolean hasLength(String actorString) {
        return actorString.equals(GROUND) || actorString.equals(PLATFORM1) || actorString.equals(PLATFORM2);
    }

    private boolean isBasicArray(String actorString) {
        return actorString.equals(ENEMIES) || actorString.equals(OBSTACLES);
    }

    // should be checked if the queue isn't empty?
    public PriorityQueue<QueueObject> getQueue() {
        return queue;
    }

    public double getLevelLength() { return levelLength; }

    public double getTotalLevelScore() {
        return levelLength + (totalCoinsSpawned * 30) + (totalAmmosSpawned * 20) + (totalAmmosSpawned * 10);
    }
}
