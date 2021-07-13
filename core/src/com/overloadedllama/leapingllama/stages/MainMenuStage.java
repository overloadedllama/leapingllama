package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.screens.CreditScreen;
import com.overloadedllama.leapingllama.screens.GameScreen;
import com.overloadedllama.leapingllama.screens.SettingScreen;
import com.overloadedllama.leapingllama.screens.ShopScreen;

public class MainMenuStage extends MyAbstractStage {
    int numLevels = 5;
    int maxUserLevel;
    float defaultButtonWidth = 240F;
    float defaultButtonHeight = 100F;

    private Image fadeoutBackground;

    // Tables
    private Table mainMenuTable;
    private Table userTable;
    private Table moneyTable;
    private Table levelTable;

    private ScrollPane scroller;
    private Table scrollTable;

    // ImageButton
    private ImageButton backButton;

    // TextButton
    private TextButton settingsButton;
    private TextButton playButton;
    private TextButton shopButton;
    private TextButton creditsButton;
    private TextButton moneyButton;

    private TextButton[] levelButtons;

    private TextButton endlessMode;
    private TextButton quitButton;

    // TextField
    private TextField user;

    // Skin
    private Skin textButtonSkin;
    private Skin textFieldSkin;
    private Skin moneyButtonSkin;
    private Skin backButtonSkin;


    public MainMenuStage(final GameApp game) {
        super(game);


        maxUserLevel = Settings.getUserLevel();

        mainMenuTable = new Table();
        userTable = new Table();
        moneyTable = new Table();
        levelTable = new Table();

        scrollTable = new Table();

        fadeoutBackground = new Image(assets.getTexture("quiteBlack"));

        // creation of Skins
        textButtonSkin = assets.getSkin("bigButton");
        textFieldSkin = assets.getSkin("bigButton");
        moneyButtonSkin = assets.getSkin("coin");
        backButtonSkin = assets.getSkin("backButton");

        // creation of TextButtons
        backButton = new ImageButton(backButtonSkin);
        playButton = new TextButton("PLAY", textButtonSkin);
        shopButton = new TextButton("SHOP", textButtonSkin);
        settingsButton = new TextButton("SETTINGS", textButtonSkin);
        creditsButton = new TextButton("CREDIT", textButtonSkin);
        quitButton = new TextButton("QUIT", textButtonSkin);
        String userMoney = "" + Settings.getUserMoney();
        moneyButton = new TextButton(userMoney , moneyButtonSkin);

        levelButtons = new TextButton[5];
        for (int i = 0; i < numLevels; ++i) {
            levelButtons[i] = new TextButton("LEVEL " + i, textButtonSkin);
            levelButtons[i].setDisabled(!(i <= maxUserLevel));
        }
        endlessMode = new TextButton("ENDLESS", textButtonSkin);

        // creation of TextField
        textFieldSkin.getFont("pixeled").getData().setScale(1F);
        user = new TextField("  USER: " + Settings.getCurrentUser(), textFieldSkin);
        user.setDisabled(true);
        user.setAlignment(Align.center);
        // adding items into mainMenu and info Tables and them to the mainMenuStage
        userTable.top().left();
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
        addActor(moneyTable);

        addActor(mainMenuTable);

        setUpButtons();

    }

    private void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        // todo fix bug of second click on this button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeoutBackground.setBounds(0, 0, getViewport().getScreenWidth(), tableHeight);
                addActor(fadeoutBackground);

                float padTop = 15f;
                scrollTable.add(backButton).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop);
                scrollTable.row();

                for (int i = 0; i < numLevels; ++i) {
                    scrollTable.add(levelButtons[i]).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop);
                    scrollTable.row();
                }

                scrollTable.add(endlessMode).width(defaultButtonWidth).height(defaultButtonHeight).padTop(15f);

                scroller = new ScrollPane(scrollTable);
                levelTable.setFillParent(true);
                levelTable.add(scroller).fill().expand();

                addActor(levelTable);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(gameApp));
            }
        });

        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ShopScreen(gameApp));
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new CreditScreen(gameApp));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                assets.disposeAll();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                fadeoutBackground.remove();
                levelTable.remove();
            }
        });

        for (int i = 0; i < numLevels; ++i) {
            final int finalI = i;
            if (!levelButtons[i].isDisabled()) {
                levelButtons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        Settings.stopMusic(MAIN_MENU_MUSIC);
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, finalI));
                    }
                });
            }
        }

        endlessMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Settings.stopMusic(assets.MAIN_MENU_MUSIC);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, -1));
            }
        });
    }

    public void resizer() {
        userTable.invalidateHierarchy();
        userTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

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
