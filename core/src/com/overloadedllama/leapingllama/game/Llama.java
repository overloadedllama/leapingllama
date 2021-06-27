package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Llama extends GameObject {


    public Llama(Texture texture, float x, float  y, float w, float h, World world, Batch batch) {
        super(texture, x, y, w, h, world, batch);
        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);

        body = world.createBody(llamaBodyDef);

        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w/2, h/2);

        FixtureDef llamaFixtureDef = new FixtureDef();
        llamaFixtureDef.shape = llamaShape;
        llamaFixtureDef.density = 1;
        llamaFixtureDef.friction = 1f;
        llamaFixtureDef.restitution = 0f;

        body.createFixture(llamaFixtureDef);

        llamaShape.dispose();

    }

    public void jump() {
        body.applyLinearImpulse(0, 9999999999999999999f, x, y, true);
    }


}