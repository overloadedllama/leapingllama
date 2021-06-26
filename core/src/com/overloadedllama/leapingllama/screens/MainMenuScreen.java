package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;

import com.overloadedllama.leapingllama.database.LlamaDbHandler;

public class MainMenuScreen implements Screen {

    final GameApp game;

    LlamaDbHandler llamaDbHandler;
    OrthographicCamera camera;
    ExtendViewport viewport;

    private Stage mainMenuStage;
    private Table mainMenuTable;

    private Texture logo;

    private TextButton settingButton;
    private Skin settingButtonSkin;

    private TextButton playButton;
    private Skin playButtonSkin;

    private TextButton creditsButton;
    private Skin creditsButtonSkin;

    private TextField username;
    private Skin usernameSkin;

    public MainMenuScreen(final GameApp game) {
        this.game = game;
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();

        llamaDbHandler = new LlamaDbHandler(game.context);
    }

    @Override
    public void show() {

        logo = new Texture(Gdx.files.internal("logo.png"));
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        mainMenuStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));

        mainMenuTable = new Table();

        // creation of Play Button
        playButtonSkin = new Skin(Gdx.files.internal("text_button/text_button.json"),
                                   new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        playButton = new TextButton("play", playButtonSkin);

        // creation of creditsButton
        creditsButtonSkin = playButtonSkin;
        creditsButton = new TextButton("credits", creditsButtonSkin);

        // creation of Settings Button
        settingButtonSkin = playButtonSkin;
        settingButton = new TextButton("settings", settingButtonSkin);

        // creation of username TextField
        usernameSkin = new Skin(Gdx.files.internal("text_field/text_field.json"),
                new TextureAtlas(Gdx.files.internal("text_field/text_field.atlas")));
        username = new TextField("test", usernameSkin);
        username.setDisabled(true);

        // add the buttons and the username to the mainMenuTable and next it to the mainMenuStage
        mainMenuTable.add(playButton).width(260F).height(120F);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(260F).height(120F).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(settingButton).width(260F).height(120F).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(username).width(260F).height(120F).padTop(10F);


        mainMenuStage.addActor(mainMenuTable);

        Gdx.input.setInputProcessor(mainMenuStage);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(game));
            }
        });


        // creation of the and Test user
        //llamaDbHandler.insertNewUser("test");

        System.out.println("User Test Money: " + llamaDbHandler.getUserMoney("test"));

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        mainMenuStage.act();
        mainMenuStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        game.font.setColor(0 , 255, 0, 1);
        game.batch.draw(logo,
                (float) logo.getWidth()/8,  (GameApp.HEIGHT/3)*2 - (float) logo.getHeight()/8,
                (float) logo.getWidth()/4,  (float) logo.getHeight()/4);
        //the divisions for 4 in the x and y above are due to the resize of the w and h

        game.font.draw(game.batch, "Tap the button to begin!", 2, 50);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);


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
        logo.dispose();
        mainMenuStage.dispose();

        playButtonSkin.dispose();
    }
}
