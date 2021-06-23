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
import com.overloadedllama.leapingllama.database.ScoreSettingsManager;

import java.sql.SQLException;

import static com.overloadedllama.leapingllama.database.ScoreSettingsManager.getIsOn;
import static com.overloadedllama.leapingllama.database.ScoreSettingsManager.setIsOn;

public class SettingScreen implements Screen {
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    GameApp game;

    private Stage settingsStage;
    private Table settingTable;

    private TextButton musicButton;
    private Skin musicSkin;
    private TextField musicText;

    private ImageButton backButton;
    private Skin backSkin;

    public SettingScreen(GameApp game) {
        this.game = game;
        camera = new OrthographicCamera();

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        camera.update();


        try {
            new ScoreSettingsManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {

        settingsStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));

        settingTable = new Table();

        /*
        try {
            if (getIsOn()) {
                musicButton = new TextButton("ON", musicSkin);
            } else {
                musicButton = new TextButton("OFF", musicSkin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        settingsStage.addActor(settingTable);

        Gdx.input.setInputProcessor(settingsStage);


        /*
        musicButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if (getIsOn()) {
                        setIsOn(0);
                        musicButton.setText("OFF");
                        // call methods to shut down music...
                    } else {
                        setIsOn(1);
                        musicButton.setText("ON");
                        // call methods to restart music...
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });*/


    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(0 , 255, 0, 1);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        settingTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
        settingTable.invalidateHierarchy();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        settingsStage.dispose();
        //musicSkin.dispose();
    }
}
