package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import org.w3c.dom.Text;

public class Llama extends GameObject {

    boolean isStanding;

    final Texture llamaStanding = new Texture(Gdx.files.internal("llamaStanding.png"));
    final Texture llamaPunching = new Texture(Gdx.files.internal("llamaPunching.png"));
    final Texture llamaCrouching = new Texture(Gdx.files.internal("llamaCrouching.png"));

    public Llama(float x, float  y, float h, World world, Batch batch) {
        super(new Texture(Gdx.files.internal("llamaStanding.png")), x, y, h, world, batch);



        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);


        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1100, 0.05f, 0);

        llamaShape.dispose();

        isStanding = true;
    }

    public Llama(float x, float  y, float h, World world, Batch batch, Texture texture) {
        super(texture, x, y, h, world, batch);

        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);


        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1100, 0.05f, 0);

        llamaShape.dispose();
        isStanding = true;
    }

    public boolean isStanding() { return isStanding; }
    public void setStanding(boolean value) { isStanding = value; }

    public void jump() {
        body.applyForceToCenter(0, 1000000, true);
        System.out.println("jumping");
    }


    public void crouch() {
/*
        PolygonShape crouchLlamaShape = new PolygonShape();
        crouchLlamaShape.setAsBox(w/2, h/2);

        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.shape = crouchLlamaShape;
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;

        this.body.destroyFixture(this.getBody().getFixtureList().first());

        body.createFixture(newFixtureDef).setUserData(this);

        crouchLlamaShape.dispose();

        setSprite(new Sprite(new Texture("llamaCrouching.png")));
        sprite.setSize(sprite.getWidth(), sprite.getHeight() / 2);
*/
    }



    public void punch() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w/2+0.1f, h/2);

        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.shape = shape;
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;

        this.body.destroyFixture(this.getBody().getFixtureList().first());

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

        sprite.set(new Sprite(llamaPunching));
        sprite.setSize(w, h);
    }

    public void depunch() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w/2, h/2);

        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.shape = shape;
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;

        this.body.destroyFixture(this.getBody().getFixtureList().first());

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

        sprite.set(new Sprite(llamaStanding));
        sprite.setSize(w, h);
    }
}