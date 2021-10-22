package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.llamautils.LlamaAssetManager;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.screens.*;


public class MainMenuStage extends MyAbstractStage {
    boolean hasToGoGameScreen = false;
    int maxUserLevel;
    final int numLevels = 6;
    float defaultButtonWidth = 220f;
    float defaultButtonHeight = 100F;

    // Tables
    private final Table mainMenuTable;
    private final Table moneyTable;
    private final Table levelTable;
    private final Table scrollTable;
    private final Table howdyTable;

    // ScrollPane
    private final ScrollPane scrollPane;

    // ImageButton
    private final ImageButton backButton;

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

    // Label
    private final Label userNameLabel;

    // Skin
    private final Skin textButtonFieldLabelSkin;
    private final Skin moneyButtonSkin;
    private final Skin backButtonSkin;
    private final Skin justTextSkin;
    private final Skin hugeButtonSkin;

    public MainMenuStage(LlamaUtil llamaUtil) {
        super(llamaUtil);
        LlamaAssetManager llamaAssetManager = llamaUtil.getAssetManager();

        maxUserLevel = llamaUtil.getLlamaDbHandler().getUserMaxLevel(currentUser);

        mainMenuTable = new Table();
        moneyTable = new Table();
        levelTable = new Table();
        howdyTable = new Table();
        scrollTable = new Table();

        // creation of Skins
        textButtonFieldLabelSkin = llamaAssetManager.getSkin("bigButton");
        moneyButtonSkin = llamaAssetManager.getSkin("coin");
        backButtonSkin = llamaAssetManager.getSkin("backButton");
        justTextSkin = llamaAssetManager.getSkin("justText");
        hugeButtonSkin = llamaAssetManager.getSkin("hugeButton");

        // creation of TextButtons
        backButton = new ImageButton(backButtonSkin);
        playButton = new TextButton("PLAY", hugeButtonSkin);
        shopButton = new TextButton("SHOP", hugeButtonSkin);
        settingsButton = new TextButton("SETTINGS", textButtonFieldLabelSkin);
        creditsButton = new TextButton("CREDITS", textButtonFieldLabelSkin);
        quitButton = new TextButton("QUIT", textButtonFieldLabelSkin);
        String userMoney = "COINS:\n" + llamaUtil.getLlamaDbHandler().getUserMoney(currentUser);
        moneyButton = new TextButton(userMoney , moneyButtonSkin);

        levelButtons = new TextButton[numLevels];
        for (int i = 0; i < numLevels; ++i) {
            levelButtons[i] = new TextButton("LEVEL " + i, textButtonFieldLabelSkin);
            levelButtons[i].setDisabled(!(i <= maxUserLevel));
        }
        endlessMode = new TextButton("ENDLESS MODE", textButtonFieldLabelSkin);
        userButton = new TextButton("USERS", textButtonFieldLabelSkin);
        userNameLabel = new Label("", justTextSkin);

        float pad = 15f;

        // MONEY TABLE
        moneyTable.top().right();
        moneyTable.add(moneyButton).size(120f).padTop(pad).padRight(pad); //padLeft(GameApp.WIDTH - 240f - moneyButton.getWidth() - 30f)
        addActor(moneyTable);

        // USERS TABLE
        howdyTable.top().left();
        howdyTable.add(userNameLabel).padTop(pad).padLeft(25f);
        addActor(howdyTable);

        // MAIN MENU TABLE
        mainMenuTable.add(new Image(new Texture(Gdx.files.internal("gameLogo.png")))).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(playButton).width(defaultButtonWidth*2).height(defaultButtonHeight).padTop(pad).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(shopButton).width(defaultButtonWidth*2).height(defaultButtonHeight).padTop(pad).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).width(defaultButtonWidth-pad).height(defaultButtonHeight).padTop(pad).align(Align.left);
        mainMenuTable.add(userButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(pad).align(Align.right);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(defaultButtonWidth -pad).height(defaultButtonHeight).padTop(pad).align(Align.left);
        mainMenuTable.add(quitButton).width(defaultButtonWidth).height(defaultButtonHeight).padTop(pad).align(Align.right);
        addActor(mainMenuTable);


        // LEVEL SCROLL TABLE AND SCROLLER
        scrollTable.add(backButton).padTop(pad).colspan(1).align(Align.left); //removed .size(defaultButtonWidth, defaultButtonHeight)
        Label titleLevelChooser = new Label("LEVELS", justTextSkin);
        titleLevelChooser.setFontScale(2);
        scrollTable.add(titleLevelChooser).align(Align.right).colspan(3);

        scrollTable.row();
        scrollTable.add(endlessMode).width(defaultButtonWidth).height(defaultButtonHeight).padTop(pad).colspan(1);
        scrollTable.add(new Label("BEST SCORE: " + Math.round(llamaUtil.getLlamaDbHandler().getLevelBestScore(currentUser,-1) * 10) / 10, justTextSkin)).colspan(3);
        for (int i = 0; i < numLevels; ++i) {
            scrollTable.row();
            scrollTable.add(levelButtons[i]).size(defaultButtonWidth, defaultButtonHeight).padTop(pad);
            int starNum = llamaUtil.getLlamaDbHandler().getLevelStarNum(currentUser, i);
            for (int j = 0; j < 3; ++j) {
                Image star;
                if (j < starNum) {
                    star = new Image(llamaAssetManager.getTexture("starWon"));
                } else {
                    star = new Image(llamaAssetManager.getTexture("starLost"));
                }
                scrollTable.add(star).size(100f, 100f).padLeft(15f);
            }

        }
        scrollPane = new ScrollPane(scrollTable);
        levelTable.setFillParent(true);
        levelTable.add(scrollPane).fill().expand();
        levelTable.setVisible(false);
        addActor(levelTable);

        // adds the button's listeners
        setUpButtons();

    }

    void setUpButtons() {
        Gdx.input.setInputProcessor(this);

        userButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new UserScreen(llamaUtil));
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addAction(Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        llamaUtil.getAssetManager().loadGameTexturesSkins();
                        llamaUtil.getAssetManager().loadGameMusic();
                    }
                }));

                mainMenuTable.setVisible(false);
                moneyTable.setVisible(false);
                howdyTable.setVisible(false);
                levelTable.setVisible(true);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(llamaUtil));
            }
        });

        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ShopScreen(llamaUtil));
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new CreditScreen(llamaUtil));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                llamaUtil.getAssetManager().disposeAll();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    llamaUtil.getAssetManager().unloadGameTexturesSkins();
                } catch (GdxRuntimeException e) {
                    System.out.println("Game assets aren't totally loaded yet, loaded: " + llamaUtil.getAssetManager().getProgress() * 100 + "%");
                }

                mainMenuTable.setVisible(true);
                howdyTable.setVisible(true);
                moneyTable.setVisible(true);
                levelTable.setVisible(false);
            }
        });

        for (int i = 0; i < numLevels; ++i) {
            final int finalI = i;
            if (!levelButtons[i].isDisabled()) {
                levelButtons[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        hasToGoGameScreen = true;

                        if (llamaUtil.getAssetManager().update()) {
                            llamaUtil.getMusicManager().stopMusic(MAIN_MENU_MUSIC);
                            llamaUtil.getMusicManager().setGameMusic();

                            llamaUtil.getAssetManager().unloadMainMenuMusic();

                            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(llamaUtil, finalI));
                        }
                    }
                });
            }
        }

        endlessMode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (llamaUtil.getAssetManager().update()) {
                    llamaUtil.getMusicManager().stopMusic(MAIN_MENU_MUSIC);
                    llamaUtil.getMusicManager().setGameMusic();

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(llamaUtil, -1));
                }
            }
        });
    }

    public void renderer() {
        super.renderer();
        llamaUtil.getAssetManager().update();

        userNameLabel.setText("Howdy, " + currentUser);
        moneyButton.setText("" + currentUser);
    }

    public void resizer() {

        moneyTable.invalidateHierarchy();
        moneyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        howdyTable.invalidateHierarchy();
        howdyTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        levelTable.invalidateHierarchy();
        levelTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        scrollTable.invalidateHierarchy();
        scrollTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

    }

}
