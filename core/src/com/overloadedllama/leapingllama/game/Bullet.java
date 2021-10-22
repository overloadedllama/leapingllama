package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class Bullet extends AbstractGameObject {
    public Bullet(float x, float y, float h, LlamaUtil llamaUtil) {
        super(BULLET, x, y, h, llamaUtil);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w/2, h/2);

        createBody(BodyDef.BodyType.KinematicBody, shape, 1, 0, 0);

        shape.dispose();

        body.setLinearVelocity(3f, 0f);
        body.setBullet(true);
    }
}
