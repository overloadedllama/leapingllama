package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;

import java.util.HashMap;


public class ButtonsStagePlay {

    float tableWidth, tableHeight;
    final float buttonSize = 100f;

    Stage stage;
    ExtendViewport viewport;

    // tables
    Table buttonsMovement;
    Table buttonsAction;
    Table buttonPauseTable;
    Table buttonPauseMenuTable;

    // ImageButtons
    ImageButton buttonJump;
    ImageButton buttonCrouch;
    ImageButton buttonPunch;
    ImageButton buttonShot;
    ImageButton buttonPause;

    // TextButtons
    TextButton buttonPlay;
    TextButton buttonSaveExit;

    // TextField
    TextField score;

    // Skins
    Skin buttonJumpSkin;
    Skin buttonPauseSkin;
    Skin buttonPunchSkin;
    Skin buttonShotSkin;
    Skin buttonCrouchSkin;
    Skin buttonSkin;
    Skin scoreSkin;

    HashMap<String, Boolean> actions;

    public ButtonsStagePlay() {


        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT);
        stage = new Stage(viewport);
        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

        // creation of Tables
        buttonsMovement = new Table();
        buttonsAction = new Table();
        buttonPauseTable = new Table();
        buttonPauseMenuTable = new Table();

        // creation of ImageButtons and their Skins
        buttonJumpSkin = new Skin(Gdx.files.internal("ui/jumpButton.json"), new TextureAtlas(Gdx.files.internal("ui/jumpButton.atlas")));
        buttonJump = new ImageButton(buttonJumpSkin);

        buttonCrouchSkin = new Skin(Gdx.files.internal("ui/crouchButton.json"), new TextureAtlas(Gdx.files.internal("ui/crouchButton.atlas")));
        buttonCrouch = new ImageButton(buttonCrouchSkin);

        buttonPunchSkin = new Skin(Gdx.files.internal("ui/fistButton.json"), new TextureAtlas(Gdx.files.internal("ui/fistButton.atlas")));
        buttonPunch = new ImageButton(buttonPunchSkin);

        buttonShotSkin = new Skin(Gdx.files.internal("ui/shotButton.json"), new TextureAtlas(Gdx.files.internal("ui/shotButton.atlas")));
        buttonShot = new ImageButton(buttonShotSkin);

        buttonPauseSkin = new Skin(Gdx.files.internal("ui/pauseButton.json"), new TextureAtlas(Gdx.files.internal("ui/pauseButton.atlas")));
        buttonPause = new ImageButton(buttonPauseSkin);

        buttonSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        buttonPlay = new TextButton("RESUME", buttonSkin);
        buttonSaveExit = new TextButton("SAVE AND EXIT", buttonSkin);

        scoreSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        score = new TextField("0", scoreSkin);
        score.setAlignment(Align.center);

        buttonPauseMenuTable.add(buttonPlay);
        buttonPauseMenuTable.add(buttonSaveExit);


        float pad = 10f;

        buttonPauseTable.top().left();
        buttonPauseTable.add(buttonPause).width(buttonSize).height(buttonSize).padLeft(pad).padTop(pad);
        buttonPauseTable.add(score).padTop(pad).padLeft(GameApp.WIDTH / 2);

        buttonsMovement.bottom().left();
        buttonsMovement.add(buttonJump).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);
        buttonsMovement.add(buttonCrouch).width(buttonSize).height(buttonSize).padBottom(pad).padLeft(pad);

        buttonsAction.bottom().right();
        buttonsAction.add(buttonShot).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);
        buttonsAction.add(buttonPunch).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);

        buttonPauseMenuTable.top().center();
        buttonPauseMenuTable.add(buttonPlay).padLeft(tableWidth/2);
        buttonPauseMenuTable.row();
        buttonPauseMenuTable.add(buttonSaveExit);

        stage.addActor(buttonPauseTable);
        stage.addActor(buttonsMovement);
        stage.addActor(buttonsAction);

        Gdx.input.setInputProcessor(stage);

        //creation of the dictionary
        actions = new HashMap<>(7);
        actions.put("jump", false);
        actions.put("crouch", false);
        actions.put("shot", false);
        actions.put("punch", false);
        actions.put("pause", false);
        actions.put("play", false);
        actions.put("exit", false);




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

    Image fadeoutBackground;

    public void setUpButtonAction() {
        for (String s : actions.keySet()){
            //actions.remove(s);
            actions.put(s, false);
        }


        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //actions.remove("pause");
                actions.put("pause", true);

                fadeoutBackground = new Image(new Texture(Gdx.files.internal("quiteBlack.png")));

                fadeoutBackground.setBounds(0,0, viewport.getScreenWidth(), tableHeight);
                stage.addActor(fadeoutBackground);



                buttonPlay.setPosition(tableWidth/2 - buttonPlay.getWidth()/2, tableHeight/2 - buttonPlay.getHeight()+20);
                stage.addActor(buttonPlay);

                buttonSaveExit.setPosition(tableWidth/2 - buttonPlay.getWidth()/2, tableHeight/2 - buttonPlay.getHeight()*2);
                stage.addActor(buttonSaveExit);


            }
        });


        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // actions.remove("jump");
                actions.put("play", true);

                fadeoutBackground.remove();
                buttonPlay.remove();
                buttonSaveExit.remove();
            }
        });


        buttonSaveExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("exit", true);

            }
        });


        buttonJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("jump", true);

            }
        });

        buttonPunch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put("punch", true);

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
        buttonPunchSkin.dispose();
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
