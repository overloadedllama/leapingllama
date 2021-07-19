package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.assetman.Assets;

public class GameObject implements LlamaConstants {

    float x, y, w, h;
    Texture texture;
    Sprite sprite;
    Body body;
    World world;
    Assets assets;
    Batch batch;
    FixtureDef fixtureDef;
    boolean destroyable;

    public GameObject(Texture texture, float x, float y, float h, World world, Batch batch) {
        this.x = x;
        this.y = y;

        this.h = h;
        this.world = world;
        this.texture = texture;
        this.batch = batch;

        destroyable = false;

        w = h/texture.getHeight()*texture.getWidth();
        sprite = new Sprite(this.texture);
        sprite.setSize(w, h);
        sprite.setOriginCenter();
        setPosition(x,y, 0);
    }

    public GameObject(float x, float y, float h, World world, Batch batch) {
        this.x = x;
        this.y = y;

        this.h = h;
        this.w = h;


        this.world = world;
        this.batch = batch;


        destroyable = false;


    }

    public GameObject(Texture texture, float x, float y, float h, Batch batch) {
        this.x = x;
        this.y = y;

        this.h = h;
        this.texture = texture;
        this.batch = batch;

        destroyable = false;

        w = h/texture.getHeight()*texture.getWidth();

        sprite = new Sprite(this.texture);
        sprite.setSize(w, h);
       // sprite.setOriginCenter();
        sprite.rotate90(true);
        setPosition(x,y, 0);
    }


    public void draw() {
        sprite.draw(batch);
    }

    public void setPosition (float x, float y, float d){
        this.x = x;
        this.y = y;

        sprite.setPosition(x-w/2,y-h/2);
        sprite.setRotation(MathUtils.radiansToDegrees * d);
    }

    public void setPosition (float x, float y){
        this.x = x;
        this.y = y;

        sprite.setPosition(x-w/2,y-h/2);
    }

    public void createBody (BodyDef.BodyType type, Shape shape, float density, float friction, float restitution){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x,y);

        body = world.createBody(bodyDef);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef).setUserData(this);

    }


    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }
    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }
    public void setH(float h) {
        this.h = h;
    }

    public Sprite getSprite() {
        return sprite;
    }
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Body getBody() {
        return body;
    }
    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isDestroyable() { return destroyable; }
    public void setDestroyable(boolean destroyable) { this.destroyable = destroyable; }

    public Texture getTexture() {
        return texture;
    }
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setWorld(World world) {
        this.world = world;
    }
    public World getWorld() {
        return world;
    }

}

