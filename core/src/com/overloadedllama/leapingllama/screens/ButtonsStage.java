package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ButtonsStage {

    Stage stage;


    TextButton buttonJump;
    Skin buttonLittle;
    Table buttons;



    public ButtonsStage(Viewport viewport, float w, float h) {


        stage = new Stage(viewport);

        buttonLittle = new Skin(Gdx.files.internal("ui/littleButton.json"), new TextureAtlas(Gdx.files.internal("ui/littleButton.atlas")));

        buttonJump = new TextButton("jump", buttonLittle);

        buttons = new Table(buttonLittle);
        buttons.setSize(w, h);
        buttons.add(buttonJump).width(0.5f).height(0.5f).align(Align.bottomLeft).getActor().getLabel().setFontScale(0.05f);

        stage.addActor(buttons);

        Gdx.input.setInputProcessor(stage);

    }

    public void drawer() {
        stage.act();
        stage.draw();
    }

    public void resizer() {
        buttonJump.invalidateHierarchy();

    }

    public TextButton getButtonJump() {
        return buttonJump;
    }

    public void setButtonJump(TextButton buttonJump) {
        this.buttonJump = buttonJump;
    }
}
