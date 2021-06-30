package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Llama extends GameObject {


    public Llama(float x, float  y, float h, World world, Batch batch) {
        super(new Texture(Gdx.files.internal("llamaStanding.png")), x, y, h, world, batch);

        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);


        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1100, 0.05f, 0);

        llamaShape.dispose();

    }

    public void jump() {
        body.applyForceToCenter(0, 1000000, true);
        System.out.println("jumping");
    }


}