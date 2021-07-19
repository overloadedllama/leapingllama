package com.overloadedllama.leapingllama.screens;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;


public class EndScreen extends MyAbstractScreen {
    int level;
    int starNum;
    long startTime;
    double lastScore;
    double totalLevelScore;
    boolean win;

    private Stage endStage;
    private Table endTable;

    private Image[] starArray;
    private Label scoreLabel;
    private Skin scoreLabelSkin;
    private Skin optionButtonsSkin;

    TextButton mainMenuButton, retryButton, nextLevelButton;


    public EndScreen(final GameApp gameApp, int level, double lastScore, double totalLevelScore, boolean win) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
        this.level = level;
        this.lastScore = lastScore;
        this.totalLevelScore = totalLevelScore;
        this.win = win;

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void show() {
        startTime = System.currentTimeMillis();

        endStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        endTable = new Table();

        scoreLabelSkin = assets.getSkin("justText");
        optionButtonsSkin = assets.getSkin("bigButton");

        if (level > -1) {
            starArray = new Image[3];

            if (!win) {
                scoreLabel = new Label(String.format("SCORE: %.1f - BEST SCORE LEVEL %d: %.1f", lastScore, level, Settings.getLevelBestScore(level)), scoreLabelSkin);
                if (Settings.checkSetNewUserBestScore(level, lastScore)) {
                    System.out.println("NEW BEST SCORE: " + lastScore);
                } else {
                    System.out.println("SCORE: " + lastScore);
                }
            } else {
                Settings.checkSetNewUserBestScore(level, totalLevelScore);
                scoreLabel = new Label("LEVEL COMPLETED!", scoreLabelSkin);
            }
        } else {
            // check for new record in endless game mode
            if (Settings.checkSetNewUserBestScore(-1, lastScore)) {
                scoreLabel = new Label(String.format("NEW ENDLESS BEST SCORE! %.1f", lastScore), scoreLabelSkin);
            } else {
                scoreLabel = new Label(String.format("SCORE: %.1f - BEST SCORE: %.1f", lastScore, Settings.getLevelBestScore(-1)), scoreLabelSkin);
            }
        }

        // use this to check next levels work fine
        // Settings.updateUserMaxLevel();

        scoreLabel.setFontScale(1.2f);
        scoreLabel.setAlignment(Align.center);

        if (level > -1) {
            // if is a level shows also stars
            if (lastScore < totalLevelScore / 3) {
                starNum = 0;
            } else {
                if (lastScore < (totalLevelScore / 3) * 2) {
                    starNum = 1;
                } else {
                    if (lastScore < totalLevelScore - 100)
                        starNum = 2;
                    else
                        starNum = 3;
                }
            }

            System.out.printf("Total level score: %.1f --- , last score: %.1f --- starNum: %d", totalLevelScore, lastScore, starNum);

            for (int i = 0; i < 3; ++i) {
                if (i < starNum) {
                    starArray[i] = new Image(assets.getTexture("starWon"));
                } else {
                    starArray[i] = new Image(assets.getTexture("starLost"));
                }
            }

            float starWidth = 200f,  starHeight = 200f;
            endTable.add(starArray[0]).width(starWidth).height(starHeight).padRight(100f);
            endTable.add(starArray[1]).width(starWidth).height(starHeight).padBottom(150f);
            endTable.add(starArray[2]).width(starWidth).height(starHeight).padLeft(100f);

            if (starNum > 0) {
                for (float i = 0; i < 3; ++i) {
                    if (i <= starNum) {
                        System.out.printf("Star %.0f ON\n", i);
                        starArray[(int) i].addAction(Actions.sequence(Actions.alpha(0.0f), Actions.fadeIn((float) (2.0 + 1 * i))));
                    }
                }
            }

            // DB multiple operations on different thread
            endStage.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    // DATABASE OPERATIONS - update level star Number and,
                    // if player got at least 2 stars, unlocks next level
                    if (starNum > Settings.getLevelStarNum(level))
                        Settings.setLevelStarNum(level, starNum);
                    if (starNum >= 2)
                        Settings.updateUserMaxLevel();
                }
            }));

        }

        endTable.row();
        endTable.add(scoreLabel).width(1000f).height(180f).padTop(40f).colspan(3);


        endTable.row();

        mainMenuButton = new TextButton("MAIN MENU", optionButtonsSkin);
        retryButton = new TextButton("RETRY", optionButtonsSkin);
        if (win && level < MAX_LEVEL) {
            nextLevelButton = new TextButton("NEXT LEVEL", optionButtonsSkin);
        }

        Table buttonTable = new Table();
        buttonTable.add(mainMenuButton);
        buttonTable.add(retryButton).padLeft(5);
        if (win) {
            buttonTable.add(nextLevelButton).padLeft(5f);
        }

        endTable.add(buttonTable).colspan(3);

        endStage.addActor(endTable);



        Gdx.input.setInputProcessor(endStage);

        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));
                // gameApp.setScreen(new MainMenuScreen(gameApp));
                // assets.unloadGameAssets();

                dispose();

            }
        });

        retryButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, level));
                //gameApp.setScreen(new GameScreen(gameApp, level));
                dispose();

            }
        });

        if (nextLevelButton != null) {
            nextLevelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(gameApp, level + 1));
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //ScreenUtils.clear(new Color(Color.NAVY));
        ScreenUtils.clear(0.56f, 0.72f, 0.8f, 1);

        endStage.act();
        endStage.draw();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        endTable.invalidateHierarchy();
        endTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
        // todo dispose star array
    }

}
