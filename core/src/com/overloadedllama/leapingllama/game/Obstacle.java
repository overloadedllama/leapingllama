package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;

import java.util.Random;

public class Obstacle extends GameObject{

    public Obstacle(float x, float y, float h, World world, Batch batch, Assets assets){
        super(assets.getTexture("alienOrange"), x, y, h, world, batch);


        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.KinematicBody, enemyShape, 20, 1f, 0f);

        enemyShape.dispose();

        body.setLinearVelocity(-3f, 5f);

    }

}
