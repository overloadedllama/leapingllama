package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.GameStage;

public abstract class AbstractGameObjectLabel extends AbstractGameObject {

    int quantity;
    Label actor;
    Skin skin;

    GameStage stagePlay;

    public AbstractGameObjectLabel(String name, float x, float y, float h, int quantity, GameStage stage, String fontName, LlamaUtil llamaUtil) {
        super(name, x, y, h, llamaUtil);
        this.quantity = quantity;
        this.skin = llamaUtil.getAssetManager().getSkin(name);
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

}
