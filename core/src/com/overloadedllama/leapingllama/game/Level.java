package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Queue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//import org.json.simple.*;
//import org.json.simple.parser.*;

public class Level {

    stuff stuff;
    int level;
    HashMap<String, ArrayList<Double>> map;


    ArrayList<Double> enemies;
    ArrayList<Double> obstacles;



    public Level(int level){
        this.level = level;
        map = new HashMap<>();

        // parsing file "JSONExample.json"
       // Object obj = new JSONParser().parse(new FileReader("game.json"));




        switch (level){
            case 1:
            enemies = new ArrayList<Double>(Arrays.asList(1.2, 2.5));
            obstacles = new ArrayList<Double>(Arrays.asList(2.0, 3.5));


        }


        map.put("enemy", enemies);
        map.put("obstacles", obstacles);
    }




    public ArrayList<String> getActor(double distance){
        ArrayList<String> strings = new ArrayList<>();

        for (String s : map.keySet() ){

         //   ArrayList<Double> doubles = map.get(s);
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
