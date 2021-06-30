package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;

import java.util.HashMap;


public class ButtonsStage {

    float tableWidth, tableHeight;
    final float buttonSize = 100f;

    Stage stage;

    // tables
    Table buttonsMovement;
    Table buttonsAction;
    Table buttonPauseTable;

    // ImageButtons
    ImageButton buttonJump;
    ImageButton buttonCrouch;
    ImageButton buttonFist;
    ImageButton buttonShot;
    ImageButton buttonPause;

    // Skins
    Skin buttonJumpSkin;
    Skin buttonPauseSkin;
    Skin buttonFistSkin;
    Skin buttonShotSkin;
    Skin buttonCrouchSkin;

    HashMap<String, Boolean> actions;

    public ButtonsStage() {

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;
        stage = new Stage(new FitViewport(tableWidth, tableHeight));

        // creation of Tables
        buttonsMovement = new Table();
        buttonsAction = new Table();
        buttonPauseTable = new Table();


        // creation of ImageButtons and their Skins
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


        buttonPauseTable.top().left();
        buttonPauseTable.add(buttonPause).width(buttonSize).height(buttonSize).padLeft(15f).padTop(15f);

        buttonsMovement.bottom().left();
        buttonsMovement.add(buttonJump).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);
        buttonsMovement.add(buttonCrouch).width(buttonSize).height(buttonSize).padBottom(15f).padLeft(15f);

        buttonsAction.bottom().right();
        buttonsAction.add(buttonShot).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);
        buttonsAction.add(buttonFist).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);


        stage.addActor(buttonPauseTable);
        stage.addActor(buttonsMovement);
        stage.addActor(buttonsAction);

        Gdx.input.setInputProcessor(stage);

        //creation of the dictionary
        actions = new HashMap<>(5);
        actions.put("jump", false);
        actions.put("crouch", false);
        actions.put("shot", false);
        actions.put("fist", false);
        actions.put("pause", false);




    }

    public void drawer() {
        stage.act();
        stage.draw();
    }

    public void resizer() {
        buttonPauseTable.invalidateHierarchy();
        buttonPauseTable.setSize(tableWidth, tableHeight);

        buttonsAction.invalidateHierarchy();
        buttonsAction.setSize(tableWidth, tableHeight);

        buttonsMovement.invalidateHierarchy();
        buttonsMovement.setSize(tableWidth, tableHeight);
    }


    public void setUpButtonAction() {
        for (String s : actions.keySet()){
            actions.put(s, false);
        }


        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("pause", true);
            }
        });

        buttonJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("jump", true);

            }
        });

        buttonFist.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("fist", true);

            }
        });

        buttonCrouch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("crouch", true);

            }
        });

        buttonShot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("shot", true);

            }
        });
    }

    public void dispose() {
        buttonCrouchSkin.dispose();
        buttonFistSkin.dispose();
        buttonJumpSkin.dispose();
        buttonShotSkin.dispose();
        buttonPauseSkin.dispose();
    }

    public HashMap<String, Boolean> getActions() {
        return actions;
    }

    public void setActions(HashMap<String, Boolean> actions) {
        this.actions = actions;
    }

}
