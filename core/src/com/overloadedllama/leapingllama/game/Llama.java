package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Llama extends GameObject {

    public Llama(Texture texture, float x, float  y, float w, float h, World world) {
        super(texture, x, y, w, h, world);

        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);

        // Create a body in the world using our definition
        body = world.createBody(llamaBodyDef);


        // Now define the dimensions of the physics shape
        PolygonShape llamaShape = new PolygonShape();

        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        llamaShape.setAsBox(w/2, h/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others  we will see shortly
        // If you are wondering, density and area are used to calculate over all ass
        FixtureDef llamaFixtureDef = new FixtureDef();
        llamaFixtureDef.shape = llamaShape;
        llamaFixtureDef.density = 985;
        llamaFixtureDef.friction = 1f;
        llamaFixtureDef.restitution = 0f;
        Fixture fixture = body.createFixture(llamaFixtureDef);


        // Shape is the only disposable of the lot, so get rid of it
        llamaShape.dispose();






    }

    public void jump(float hJump) {
        //super.setPosition(getX(), getY() + hJump);
    }


}