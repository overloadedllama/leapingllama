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

import com.overloadedllama.leapingllama.database.LlamaDbHandler;

public class MainMenuScreen implements Screen {

    private final GameApp game;
    private static final String TEST_USER = "test";

    OrthographicCamera camera;
    ExtendViewport viewport;
    LlamaDbHandler llamaDbHandler;

    float defaultButtonWidth = 240F;
    float defaultButtonHeight = 100F;

    private Stage mainMenuStage;

    // Tables
    private Table mainMenuTable;
    private Table userTable;
    private Table bestScoreTable;
    private Table moneyTable;

    // TextButton
    private TextButton settingsButton;
    private TextButton playButton;
    private TextButton shopButton;
    private TextButton creditsButton;

    // TextField
    private TextField user;
    private TextField bestScore;
    private TextField money;

    // Skin
    private Skin textButtonSkin;
    private Skin textFieldSkin;

    public MainMenuScreen(final GameApp game) {
        this.game = game;
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();

        llamaDbHandler = new LlamaDbHandler(game.getContext());
    }

    @Override
    public void show() {

        mainMenuStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        mainMenuTable = new Table();
        userTable = new Table();
        bestScoreTable = new Table();
        moneyTable = new Table();

        // creation of Skins
        textButtonSkin = new Skin(Gdx.files.internal("text_button/text_button.json"),
                new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        textFieldSkin = new Skin(Gdx.files.internal("text_field/text_field.json"),
                new TextureAtlas(Gdx.files.internal("text_field/text_field.atlas")));
        textFieldSkin.getFont("Dream").getData().setScale(0.7F);

        // creation of TextButtons
        textButtonSkin.getFont("Dream").getData().setScale(1F);
        playButton = new TextButton("play", textButtonSkin);
        shopButton = new TextButton("shop", textButtonSkin);
        settingsButton = new TextButton("settings", textButtonSkin);
        creditsButton = new TextButton("credits", textButtonSkin);

        // creation of TextField
        textFieldSkin.getFont("Dream").getData().setScale(0.6F);
        user = new TextField("user: " + TEST_USER, textFieldSkin);
        bestScore = new TextField("best score: " + llamaDbHandler.getUserBestScore(TEST_USER), textFieldSkin);
        money = new TextField("money: " + llamaDbHandler.getUserMoney(TEST_USER), textFieldSkin);

        // adding items into mainMenu and info Tables and them to the mainMenuStage
        bestScoreTable.top().left();
        bestScoreTable.add(bestScore).width(240F).height(80F);
        userTable.top();
        userTable.add(user).width(240F).height(80F);
        moneyTable.top().right();
        moneyTable.add(money).width(240F).height(80F);

        mainMenuTable.add(playButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(shopButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(10F);

        mainMenuStage.addActor(userTable);
        mainMenuStage.addActor(bestScoreTable);
        mainMenuStage.addActor(moneyTable);

        mainMenuStage.addActor(mainMenuTable);

        Gdx.input.setInputProcessor(mainMenuStage);

        // adding the listeners to the buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(game));
            }
        });

        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ShopScreen(game));
            }
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        mainMenuStage.act();
        mainMenuStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        userTable.invalidateHierarchy();
        userTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        bestScoreTable.invalidateHierarchy();
        bestScoreTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        moneyTable.invalidateHierarchy();
        moneyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

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
        mainMenuStage.dispose();

        textFieldSkin.dispose();
        textButtonSkin.dispose();
    }
}
