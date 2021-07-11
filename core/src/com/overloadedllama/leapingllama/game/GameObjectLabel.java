package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;

public class GameObjectLabel extends GameObject{

    int quantity;
    Label actor;
    Skin skin;

    ButtonsStagePlay stagePlay;

    public GameObjectLabel(Skin skin, float x, float y, float h, int quantity, World world, Batch batch, ButtonsStagePlay stage, String fontName) {
        super(x, y, h, world, batch);
        this.quantity = quantity;
        this.skin = skin;
        stagePlay = stage;

        skin.getFont(fontName).setUseIntegerPositions(false);

        actor = new Label(String.valueOf(quantity), skin);

        setPosition(x, y);
        actor.setSize(h, h);
        actor.setFontScale(0.01f);
        actor.setAlignment(Align.center);

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
    public void draw() {

        actor.draw(batch, 1);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
