package com.overloadedllama.leapingllama.stages;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.screens.GameScreen;
import com.overloadedllama.leapingllama.screens.SettingScreen;
import com.overloadedllama.leapingllama.screens.ShopScreen;

public class MainMenuStage extends Stage {
    GameApp game;
    Assets assets;

    int maxUserLevel;
    float tableHeight, tableWidth;
    float defaultButtonWidth = 240F;
    float defaultButtonHeight = 100F;

    private Image fadeoutBackground;

    // Tables
    private Table mainMenuTable;
    private Table userTable;
    private Table bestScoreTable;
    private Table moneyTable;
    private Table levelTable;

    // TextButton
    private TextButton settingsButton;
    private TextButton playButton;
    private TextButton shopButton;
    private TextButton creditsButton;
    private TextButton moneyButton;
    private TextButton level1;
    private TextButton level2;
    private TextButton level0;
    private TextButton endlessMode;
    private TextButton quitButton;

    // TextField
    private TextField user;

    //Label
    private Label bestScore;

    // Skin
    private Skin textButtonSkin;
    private Skin textFieldSkin;
    private Skin moneyButtonSkin;

    public MainMenuStage(final GameApp game) {
        super(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        this.game = game;
        this.assets = game.getAssets();

        maxUserLevel = Settings.getUserLevel();

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

        mainMenuTable = new Table();
        userTable = new Table();
        bestScoreTable = new Table();
        moneyTable = new Table();
        levelTable = new Table();

        fadeoutBackground = new Image(assets.getTexture("quiteBlack"));

        // creation of Skins
        textButtonSkin = assets.getSkin("bigButton");
        textFieldSkin = assets.getSkin("bigButton");
        moneyButtonSkin = assets.getSkin("coin");

        // creation of TextButtons
        playButton = new TextButton("PLAY", textButtonSkin);
        shopButton = new TextButton("SHOP", textButtonSkin);
        settingsButton = new TextButton("SETTINGS", textButtonSkin);
        creditsButton = new TextButton("CREDIT", textButtonSkin);
        quitButton = new TextButton("QUIT", textButtonSkin);
        String userMoney = "" + Settings.getUserMoney();
        moneyButton = new TextButton(userMoney , moneyButtonSkin);
        level0 = new TextButton("LEVEL 0", textButtonSkin);
        level1 = new TextButton("LEVEL 1", textButtonSkin);
        level2 = new TextButton("LEVEL 2", textButtonSkin);
        endlessMode = new TextButton("ENDLESS", textButtonSkin);

        if (maxUserLevel <= 1) {
            level2.setDisabled(true);
            if (maxUserLevel < 1) {
                level1.setDisabled(true);
            }
        }

        // creation of TextField
        textFieldSkin.getFont("pixeled").getData().setScale(1F);
        user = new TextField("  USER: " + Settings.getCurrentUser(), textFieldSkin);
        user.setDisabled(true);
        user.setAlignment(Align.center);

        //Label
        @SuppressLint("DefaultLocale") String bestScoreText = String.format("%.1f", Settings.getUserBestScore());
        bestScore = new Label("BEST SCORE: " + bestScoreText, textFieldSkin);
        bestScore.setAlignment(Align.center);
        bestScore.setColor(255, 251, 209, 255); //NOT WORKING

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
        mainMenuTable.row();
        mainMenuTable.add(quitButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(10F);

        addActor(userTable);
        addActor(bestScoreTable);
        addActor(moneyTable);

        addActor(mainMenuTable);

        setUpButtons();

    }

    private void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeoutBackground.setBounds(0, 0, getViewport().getScreenWidth(), tableHeight);
                addActor(fadeoutBackground);

                levelTable.add(level0).width(defaultButtonWidth).height(defaultButtonHeight);
                levelTable.row();
                levelTable.add(level1).width(defaultButtonWidth).height(defaultButtonHeight).padTop(15f);
                levelTable.row();
                levelTable.add(level2).width(defaultButtonWidth).height(defaultButtonHeight).padTop(15f);
                levelTable.row();
                levelTable.add(endlessMode).width(defaultButtonWidth).height(defaultButtonHeight).padTop(15f);

                addActor(levelTable);
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

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                assets.disposeAll();
            }
        });

        level0.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Settings.stopMusic(assets.MAIN_MENU_MUSIC);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, 0));
            }
        });

        if (!level1.isDisabled()) {
            level1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Settings.stopMusic(assets.MAIN_MENU_MUSIC);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, 1));
                }
            });
        }

        if (!level2.isDisabled()) {
            level2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Settings.stopMusic(assets.MAIN_MENU_MUSIC);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, 2));
                }
            });
        }

        endlessMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
    }

    public void renderer() {
        act();
        draw();
    }

    public void resizer() {
        userTable.invalidateHierarchy();
        userTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        bestScoreTable.invalidateHierarchy();
        bestScoreTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        moneyTable.invalidateHierarchy();
        moneyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        levelTable.invalidateHierarchy();
        levelTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

    }

    public void dispose() {

    }

}
