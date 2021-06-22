package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class GameObject extends Actor {

        Sprite sprite;

        public GameObject(Texture texture) {
            sprite = new Sprite(texture);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            sprite.draw(batch);
        }

        public void setBounds(int x, int y, int w, int h){
            sprite.setBounds(x, y, w, h);
        }
    }

