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



public class SettingScreen implements Screen {
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    GameApp game;

    private Stage settingsStage;
    private Table settingTable;

    private TextButton musicButton;
    private Skin musicButtonSkin;

    private TextField musicTextField;
    private Skin musicTextFieldSkin;

    private ImageButton backButton;
    private Skin backButtonSkin;

    private final String ON = "on";
    private final String OFF = "off";

    private boolean MUSIC_ON = true;

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


        // creation of the musicTextField
        musicTextFieldSkin = new Skin(Gdx.files.internal("text_field/text_field.json"),
                                        new TextureAtlas(Gdx.files.internal("text_field/text_field.atlas")));
        musicTextField = new TextField("music: ", musicTextFieldSkin);

        // disable the modifiability
        musicTextField.setDisabled(true);

        // creation of the musicButton
        musicButtonSkin = new Skin(Gdx.files.internal("text_button/text_button.json"),
                                        new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        musicButton = new TextButton(ON, musicButtonSkin);

        musicButton.setDisabled(false);

        // creation of the backButton
        backButtonSkin = new Skin(Gdx.files.internal("backButton/backButton.json"),
                                        new TextureAtlas(Gdx.files.internal("backButton/backButton.atlas")));
        backButton = new ImageButton(backButtonSkin);

        settingTable.add(musicTextField).width(160F).height(120F);
        settingTable.add(musicButton).width(120F).height(120F).padRight(100F);
        settingTable.row();
        settingTable.add(backButton).width(160F).height(120F).padTop(10F);



        settingsStage.addActor(settingTable);

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
                System.out.println("musicButton: " + musicButton.getText());
                if (MUSIC_ON) {
                    MUSIC_ON = false;
                    musicButton.getLabel().setText(OFF);
                } else {
                    MUSIC_ON = true;
                    musicButton.getLabel().setText(ON);
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

        backButtonSkin.dispose();
        musicButtonSkin.dispose();
        musicTextFieldSkin.dispose();

    }
}
