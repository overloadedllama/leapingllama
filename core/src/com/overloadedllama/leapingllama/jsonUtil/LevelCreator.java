package com.overloadedllama.leapingllama.jsonUtil;

import java.util.ArrayList;
import java.util.Random;


public class LevelCreator {

    private ArrayList<Boolean> enemies;
    private ArrayList<Boolean> obstacles;
    private ArrayList<Boolean> grounds;
    private ArrayList<Boolean> platformsI;
    private ArrayList<Boolean> platformsII;

    private ArrayList<Boolean> money;
    private ArrayList<Boolean> ammo;

    private ArrayList<Integer> moneyQty;
    private ArrayList<Integer> ammoQty;

    private int level;


    private int distanceMax;
    private int distance = 0;
    private double distanceTmp;


    private ArrayList<Double> listEnemies = new ArrayList<>();
    private ArrayList<Double> listObstacles = new ArrayList<>();
    private ArrayList<Double> listGrounds = new ArrayList<>();
    private ArrayList<Double> listPlatformsI = new ArrayList<>();
    private ArrayList<Double> listPlatformsII = new ArrayList<>();

    private ArrayList<Double> listGroundsLength = new ArrayList<>();
    private ArrayList<Double> listPlatformsILength = new ArrayList<>();
    private ArrayList<Double> listPlatformsIILength = new ArrayList<>();

    private ArrayList<Double> listMoney = new ArrayList<>();
    private ArrayList<Double> listAmmo = new ArrayList<>();

    private ArrayList<Integer> listMoneyNum = new ArrayList<>();
    private ArrayList<Integer> listAmmoNum = new ArrayList<>();


    public LevelCreator(int level) {
        this.level = level;
        createLevel(level);


    }



    private void createLevel(int level) {
        enemies = new ArrayList<>();

        grounds = new ArrayList<>();
        platformsI = new ArrayList<>();
        platformsII = new ArrayList<>();

        money = new ArrayList<>();
        ammo = new ArrayList<>();

        moneyQty = new ArrayList<>();
        ammoQty = new ArrayList<>();

        obstacles = new ArrayList<>();


        distanceMax = 50+50*level;

        Random random = new Random();


        //randomization of platform I
        distance = 0;
        while (distance<distanceMax){
            //space
            random = new Random();
            distanceTmp = 10 + random.nextInt(10);
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                platformsI.add(Boolean.FALSE);
            }

            //length
            random = new Random();
            distanceTmp = 2 + random.nextInt(4);
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                platformsI.add(Boolean.TRUE);
            }

        }

        //checking that platform starts at at least 10 m
        for (int d = 0; d<10; d++){
            platformsI.add(d, false);
        }


        //randomization of platform II
        distance = 0;
        while (distance<distanceMax){
            //space
            distanceTmp = 50 + random.nextInt(50);
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                platformsII.add(Boolean.FALSE);
            }

            //length
            distanceTmp = random.nextInt(2) + 1;
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                platformsII.add(Boolean.TRUE);
            }

        }

        //checking that platform starts at at least 10 m
        for (int d = 0; d<10; d++){
            platformsII.add(d, false);
        }

        //checking that under each start of a platformII there is a platform I


        for (int d = 3; d<distanceMax; d++){
            if (platformsII.get(d)){
                platformsI.add(d-1, Boolean.TRUE);
                platformsI.add(d-2, Boolean.TRUE);

            }
        }

        //randomization of grounds
        distance = 0;
        while (distance<distanceMax){
            //space
            distanceTmp = 5+random.nextInt(10);
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                grounds.add(Boolean.FALSE);
            }

            //length
            distanceTmp = 2+random.nextInt(20);
            distance+=distanceTmp;
            for (int d = 0; d<distanceTmp; d++) {
                grounds.add(Boolean.TRUE);
            }


        }

        //checking that there's ground where there isn't any platform

        distance = 0;

        for (int d = 0; d<distanceMax; d++){
            if (!platformsI.get(d)){
                grounds.add(d, Boolean.TRUE);
            }
        }

        //randomization of enemies

        distance = 0;

        for (int d = 0; d<distanceMax; d++){
            if (random.nextDouble()>0.95){
                enemies.add(true);
            }
            else
                enemies.add(false);

        }

        //randomization of obstacles
        for (int d = 0; d<distanceMax; d++){
            if (random.nextDouble()>0.95){
                obstacles.add(true);
            }
            else
                obstacles.add(false);

        }


        //randomization of money

        for (int d = 0; d<distanceMax; d++){
            if (random.nextDouble()>0.97){
                money.add(true);
                moneyQty.add(random.nextInt(2)+1);
            }
            else
                money.add(false);

        }

        //checking that money starts at at least 3 m
        for (int d = 0; d<3; d++){
            money.add(d, false);
        }

        //randomization of ammo

        for (int d = 0; d<distanceMax; d++){
            if (random.nextDouble()>0.97){
                ammo.add(true);
                ammoQty.add(random.nextInt(5)+1);
            }
            else
                ammo.add(false);

        }

        //checking that ammo starts at at least 3 m
        for (int d = 0; d<3; d++){
            ammo.add(d, false);
        }

        //System.out.println(level);
        //System.out.println(enemies);
        //System.out.println(grounds);
        //System.out.println(platformsI);
        //System.out.println(platformsII);

        parsing(level, distanceMax);

    }




    private void parsing(int level, int levelLength) {





        parseArray(enemies, listEnemies);
        parseArray(obstacles, listObstacles);
        parseArray(money, listMoney);
        parseArray(ammo, listAmmo);

        parseArray(grounds, listGrounds, listGroundsLength);
        parseArray(platformsI, listPlatformsI, listPlatformsILength);
        parseArray(platformsII, listPlatformsII, listPlatformsIILength);

        listAmmoNum.addAll(ammoQty);
        listMoneyNum.addAll(moneyQty);
    }

    private void parseArray(ArrayList<Boolean> booleans, ArrayList<Double> positions, ArrayList<Double> lengths) {
        distance = 0;
        double counter = 0.0;
        double position = (double) distance;
        boolean isEmpty = false;

        while (distance<distanceMax){

            if(!isEmpty){
                if (booleans.get(distance)){
                    counter++;
                }else{
                    isEmpty=true;

                    if (counter>0){
                        positions.add(littleRandom(position));

                        lengths.add(littleRandom(counter));
                    }

                    counter=0;

                }
            }else{
                if (booleans.get(distance)){
                    isEmpty = false;
                    counter++;
                    position = (double) distance;
                }
            }

            distance++;

        }

    }

    private void parseArray(ArrayList<Boolean> booleans, ArrayList<Double> positions) {
        distance = 0;

        while (distance<distanceMax){

            if(booleans.get(distance)){
                positions.add(littleRandom(distance));
            }

            distance++;

        }

    }

    private Double littleRandom(double position) {
        Random random = new Random();
        //todo return (double) Math.round((position - 0.5 + random.nextDouble())*100) / 100;
        return  position;
    }


    public ArrayList<Double> getListEnemies() {
        return listEnemies;
    }

    public ArrayList<Double> getListObstacles() {
        return listObstacles;
    }

    public ArrayList<Double> getListGrounds() {
        return listGrounds;
    }

    public ArrayList<Double> getListPlatformsI() {
        return listPlatformsI;
    }

    public ArrayList<Double> getListPlatformsII() {
        return listPlatformsII;
    }

    public ArrayList<Double> getListGroundsLength() {
        return listGroundsLength;
    }

    public ArrayList<Double> getListPlatformsILength() {
        return listPlatformsILength;
    }

    public ArrayList<Double> getListPlatformsIILength() {
        return listPlatformsIILength;
    }

    public ArrayList<Double> getListMoney() {
        return listMoney;
    }

    public ArrayList<Double> getListAmmo() {
        return listAmmo;
    }

    public ArrayList<Integer> getListMoneyNum() {
        return listMoneyNum;
    }

    public ArrayList<Integer> getListAmmoNum() {
        return listAmmoNum;
    }


    public int getDistanceMax() {
        return distanceMax;
    }
}
