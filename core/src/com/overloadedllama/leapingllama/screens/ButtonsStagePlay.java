package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;

import java.util.HashMap;


public class ButtonsStagePlay {

    float tableWidth, tableHeight;
    final float buttonSize = 100f;

    Stage stage;

    // tables
    Table buttonsMovement;
    Table buttonsAction;
    Table buttonPauseTable;
    Table buttonPauseMenuTable;

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
    Skin buttonSkin;

    // TextButtons
    TextButton buttonPlay;
    TextButton buttonSaveExit;

    HashMap<String, Boolean> actions;

    public ButtonsStagePlay() {

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;
        stage = new Stage(new FitViewport(tableWidth, tableHeight));

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

        buttonFistSkin = new Skin(Gdx.files.internal("ui/fistButton.json"), new TextureAtlas(Gdx.files.internal("ui/fistButton.atlas")));
        buttonFist = new ImageButton(buttonFistSkin);

        buttonShotSkin = new Skin(Gdx.files.internal("ui/shotButton.json"), new TextureAtlas(Gdx.files.internal("ui/shotButton.atlas")));
        buttonShot = new ImageButton(buttonShotSkin);

        buttonPauseSkin = new Skin(Gdx.files.internal("ui/pauseButton.json"), new TextureAtlas(Gdx.files.internal("ui/pauseButton.atlas")));
        buttonPause = new ImageButton(buttonPauseSkin);

        buttonSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        buttonPlay = new TextButton("RESUME", buttonSkin);
        buttonSaveExit = new TextButton("SAVE AND EXIT", buttonSkin);

        buttonPauseMenuTable.add(buttonPlay);
        buttonPauseMenuTable.add(buttonSaveExit);


        buttonPauseTable.top().left();
        buttonPauseTable.add(buttonPause).width(buttonSize).height(buttonSize).padLeft(15f).padTop(15f);

        buttonsMovement.bottom().left();
        buttonsMovement.add(buttonJump).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);
        buttonsMovement.add(buttonCrouch).width(buttonSize).height(buttonSize).padBottom(15f).padLeft(15f);

        buttonsAction.bottom().right();
        buttonsAction.add(buttonShot).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);
        buttonsAction.add(buttonFist).width(buttonSize).height(buttonSize).padLeft(15f).padBottom(15f);

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
        actions.put("fist", false);
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

                fadeoutBackground.setBounds(0,0, tableWidth, tableHeight);
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
                // actions.remove("jump");
                actions.put("exit", true);

            }
        });


        buttonJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
               // actions.remove("jump");
                actions.put("jump", true);

            }
        });

        buttonFist.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
               // actions.remove("fist");
                actions.put("fist", true);

            }
        });

        buttonCrouch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
              //  actions.remove("crouch");
                actions.put("crouch", true);

            }
        });

        buttonShot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.remove("shot");
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
