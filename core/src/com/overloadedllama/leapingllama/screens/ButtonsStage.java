package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;

import java.util.ArrayList;

public class ButtonsStage {




    ImageButton buttonJump;
    Skin buttonJumpSkin;

    ImageButton buttonCrouch;
    Skin buttonCrouchSkin;

    ImageButton buttonFist;
    Skin buttonFistSkin;

    ImageButton buttonShot;
    Skin buttonShotSkin;

    ImageButton buttonPause;
    Skin buttonPauseSkin;

    float buttonSize;

    Table buttons;
    Stage stage;


    public ButtonsStage(Viewport viewport, float w, float h) {

        stage = new Stage(viewport);



        buttonJumpSkin = new Skin(Gdx.files.internal("ui/jumpButton.json"), new TextureAtlas(Gdx.files.internal("ui/jumpButton.atlas")));
        buttonJump = new ImageButton(buttonJumpSkin);

        buttonCrouchSkin = new Skin(Gdx.files.internal("ui/crouchButton.json"), new TextureAtlas(Gdx.files.internal("ui/crouchButton.atlas")));
        buttonCrouch = new ImageButton(buttonCrouchSkin);

        buttonFistSkin = new Skin(Gdx.files.internal("ui/fistButton.json"), new TextureAtlas(Gdx.files.internal("ui/fistButton.atlas")));
        buttonFist = new ImageButton(buttonFistSkin);

        buttonShotSkin = new Skin(Gdx.files.internal("ui/shotButton.json"), new TextureAtlas(Gdx.files.internal("ui/shotButton.atlas")));
        buttonShot = new ImageButton(buttonShotSkin);

        buttonPauseSkin = new Skin(Gdx.files.internal("ui/pauseButton.json"), new TextureAtlas(Gdx.files.internal("ui/pauseButton.atlas")));
        buttonPause = new ImageButton(buttonPauseSkin);



        buttons = new Table();

        buttons.setSize(w, h);
        buttonSize = 0.3f;

        buttons.add(buttonPause).width(buttonSize).height(buttonSize).align(Align.topLeft);
        buttons.row();
        buttons.add(buttonJump).width(buttonSize).height(buttonSize).align(Align.bottomLeft);
        buttons.add(buttonShot).width(buttonSize).height(buttonSize).align(Align.bottomRight);
        buttons.row();
        buttons.add(buttonCrouch).width(buttonSize).height(buttonSize).align(Align.topLeft);
        buttons.add(buttonFist).width(buttonSize).height(buttonSize).align(Align.topRight);
        buttons.row();

        stage.addActor(buttons);

        Gdx.input.setInputProcessor(stage);

    }

    public void drawer() {
        stage.act();
        stage.draw();
    }

    public void resizer() {
        buttons.invalidateHierarchy();
        //buttons.setSize(GameApp.WIDTH, GameApp.HEIGHT);
    }

    public ImageButton getButtonJump() {
        return buttonJump;
    }

    public void setButtonJump(ImageButton buttonJump) {
        this.buttonJump = buttonJump;
    }
}
