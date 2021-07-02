package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Llama extends GameObject {

    boolean isStanding;
    boolean isJumping = false;

    final Texture llamaStanding = new Texture(Gdx.files.internal("llamaStanding.png"));
    final Texture llamaPunching = new Texture(Gdx.files.internal("llamaPunching.png"));
    final Texture llamaCrouching = new Texture(Gdx.files.internal("llamaCrouching.png"));

    public Llama(float x, float y, float h, World world, Batch batch) {
        super(new Texture(Gdx.files.internal("llamaStanding.png")), x, y, h, world, batch);


        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);


        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w / 2, h / 2);

        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1100, 0.05f, 0);

        llamaShape.dispose();

        isStanding = true;
    }

    public Llama(float x, float y, float h, World world, Batch batch, Texture texture) {
        super(texture, x, y, h, world, batch);

        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);


        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w / 2, h / 2);

        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1100, 0.05f, 0);

        llamaShape.dispose();
        isStanding = true;
    }

    public void jump() {
        isJumping = true;
        body.applyForceToCenter(0, 1000000, true);
        //System.out.println("jumping");
    }

    public void crouch(Boolean isCrouching) {
        PolygonShape shape = new PolygonShape();
        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;

        if (isCrouching) {
            // then crouches
            h /= 2;

            newFixtureDef.density = fixtureDef.density * 2;

            this.body.setTransform(x, y - h / 2, 0);

            sprite.set(new Sprite(llamaCrouching));

        } else {
            // stands up
            h *= 2;

            this.body.setTransform(x, y + h / 4, 0);

            sprite.set(new Sprite(llamaStanding));
        }

        sprite.setSize(w, h);
        sprite.setPosition(body.getPosition().x, body.getPosition().y); // i'm not sure it works...

        this.body.destroyFixture(this.getBody().getFixtureList().first());

        shape.setAsBox(w / 2, h / 2);
        newFixtureDef.shape = shape;

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

    }

    public void punch(Boolean isPunching) {
        PolygonShape shape = new PolygonShape();
        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;


        if (isPunching) {
            shape.setAsBox(w / 2 + 0.1f, h / 2);

            sprite.set(new Sprite(llamaPunching));
            sprite.setSize(w, h);
        } else {

            shape.setAsBox(w / 2, h / 2);
            sprite.set(new Sprite(llamaStanding));
            sprite.setSize(w, h);
        }

        this.body.destroyFixture(this.getBody().getFixtureList().first());
        newFixtureDef.shape = shape;

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

        sprite.setPosition(x, y);
    }


    public boolean isJumping() { return isJumping; }
    public void setJumping(boolean jumping) { isJumping = jumping; }


    public boolean isStanding() { return isStanding; }
    public void setStanding(boolean value) { isStanding = value; }

}
