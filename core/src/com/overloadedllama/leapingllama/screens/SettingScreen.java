package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;


public class SettingScreen implements Screen {
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    GameApp game;

    private Stage settingsStage;
    private Table settingTable;
    private Table backButtonTable;

    private final String ON = "on";
    private final String OFF = "off";

    private boolean MUSIC_ON = true;
    private boolean SOUND_ON = true;


    // ImageButtons
    private ImageButton backButton;

    // TextButtons
    private TextButton musicButton;
    private TextButton soundButton;

    // TextFields
    private TextField musicTextField;
    private TextField soundTextField;

    // Skins
    private Skin imageButtonSkin;
    private Skin textButtonSkin;
    private Skin textFieldSkin;


    public SettingScreen(GameApp game) {
        this.game = game;
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
        textFieldSkin = new Skin(Gdx.files.internal("text_field/text_field.json"),
                new TextureAtlas(Gdx.files.internal("text_field/text_field.atlas")));
        textButtonSkin = new Skin(Gdx.files.internal("text_button/text_button.json"),
                new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        imageButtonSkin = new Skin(Gdx.files.internal("backButton/backButton.json"),
                new TextureAtlas(Gdx.files.internal("backButton/backButton.atlas")));

        // creation of TextFields
        musicTextField = new TextField("music: ", textFieldSkin);
        musicTextField.setDisabled(true);
        soundTextField = new TextField("sound: ", textFieldSkin);
        soundTextField.setDisabled(true);

        // creation of TextButtons
        musicButton = new TextButton(ON, textButtonSkin);
        musicButton.setDisabled(false);
        soundButton = new TextButton(ON, textButtonSkin);
        musicButton.setDisabled(false);

        // creation of ImageButtons
        backButton = new ImageButton(imageButtonSkin);

        // adding items to settingTable and backButtonTable and them to settingsStage
        settingTable.add(musicTextField).width(200F).height(120F);
        settingTable.add(musicButton).width(160F).height(120F).padLeft(10F);
        settingTable.row();
        settingTable.add(soundTextField).width(200F).height(120F);
        settingTable.add(soundButton).width(160F).height(120F).padLeft(10F);

        backButtonTable.top().left();
        backButtonTable.add(backButton).width(120F).height(80);

        settingsStage.addActor(settingTable);
        settingsStage.addActor(backButtonTable);

        Gdx.input.setInputProcessor(settingsStage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("BACK BUTTON PRESSED");
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });


        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isMUSIC()) {
                    Settings.setMUSIC(false);
                    musicButton.getLabel().setText(OFF);
                } else {
                    Settings.setMUSIC(true);
                    musicButton.getLabel().setText(ON);
                }
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.isSOUND()) {
                    Settings.setSOUND(false);
                    soundButton.getLabel().setText(OFF);
                } else {
                    Settings.setSOUND(true);
                    soundButton.getLabel().setText(ON);
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

        imageButtonSkin.dispose();
        textFieldSkin.dispose();
        textButtonSkin.dispose();

    }
}
