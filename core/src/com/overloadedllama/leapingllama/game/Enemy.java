package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class Enemy extends GameObject{

    public Enemy(float x, float y, float h, World world, Batch batch){
        super(new Texture(Gdx.files.internal("alienCyan.png")), x, y, h, world, batch);

        Random random = new Random();
        int randomNumber = random.nextInt(2);

        if(randomNumber == 1){
            texture = new Texture(Gdx.files.internal("alienOrange.png"));
            sprite.setTexture(texture);
        }








        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, enemyShape, 20, 0.01f, 0.4f);


        enemyShape.dispose();

        body.setLinearVelocity(-3f, 5f);

    }

}