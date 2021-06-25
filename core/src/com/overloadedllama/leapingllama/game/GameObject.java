package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GameObject extends Actor {

    float x, y, w, h;

    Sprite sprite;

    public GameObject(Texture texture, float x, float y, float width, float height) {
        super();

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;



        super.setSize(width, height);
        sprite = new Sprite(texture);

        super.setOrigin(0,0);
        setPosition(x,y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawing(batch, x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    public void drawing(Batch batch, float x, float y) {
        sprite.setPosition(x,y);
        sprite.draw(batch);
    }




    public void setPosition (float x, float y){
        this.x = x;
        this.y = y;

        sprite.setPosition(x,y);
        sprite.setBounds(x, y, super.getWidth(), super.getHeight());

        super.setPosition(x,y);
    }

}

