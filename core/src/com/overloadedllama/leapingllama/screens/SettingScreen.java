package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.resources.Settings;


public class SettingScreen extends MyAbstractScreen {

    private Stage settingsStage;
    private Table settingTable;
    private Table backButtonTable;

    private final String ON = "on";
    private final String OFF = "off";

    private ImageButton backButton;

    // TextButtons
    private TextButton musicButton;
    private TextButton soundButton;
    private TextButton lxDxButton;
    private TextButton goreButton;

    // Skins
    private Skin textButtonSkin;
    private Skin backButtonSkin;

    //Sky
    Sky sky;
    Batch batch;

    public SettingScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        sky = new Sky(assets.getTexture("sky"));
        sky.setXSky(Settings.getXSky());

        settingsStage = new Stage(new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT));
        settingTable = new Table();
        backButtonTable = new Table();

        // creation of the Skins
        textButtonSkin = assets.getSkin("bigButton");
        backButtonSkin = assets.getSkin("backButton");

        // creation of TextButtons
        musicButton = new TextButton("MUSIC: " + Settings.getStringSetting(MUSIC), textButtonSkin);
        soundButton = new TextButton("SOUND: " + Settings.getStringSetting(SOUND), textButtonSkin);
        lxDxButton = new TextButton(Settings.getStringSetting(GAME_MODE_), textButtonSkin);
        goreButton = new TextButton("GORE: " + Settings.getStringSetting(GORE), textButtonSkin);
        backButton = new ImageButton(backButtonSkin);
        backButton.setDisabled(false);

        // adding items to settingTable and backButtonTable and them to settingsStage
        float w = 300f, h = 120f, pad = 15f;
        settingTable.add(musicButton).width(w).height(h);
        settingTable.row();
        settingTable.add(soundButton).width(w).height(h).padTop(pad);
        settingTable.row();
        settingTable.add(lxDxButton).width(w).height(h).padTop(pad);
        settingTable.row();
        settingTable.add(goreButton).width(w).height(h).padTop(pad);
        settingsStage.addActor(settingTable);

        backButtonTable.top().left();
        backButtonTable.add(backButton).width(h).height(h).padLeft(pad).padTop(pad);
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
                    Settings.stopMusic(MAIN_MENU_MUSIC);
                    musicButton.getLabel().setText("MUSIC: " + OFF);
                } else {
                    Settings.setMUSIC(true);
                    Settings.playMusic(MAIN_MENU_MUSIC);
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
                if (Settings.getStringSetting(GAME_MODE_).equals("LEFT HANDED")) {
                    Settings.setGameMode();
                    lxDxButton.setText("RIGHT HANDED");
                } else if (Settings.getStringSetting(GAME_MODE_).equals("RIGHT HANDED")){
                    Settings.setGameMode();
                    lxDxButton.setText("GESTURES");
                } else {
                    Settings.setGameMode();
                    lxDxButton.setText("LEFT HANDED");
                }
            }
        });

        goreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (Settings.isGORE()) {
                    Settings.setGORE(false);
                    goreButton.setText("GORE: " + OFF);
                } else {
                    Settings.setGORE(true);
                    goreButton.setText("GORE: " + ON);
                }
            }
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();
        Settings.setXSky(sky.getXSky());

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
