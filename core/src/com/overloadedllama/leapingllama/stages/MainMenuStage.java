package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.screens.CreditScreen;
import com.overloadedllama.leapingllama.screens.GameScreen;
import com.overloadedllama.leapingllama.screens.SettingScreen;
import com.overloadedllama.leapingllama.screens.ShopScreen;

public class MainMenuStage extends MyAbstractStage {
    final int numLevels = 6;
    int maxUserLevel;
    float defaultButtonWidth = 350F;
    float defaultButtonHeight = 100F;

    // Image
    // private final Image fadeoutBackground;

    // Tables
    private final Table mainMenuTable;
    private final Table userMoneyTable;
    private final Table levelTable;
    private final Table chooseUserTable;
    private final Table scrollTable;
    private final Table howdyTable;

    // ScrollPane
    private final ScrollPane scroller;

    // ImageButton
    private final ImageButton backButton;
    private final ImageButton backButton1;

    // TextButton
    private final TextButton settingsButton;
    private final TextButton playButton;
    private final TextButton shopButton;
    private final TextButton creditsButton;
    private final TextButton moneyButton;
    private final TextButton endlessMode;
    private final TextButton quitButton;
    private final TextButton userButton;
    private final TextButton[] levelButtons;

    // TextField
    private final TextField userTextField;

    // Label
    private final Label userLabel;
    private final Label userNameLabel;
    // Skin
    private final Skin textButtonFieldLabelSkin;
    private final Skin moneyButtonSkin;
    private final Skin backButtonSkin;
    private final Skin justTextSkin;

    public MainMenuStage(final GameApp game) {
        super(game);

        maxUserLevel = Settings.getUserLevel();

        // fadeoutBackground = new Image(assets.getTexture("quiteBlack"));

        mainMenuTable = new Table();
        userMoneyTable = new Table();
        levelTable = new Table();
        chooseUserTable = new Table();
        howdyTable = new Table();
        scrollTable = new Table();


        // creation of Skins
        textButtonFieldLabelSkin = assets.getSkin("bigButton");
        moneyButtonSkin = assets.getSkin("coin");
        backButtonSkin = assets.getSkin("backButton");
        justTextSkin = assets.getSkin("justText");
        // creation of TextButtons
        backButton = new ImageButton(backButtonSkin);
        playButton = new TextButton("PLAY", textButtonFieldLabelSkin);
        shopButton = new TextButton("SHOP", textButtonFieldLabelSkin);
        settingsButton = new TextButton("SETTINGS", textButtonFieldLabelSkin);
        creditsButton = new TextButton("CREDITS", textButtonFieldLabelSkin);
        quitButton = new TextButton("QUIT", textButtonFieldLabelSkin);
        String userMoney = "" + Settings.getUserMoney();
        moneyButton = new TextButton(userMoney , moneyButtonSkin);

        levelButtons = new TextButton[numLevels];
        for (int i = 0; i < numLevels; ++i) {
            levelButtons[i] = new TextButton("LEVEL " + i, textButtonFieldLabelSkin);
            levelButtons[i].setDisabled(!(i <= maxUserLevel));
        }
        endlessMode = new TextButton("ENDLESS MODE", textButtonFieldLabelSkin);
        //userButton = new TextButton("USER: " + Settings.getCurrentUser(), textButtonFieldLabelSkin);
        userButton = new TextButton("USERS", textButtonFieldLabelSkin);
        userNameLabel = new Label("", justTextSkin);
        float padTop = 15f;

        // MONEY TABLE
        userMoneyTable.top().right();
        //userMoneyTable.add(userButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop).padLeft(15f);
        userMoneyTable.add(moneyButton).padTop(padTop).padRight(15f); //padLeft(GameApp.WIDTH - 240f - moneyButton.getWidth() - 30f)
        addActor(userMoneyTable);

        // USERS TABLE
        howdyTable.top().left();
        howdyTable.add(userNameLabel).padTop(padTop).padLeft(25f);
        addActor(howdyTable);


        // MAIN MENU TABLE
        mainMenuTable.add(playButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(shopButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).width(defaultButtonWidth/2-padTop).height(defaultButtonHeight).padTop(padTop).align(Align.left);
        mainMenuTable.add(userButton).width(defaultButtonWidth/2).height(defaultButtonHeight).padTop(padTop).align(Align.right);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(defaultButtonWidth/2 -padTop).height(defaultButtonHeight).padTop(padTop).align(Align.left);
        mainMenuTable.add(quitButton).width(defaultButtonWidth/2).height(defaultButtonHeight).padTop(padTop).align(Align.right);
        addActor(mainMenuTable);

        // FADEOUT BACKGROUND
        //deactivated because it made the mood a bit too goth.
        // fadeoutBackground.setBounds(0, 0, getViewport().getScreenWidth(), getViewport().getScreenHeight());
        // fadeoutBackground.setVisible(false);
        // addActor(fadeoutBackground);

        // LEVEL SCROLL TABLE AND SCROLLER
        scrollTable.add(backButton).padTop(padTop).colspan(1).align(Align.left); //removed .size(defaultButtonWidth, defaultButtonHeight)
        Label titleLevelChooser = new Label("LEVELS", justTextSkin);
        titleLevelChooser.setFontScale(2);
        scrollTable.add(titleLevelChooser).align(Align.right).colspan(3);

        scrollTable.row();
        scrollTable.add(endlessMode).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop).colspan(1);
        scrollTable.add(new Label("BEST SCORE: " + String.valueOf(Math.round(Settings.getLevelBestScore(-1)*10)/10), justTextSkin)).colspan(3);
        for (int i = 0; i < numLevels; ++i) {
            scrollTable.row();
            scrollTable.add(levelButtons[i]).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop);
            int starNum = Settings.getLevelStarNum(i);
            for (int j = 0; j < 3; ++j) {
                Image star;
                if (j < starNum) {
                    star = new Image(assets.getTexture("starWon"));
                } else {
                    star = new Image(assets.getTexture("starLost"));
                }
                scrollTable.add(star).size(100f, 100f).padLeft(15f);
            }

        }
        scroller = new ScrollPane(scrollTable);
        levelTable.setFillParent(true);
        levelTable.add(scroller).fill().expand();
        levelTable.setVisible(false);
        addActor(levelTable);

        // CHOOSE USER TABLE
        backButton1 = new ImageButton(backButtonSkin);
        userLabel = new Label("CURRENT USER: ", justTextSkin);
        userLabel.setAlignment(Align.center);
        userTextField = new TextField("" + Settings.getCurrentUser(), textButtonFieldLabelSkin);
        userTextField.setAlignment(Align.center);
        chooseUserTable.top();
        chooseUserTable.add(backButton1).padTop(padTop).align(Align.left);  //removed size(100f, defaultButtonHeight).colspan(2).

        Label titleUserChooser = new Label("USERS", justTextSkin);
        titleUserChooser.setFontScale(2);
        chooseUserTable.add(titleUserChooser).align(Align.right);

        chooseUserTable.row();
        chooseUserTable.add(userLabel).size(defaultButtonWidth, defaultButtonHeight).padRight(15f).padTop(15f);
        chooseUserTable.add(userTextField).size(defaultButtonWidth, defaultButtonHeight).padTop(15f);
        chooseUserTable.setVisible(false);
        addActor(chooseUserTable);

        // adds the button's listeners
        setUpButtons();

    }

    private void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        userButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                mainMenuTable.setVisible(false);
                userMoneyTable.setVisible(false);
                howdyTable.setVisible(false);
                //fadeoutBackground.setVisible(true);
                chooseUserTable.setVisible(true);
            }
        });

        backButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //fadeoutBackground.setVisible(false);
                chooseUserTable.setVisible(false);
                Settings.insertNewUser(userTextField.getText());
                Settings.setCurrentUser(userTextField.getText());
                System.out.println("CURRENT USER: " + Settings.getCurrentUser());
                mainMenuTable.setVisible(true);
                userMoneyTable.setVisible(true);
                howdyTable.setVisible(true);
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addAction(Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        assets.loadGameAssets();
                    }
                }));

                mainMenuTable.setVisible(false);
                userMoneyTable.setVisible(false);
                howdyTable.setVisible(false);
                //fadeoutBackground.setVisible(true);
                levelTable.setVisible(true);
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

                try {
                    assets.unloadGameAssets();
                } catch (GdxRuntimeException e) {
                    System.out.println("Game assets aren't totally loaded yet, loaded: " + assets.getProgress() * 100 + "%");
                }

                mainMenuTable.setVisible(true);
                howdyTable.setVisible(true);
                userMoneyTable.setVisible(true);
                levelTable.setVisible(false);
                //fadeoutBackground.setVisible(false);
            }
        });

        for (int i = 0; i < numLevels; ++i) {
            final int finalI = i;
            if (!levelButtons[i].isDisabled()) {
                levelButtons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        if (assets.update()) {
                            Settings.setGameSoundsMusics();
                            Settings.stopMusic(MAIN_MENU_MUSIC);
                            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, finalI));
                        }
                    }
                });
            }
        }

        endlessMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (assets.update()) {
                    Settings.setGameSoundsMusics();

                    Settings.stopMusic(assets.MAIN_MENU_MUSIC);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, -1));
                }
            }
        });
    }

    public void renderer() {
        super.renderer();
        assets.update();

        userNameLabel.setText("Howdy, " + Settings.getCurrentUser());
        moneyButton.setText("" + Settings.getUserMoney());
    }

    public void resizer() {

        chooseUserTable.invalidateHierarchy();
        chooseUserTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        userMoneyTable.invalidateHierarchy();
        userMoneyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        howdyTable.invalidateHierarchy();
        howdyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        levelTable.invalidateHierarchy();
        levelTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        scrollTable.invalidateHierarchy();
        scrollTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

    }

    public void dispose() {

    }

}
