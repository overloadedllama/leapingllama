package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameObjectLabel extends GameObject{

    int quantity;
    Label actor;
    Skin skin;
    public GameObjectLabel(Skin skin, float x, float y, float h, int quantity, World world, Batch batch) {
        super(x, y, h, world, batch);
        this.quantity = quantity;
        this.skin = skin;

        actor = new Label(String.valueOf(quantity), skin);

        setPosition(x, y);
        actor.setSize(h/128, h/128);
        actor.setFontScale(0.01f);



    }

    @Override
    public void setPosition (float x, float y) {
        actor.setPosition(x, y);
    }

    @Override
    public void setPosition (float x, float y, float d) {
        actor.setPosition(x, y);
    }

    @Override
    public void draw(){
        actor.draw(batch, 1);
    }



}
