package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.Queue;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.StringBuilder;
import java.util.*;



public class LevelParser {

    stuff stuff;
    int levelNumber;
    HashMap<String, ArrayList<Double>> map;



    public LevelParser(int levelNumber){
        this.levelNumber = levelNumber;
        map = new HashMap<>();



        JsonValue root = new JsonReader().parse(Gdx.files.internal("game.json"));

        //ArrayList<Double> enemies = new ArrayList<>(Arrays.asList(root.get("levels").get(levelNumber).get("enemies").asDoubleArray()));

        ArrayList<Double> arrayList = new ArrayList<>();

        double[] doublesArray = root.get("levels").get(String.valueOf(levelNumber)).get("enemies").asDoubleArray();
        for (double d : doublesArray) {
            arrayList.add(d);
        }

        map.put("enemy", arrayList);

    }




    public ArrayList<String> getActor(double distance){
        ArrayList<String> strings = new ArrayList<>();

        for (String s : map.keySet() ){

            Iterator<Double> i = map.get(s).iterator();

            while (i.hasNext()){
                Double d = i.next();
                if (Math.abs(d-distance)<=0.1){
                    strings.add(s);
                    i.remove();
                }

            }
        }

        return strings;

    }

}
