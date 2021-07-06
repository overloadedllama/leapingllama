package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;

import java.util.*;



public class LevelParser {

    stuff stuff;
    int levelNumber;
    HashMap<String, ArrayList<Double>> mapActorsSimple;
    HashMap<String, ArrayList<ArrayList<Double>>> mapActorsComplex;

    String [] actorsSimpleStrings = {"enemies", "money", "bullets", "obstacles"};
    String [] actorsComplexStrings = {"platforms", "platformsII", "grounds"};


    public LevelParser(int levelNumber){
        this.levelNumber = levelNumber;

        mapActorsSimple = new HashMap<>();
        mapActorsComplex = new HashMap<>();



        JsonValue root = new JsonReader().parse(Gdx.files.internal("game.json"));


        for (String actorString : actorsSimpleStrings) {
            ArrayList<Double> arrayList = new ArrayList<>();

            double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).asDoubleArray();
            for (double d : doublesArray) {
                arrayList.add(d);
            }

            mapActorsSimple.put(actorString, arrayList);
            System.out.println(arrayList.toString());

        }

        for (String actorString : actorsComplexStrings) {
            ArrayList<ArrayList<Double>> arrayList = new ArrayList<>();

            //double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).asDoubleArray();
            JsonValue.JsonIterator i = root.get("levels").get(String.valueOf(levelNumber)).get(actorString).iterator();
            while (i.hasNext()) {
                ArrayList<Double> tmp = new ArrayList<>();
                JsonValue.JsonIterator ii = i.next().iterator();
                while (ii.hasNext()){
                    tmp.add(ii.next().asDouble());
                }

                System.out.println(tmp);
                arrayList.add(tmp);
            }
            mapActorsComplex.put(actorString, arrayList);
        }

    }




    public ArrayList<String> getActor(double distance){

        /*
        * this method returns the name (strings) of the actors that are in proximity of the given distance.
        * if those actor are complex, that is, they have also another information such as their length, the string returned
        * is composed by the name of the actor minus that information.
        *
         */

        ArrayList<String> strings = new ArrayList<>();

        for (String s : mapActorsSimple.keySet() ){

            Iterator<Double> i = mapActorsSimple.get(s).iterator();

            while (i.hasNext()){
                Double d = i.next();
                if (d<=distance+0.1){
                    strings.add(s);
                    i.remove();
                }

            }
        }

       for (String s : mapActorsComplex.keySet() ){

            Iterator<ArrayList<Double>> i = mapActorsComplex.get(s).iterator();

            while (i.hasNext()){
                ArrayList<Double> d = i.next();
                if (d.get(0)<=distance+0.1){

                    strings.add(s+'-'+String.valueOf(d.get(1)));
                    i.remove();
                }

            }
        }

        return strings;

    }

    public String[] getActorsSimpleStrings() {
        return actorsSimpleStrings;
    }

    public void setActorsSimpleStrings(String[] actorsSimpleStrings) {
        this.actorsSimpleStrings = actorsSimpleStrings;
    }

    public String[] getActorsComplexStrings() {
        return actorsComplexStrings;
    }

    public void setActorsComplexStrings(String[] actorsComplexStrings) {
        this.actorsComplexStrings = actorsComplexStrings;
    }
}
