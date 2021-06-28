package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.*;

public class Ground extends GameObject{

    public Ground(Texture texture, float x, float y, float w, float h, World world, Batch batch) {
        super(texture, x, y, w, h, world, batch);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.StaticBody, groundShape, 20, 0.5f, 0);


        groundShape.dispose();

    }
}
