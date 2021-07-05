package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;

public class Bullet extends GameObject{
    public Bullet(float x, float y, float h, World world, Batch batch, Assets assets) {
        super(assets.getTexture("base_bullet"), x, y, h, world, batch);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w/2, h/2);

        createBody(BodyDef.BodyType.KinematicBody, shape, 1, 0, 0);

        shape.dispose();

        body.setLinearVelocity(3f, 0f);
        body.setBullet(true);
    }
}
