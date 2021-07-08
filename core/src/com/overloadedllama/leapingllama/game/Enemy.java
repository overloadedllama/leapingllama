package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;

import java.util.Random;

public class Enemy extends GameObject{

    public Enemy(float x, float y, float h, World world, Batch batch, Assets assets){
        super(assets.getTexture("alienCyan"), x, y, h, world, batch);

        Random random = new Random();
        int randomNumber = random.nextInt(2);

        if(randomNumber == 1){
            texture = assets.getTexture("alienOrange");
            sprite.setTexture(texture);
        }

        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, enemyShape, 20, 1f, 0f);

        enemyShape.dispose();

        body.setLinearVelocity(-3f, 5f);

    }

}