package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;

public class GameObjectLabel extends GameObject{

    int quantity;
    Label actor;
    Skin skin;
    public GameObjectLabel(Skin skin, float x, float y, float h, int quantity, World world, Batch batch, ButtonsStagePlay stage) {
        super(x, y, h, world, batch);
        this.quantity = quantity;
        this.skin = skin;



        actor = new Label(String.valueOf(quantity), skin);


        setPosition(x, y);
        actor.setSize(h, h);
        actor.setFontScale(0.02f);

        stage.addActor(actor);

    }

    @Override
    public void setPosition (float x, float y) {
        this.x = x;
        this.y = y;


        actor.setPosition(x - w/2, y - h/2);
    }

    @Override
    public void setPosition (float x, float y, float d) {
        this.x = x;
        this.y = y;


        actor.setPosition(x - w/2, y -h/2);
    }

    @Override
    public void draw(){
        actor.draw(batch, 1);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
