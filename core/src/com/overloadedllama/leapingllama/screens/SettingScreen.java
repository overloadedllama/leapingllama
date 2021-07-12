package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;

public class SettingScreen extends MyAbstractScreen {

    private Stage settingsStage;
    private Table settingTable;
    private Table backButtonTable;

    private final String ON = "on";
    private final String OFF = "off";

    // TextButtons
    private TextButton musicButton;
    private TextButton soundButton;
    private TextButton lxDxButton;
    private TextButton backButton;

    // Skins
    private Skin textButtonSkin;

    public SettingScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        settingsStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        settingTable = new Table();
        backButtonTable = new Table();

        // creation of the Skins
        textButtonSkin = assets.getSkin("bigButton");

        // creation of TextButtons
        musicButton = new TextButton("MUSIC: " + Settings.getSetting("MUSIC"), textButtonSkin);
        musicButton.setDisabled(false);
        soundButton = new TextButton("SOUND: " + Settings.getSetting("SOUND"), textButtonSkin);
        musicButton.setDisabled(false);
        lxDxButton = new TextButton("GAME BUTTONS: LX-DX", textButtonSkin);
        lxDxButton.setDisabled(false);
        backButton = new TextButton("BACK", textButtonSkin);
        backButton.setDisabled(true);

        // adding items to settingTable and backButtonTable and them to settingsStage
        float w = 300f, h = 120f;
        settingTable.add(musicButton).width(w).height(h);
        settingTable.row();
        settingTable.add(soundButton).width(w).height(h).padTop(10f);
        settingTable.row();
        settingTable.add(lxDxButton).width(w).height(h).pad(10f);

        backButtonTable.top().left();
        backButtonTable.add(backButton).width(w).height(80f);

        settingsStage.addActor(settingTable);
        settingsStage.addActor(backButtonTable);

        Gdx.input.setInputProcessor(settingsStage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isMUSIC()) {
                    Settings.setMUSIC(false);
                    Settings.stopMusic("mainMenuMusic");
                    musicButton.getLabel().setText("MUSIC: " + OFF);
                } else {
                    Settings.setMUSIC(true);
                    Settings.playMusic("mainMenuMusic");
                    musicButton.getLabel().setText("MUSIC: " + ON);
                }
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isSOUND()) {
                    Settings.setSOUND(false);
                    soundButton.getLabel().setText("SOUND: " + OFF);
                } else {
                    Settings.setSOUND(true);
                    soundButton.getLabel().setText("SOUND: " + ON);
                }
            }
        });

        lxDxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.getSetting("GAME_MODE").equals("LX_DX")) {
                    Settings.setGameMode();
                    lxDxButton.setText("GAME BUTTONS: DX-LX");
                } else if (Settings.getSetting("GAME_MODE").equals("DX_LX")){
                    Settings.setGameMode();
                    lxDxButton.setText("GAME BUTTONS: GESTURES");
                } else {
                    Settings.setGameMode();
                    lxDxButton.setText("GAME BUTTONS: LX_DX");
                }
            }
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        settingsStage.act();
        settingsStage.draw();

        super.render(delta);

        gameApp.batch.begin();
        gameApp.font.setColor(0 , 255, 0, 1);
        gameApp.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        settingTable.invalidateHierarchy();
        settingTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        backButtonTable.invalidateHierarchy();
        backButtonTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        settingsStage.dispose();

    }
}
