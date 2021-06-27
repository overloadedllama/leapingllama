package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GameObject {

    float x, y, w, h;
    Texture texture;
    Sprite sprite;
    Body body;
    World world;
    Batch batch;


    public GameObject(Texture texture, float x, float y, float w, float h, World world, Batch batch) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.world = world;
        this.texture = texture;
        this.batch = batch;

        sprite = new Sprite(this.texture);
        sprite.setSize(w, h);
        setPosition(x,y);

    }

    public void draw() {

        sprite.draw(batch);
    }

    public void setPosition (float x, float y){
        this.x = x;
        this.y = y;

        sprite.setPosition(x-w/2,y-h/2);
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

    public World getWorld() {
        return world;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}

