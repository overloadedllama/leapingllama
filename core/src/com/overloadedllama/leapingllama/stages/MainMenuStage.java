package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.screens.CreditScreen;
import com.overloadedllama.leapingllama.screens.GameScreen;
import com.overloadedllama.leapingllama.screens.SettingScreen;
import com.overloadedllama.leapingllama.screens.ShopScreen;

public class MainMenuStage extends MyAbstractStage {
    final int numLevels = 6;
    int maxUserLevel;
    float defaultButtonWidth = 240F;
    float defaultButtonHeight = 100F;

    // Image
    private final Image fadeoutBackground;

    // Tables
    private final Table mainMenuTable;
    private final Table userMoneyTable;
    private final Table levelTable;
    private final Table chooseUserTable;
    private final Table scrollTable;

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

    // Skin
    private final Skin textButtonFieldLabelSkin;
    private final Skin moneyButtonSkin;
    private final Skin backButtonSkin;


    public MainMenuStage(final GameApp game) {
        super(game);

        maxUserLevel = Settings.getUserLevel();

        mainMenuTable = new Table();
        userMoneyTable = new Table();
        levelTable = new Table();
        chooseUserTable = new Table();

        scrollTable = new Table();

        fadeoutBackground = new Image(assets.getTexture("quiteBlack"));

        // creation of Skins
        textButtonFieldLabelSkin = assets.getSkin("bigButton");
        moneyButtonSkin = assets.getSkin("coin");
        backButtonSkin = assets.getSkin("backButton");

        // creation of TextButtons
        backButton = new ImageButton(backButtonSkin);
        playButton = new TextButton("PLAY", textButtonFieldLabelSkin);
        shopButton = new TextButton("SHOP", textButtonFieldLabelSkin);
        settingsButton = new TextButton("SETTINGS", textButtonFieldLabelSkin);
        creditsButton = new TextButton("CREDIT", textButtonFieldLabelSkin);
        quitButton = new TextButton("QUIT", textButtonFieldLabelSkin);
        String userMoney = "" + Settings.getUserMoney();
        moneyButton = new TextButton(userMoney , moneyButtonSkin);

        levelButtons = new TextButton[numLevels];
        for (int i = 0; i < numLevels; ++i) {
            levelButtons[i] = new TextButton("LEVEL " + i, textButtonFieldLabelSkin);
            levelButtons[i].setDisabled(!(i <= maxUserLevel));
        }
        endlessMode = new TextButton("ENDLESS", textButtonFieldLabelSkin);
        userButton = new TextButton("  USER: " + Settings.getCurrentUser(), textButtonFieldLabelSkin);

        float padTop = 15f;

        // USER-MONEY TABLE
        userMoneyTable.top().left();
        userMoneyTable.add(userButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop).padLeft(15f);
        userMoneyTable.add(moneyButton).padLeft(GameApp.WIDTH - 240f - moneyButton.getWidth() - 15f).padTop(padTop);
        addActor(userMoneyTable);

        // MAIN MENU TABLE
        mainMenuTable.add(playButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        mainMenuTable.row();
        mainMenuTable.add(shopButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        mainMenuTable.row();
        mainMenuTable.add(quitButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        addActor(mainMenuTable);

        // FADEOUT BACKGROUND
        fadeoutBackground.setBounds(0, 0, getViewport().getScreenWidth(), tableHeight);
        fadeoutBackground.setVisible(false);
        addActor(fadeoutBackground);

        // SCROLL TABLE AND SCROLLER
        scrollTable.add(backButton).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop);
        scrollTable.row();
        for (int i = 0; i < numLevels; ++i) {
            scrollTable.add(levelButtons[i]).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop);
            scrollTable.row();
        }
        scrollTable.add(endlessMode).width(defaultButtonWidth).height(defaultButtonHeight).padTop(padTop);
        scroller = new ScrollPane(scrollTable);
        levelTable.setFillParent(true);
        levelTable.add(scroller).fill().expand();
        levelTable.setVisible(false);
        addActor(levelTable);

        // CHOOSE USER TABLE
        backButton1 = new ImageButton(backButtonSkin);
        userLabel = new Label("CURRENT USER: ", textButtonFieldLabelSkin);
        userLabel.setAlignment(Align.center);
        userTextField = new TextField("" + Settings.getCurrentUser(), textButtonFieldLabelSkin);
        userTextField.setAlignment(Align.center);
        chooseUserTable.top();
        chooseUserTable.add(backButton1).size(defaultButtonWidth, defaultButtonHeight).padTop(padTop).colspan(2);
        chooseUserTable.row();
        chooseUserTable.add(userLabel).size(defaultButtonWidth, defaultButtonHeight).padRight(10f);
        chooseUserTable.add(userTextField).size(defaultButtonWidth, defaultButtonHeight);
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
                fadeoutBackground.setVisible(true);
                chooseUserTable.setVisible(true);
            }
        });

        backButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                fadeoutBackground.setVisible(false);
                chooseUserTable.setVisible(false);
                Settings.insertNewUser(userTextField.getText());
                Settings.setCurrentUser(userTextField.getText());
                System.out.println("CURRENT USER: " + Settings.getCurrentUser());
                mainMenuTable.setVisible(true);
                userMoneyTable.setVisible(true);
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuTable.setVisible(false);
                fadeoutBackground.setVisible(true);
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
                mainMenuTable.setVisible(true);
                levelTable.setVisible(false);
                fadeoutBackground.remove();
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

    public void renderer() {
        super.renderer();
        userButton.setText(Settings.getCurrentUser());
        moneyButton.setText("" + Settings.getUserMoney());
    }

    public void resizer() {

        chooseUserTable.invalidateHierarchy();
        chooseUserTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        userMoneyTable.invalidateHierarchy();
        userMoneyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

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
