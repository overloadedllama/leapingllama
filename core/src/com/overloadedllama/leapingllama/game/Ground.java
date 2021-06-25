package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

public class Ground extends GameObject{

    public Ground(Texture texture, float x, float y, float width, float height, World world) {
        super(texture, x, y, width, height, world);


        // Now create a BodyDefinition. This defines the physics objects type and position in the simulation
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(x,y);

        body = world.createBody(groundBodyDef);



        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(w/2, h/2);
        body.createFixture(groundShape, 0f);

        groundShape.dispose();

    }
}
