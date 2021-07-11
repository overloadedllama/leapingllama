package com.overloadedllama.leapingllama.stages;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.game.TestConstant;

import java.util.HashMap;


public class ButtonsStagePlay implements TestConstant {

    float tableWidth, tableHeight;
    final float buttonSize = 150f;

    Stage stage;
    ExtendViewport viewport;
    Assets assets;

    Image fadeoutBackground;

    // tables
    Table buttonsMovement;
    Table buttonsAction;
    Table buttonPauseTable;
    Table labelsTable;
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

    // Labels
    Label labelDistance;
    Label labelMoney;
    Label labelBullets;

    // Skins
    Skin buttonJumpSkin;
    Skin buttonPauseSkin;
    Skin buttonPunchSkin;
    Skin buttonShotSkin;
    Skin buttonCrouchSkin;
    Skin buttonSkin;
    Skin justTextSkin;

    //Distance
    double distance = 0;
    String distanceText;

    HashMap<String, Boolean> actions;


    public ButtonsStagePlay(Assets assets) {
        this.assets = assets;

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT);
        stage = new Stage(viewport);
        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

        // creation of Tables
        buttonsMovement = new Table();
        buttonsAction = new Table();
        buttonPauseTable = new Table();
        buttonPauseMenuTable = new Table();
        labelsTable = new Table();

        // skins from Assets
        buttonJumpSkin = assets.getSkin(JUMP);
        buttonPauseSkin = assets.getSkin(PAUSE);
        buttonCrouchSkin = assets.getSkin(CROUCH);
        buttonShotSkin = assets.getSkin(SHOT);
        buttonPunchSkin = assets.getSkin(PUNCH);
        buttonSkin = assets.getSkin("bigButton");
        justTextSkin = assets.getSkin("justText");

        // creation of ImageButtons,
        buttonJump = new ImageButton(buttonJumpSkin);
        buttonCrouch = new ImageButton(buttonCrouchSkin);
        buttonPunch = new ImageButton(buttonPunchSkin);
        buttonShot = new ImageButton(buttonShotSkin);
        buttonPause = new ImageButton(buttonPauseSkin);

        // TextButtons,
        buttonPlay = new TextButton("RESUME", buttonSkin);
        buttonSaveExit = new TextButton("SAVE AND EXIT", buttonSkin);

        // Labels
        labelDistance = new Label("", justTextSkin);
        labelMoney = new Label("0 coins", justTextSkin);
        labelBullets = new Label("0 shots", justTextSkin);
        labelBullets.setAlignment(Align.center);
        labelMoney.setAlignment(Align.center);
        labelDistance.setAlignment(Align.center);

        buttonPauseMenuTable.add(buttonPlay);
        buttonPauseMenuTable.add(buttonSaveExit);

        setDistance(0);

        float pad = 10f;

        buttonPauseTable.top().left();
        buttonPauseTable.add(buttonPause).width(buttonSize).height(buttonSize).padLeft(pad);

        if (Settings.isLxDx()) {
            buttonsMovement.bottom().left();
            buttonsAction.bottom().right();
        } else {
            buttonsMovement.bottom().right();
            buttonsAction.bottom().left();
        }

        buttonsMovement.add(buttonJump).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);
        buttonsMovement.add(buttonCrouch).width(buttonSize).height(buttonSize).padBottom(pad).padLeft(pad);

        buttonsAction.add(buttonShot).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);
        buttonsAction.add(buttonPunch).width(buttonSize).height(buttonSize).padLeft(pad).padBottom(pad);

        buttonPauseMenuTable.top().center();
        buttonPauseMenuTable.add(buttonPlay).padLeft(tableWidth/2);
        buttonPauseMenuTable.row();
        buttonPauseMenuTable.add(buttonSaveExit);

        float labelW = 140f, labelH = 80f;
        labelsTable.top().right();
        labelsTable.add(labelBullets).width(labelW).height(labelH).padRight(pad);
        labelsTable.add(labelMoney).width(labelW).height(labelH).padRight(pad);
        labelsTable.add(labelDistance).width(labelW).height(labelH).padRight(pad);

        stage.addActor(buttonPauseTable);
        stage.addActor(buttonsMovement);
        stage.addActor(buttonsAction);
        stage.addActor(labelsTable);

        Gdx.input.setInputProcessor(stage);

        //creation of the dictionary
        actions = new HashMap<>(7);
        actions.put(JUMP, false);
        actions.put(CROUCH, false);
        actions.put(SHOT, false);
        actions.put(PUNCH, false);
        actions.put(PAUSE, false);
        actions.put(PLAY, false);
        actions.put(EXIT, false);

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

        labelsTable.invalidateHierarchy();
        labelsTable.setSize(tableWidth, tableHeight);
    }


    public void setUpButtonAction() {
        for (String s : actions.keySet()){
            if (!s.equals(CROUCH))
                actions.put(s, false);
        }


        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put(PAUSE, true);

                fadeoutBackground = new Image(assets.getTexture("quiteBlack"));
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
                actions.put(PLAY, true);

                fadeoutBackground.remove();
                buttonPlay.remove();
                buttonSaveExit.remove();
            }
        });


        buttonSaveExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put(EXIT, true);

            }
        });

        buttonShot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put(SHOT, true);
            }
        });

        buttonPunch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actions.put(PUNCH, true);
            }
        });

        buttonCrouch.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                actions.put(CROUCH, true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                actions.put(CROUCH, false);
            }
        });

        buttonJump.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                actions.put(JUMP, true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                actions.put(JUMP, false);
            }
        });
    }

    public void dispose() {

    }

    public HashMap<String, Boolean> getActions() {
        return actions;
    }

    public void setActions(HashMap<String, Boolean> actions) {
        this.actions = actions;
    }

    public void setBullets(int n) {
        labelBullets.setText(n + " shots");
    }

    public void setMoney(int n) {
        labelMoney.setText(n + " coins");
    }


    @SuppressLint("DefaultLocale")
    public void setDistance(double distance) {
        this.distance = distance;

        distanceText = String.format("%.1f m", distance);
        labelDistance.setText(distanceText);

    }

    public void addActor(Label actor) {
        stage.addActor(actor);
    }
}
