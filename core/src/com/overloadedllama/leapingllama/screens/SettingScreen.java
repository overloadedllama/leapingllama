package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.assetman.Assets;

public class SettingScreen implements Screen {
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    private final Assets assets;
    GameApp game;

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

    public SettingScreen(GameApp game) {
        this.game = game;
        this.assets = game.getAssets();
        camera = new OrthographicCamera();

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        camera.update();
    }

    @Override
    public void show() {

        settingsStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        settingTable = new Table();
        backButtonTable = new Table();

        // creation of the Skins
        textButtonSkin = assets.getSkin("bigButton");

        // creation of TextButtons
        musicButton = new TextButton("MUSIC: " + Settings.getState("MUSIC"), textButtonSkin);
        musicButton.setDisabled(false);
        soundButton = new TextButton("SOUND: " + Settings.getState("SOUND"), textButtonSkin);
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
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isMUSIC()) {
                    Settings.setMUSIC(false);
                    musicButton.getLabel().setText("MUSIC: " + OFF);
                } else {
                    Settings.setMUSIC(true);
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
                if (Settings.isLxDx()) {
                    Settings.setLxDx(false);
                    lxDxButton.setText("GAME BUTTONS: DX-LX");
                } else {
                    Settings.setLxDx(true);
                    lxDxButton.setText("GAME BUTTONS: LX-DX");
                }
            }
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        settingsStage.act();
        settingsStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(0 , 255, 0, 1);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
