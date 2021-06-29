package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends GameObject{

    public Enemy(Texture texture, float x, float y, float w, float h, World world, Batch batch){
        super(texture, x, y, w, h, world, batch);



        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.KinematicBody, enemyShape, 20, 0.2f, 0.7f);


        enemyShape.dispose();

        body.setLinearVelocity(-1f, 0f);

    }

}