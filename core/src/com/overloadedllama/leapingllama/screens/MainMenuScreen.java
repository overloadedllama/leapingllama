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
import com.badlogic.gdx.utils.Align;
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
    private TextButton moneyButton;

    // TextField
    private TextField user;

    //Label
    private Label bestScore;

    // Skin
    private Skin textButtonSkin;
    private Skin textFieldSkin;
    private Skin moneyButtonSkin;

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
        textButtonSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        textFieldSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        moneyButtonSkin = new Skin(Gdx.files.internal("ui/coin.json"), new TextureAtlas(Gdx.files.internal("ui/coin.atlas")));

        // creation of TextButtons
        //textButtonSkin.getFont("pixeled").getData().setScale(1F);
        playButton = new TextButton("PLAY", textButtonSkin);
        shopButton = new TextButton("SHOP", textButtonSkin);
        settingsButton = new TextButton("SETTINGS", textButtonSkin);
        creditsButton = new TextButton("CREDIT", textButtonSkin);
        String userMoney = "" + llamaDbHandler.getUserMoney(TEST_USER);
        moneyButton = new TextButton(userMoney , moneyButtonSkin);

        //labels

        // creation of TextField
        textFieldSkin.getFont("pixeled").getData().setScale(1F);
        user = new TextField("  USER: " + TEST_USER, textFieldSkin);
        user.setDisabled(true);
        user.setAlignment(Align.center);

       //Label
        bestScore = new Label("BEST SCORE: 0", textFieldSkin); // + llamaDbHandler.getUserBestScore(TEST_USER), textFieldSkin);
        bestScore.setAlignment(Align.center);
        bestScore.setColor(255, 251, 209, 255); //NOT WORKING
        //money = new TextField("MONEY:  0", textFieldSkin);// + llamaDbHandler.getUserMoney(TEST_USER), textFieldSkin);

        // adding items into mainMenu and info Tables and them to the mainMenuStage
        bestScoreTable.top().left();
        bestScoreTable.add(bestScore).width(240F).height(80F).padTop(15F).padLeft(15F);
        userTable.top();
        userTable.add(user).width(240F).height(80F).padTop(15F);
        moneyTable.top().right();
        moneyTable.add(moneyButton).padTop(15F).padRight(15F);

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
        moneyButtonSkin.dispose();
    }
}
