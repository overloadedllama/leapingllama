package com.overloadedllama.leapingllama.screens;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

    private final Image[] starArray;
    private Label scoreLabel;
    private Skin scoreLabelSkin;
    private Skin optionButtonsSkin;

    TextButton mainMenuButton, retryButton;


    public EndScreen(final GameApp gameApp, int level, double lastScore, double totalLevelScore, boolean win) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
        this.level = level;
        this.lastScore = lastScore;
        this.totalLevelScore = totalLevelScore;
        this.win = win;

        starArray = new Image[3];
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void show() {
        startTime = System.currentTimeMillis();

        endStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        endTable = new Table();

        scoreLabelSkin = assets.getSkin("justText");
        optionButtonsSkin = assets.getSkin("bigButton");

        //System.out.printf("Total level score: %.1f --- last score: %.1f%n", totalLevelScore, lastScore);

        if (level > -1) {
            if (!win) {
                scoreLabel = new Label(String.format("SCORE: %.1f - BEST SCORE LEVEL %d: %.1f", lastScore, level, Settings.getLevelBestScore(level)), scoreLabelSkin);
                if (Settings.checkSetNewUserBestScore(level, lastScore)) {
                    System.out.println("NEW BEST SCORE: " + lastScore);
                } else {
                    System.out.println("SCORE: " + lastScore);
                }
            } else {
                Settings.updateUserMaxLevel();
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

            // DATABASE OPERATION
            if (starNum > Settings.getLevelStarNum(level))
                Settings.setLevelStarNum(level, starNum);
        }

        endTable.row();
        endTable.add(scoreLabel).width(1000f).height(180f).padTop(40f).colspan(3);


        endTable.row();

        mainMenuButton = new TextButton("MAIN MENU", optionButtonsSkin);
        retryButton = new TextButton("RETRY", optionButtonsSkin);

        Table buttonTable = new Table();
        buttonTable.add(mainMenuButton);
        buttonTable.add(retryButton).padLeft(5);

        endTable.add(buttonTable).colspan(3);

        endStage.addActor(endTable);



        Gdx.input.setInputProcessor(endStage);

        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameApp.setScreen(new MainMenuScreen(gameApp));
              //  assets.unloadGameAssets();

                dispose();


                super.clicked(event, x, y);
            }
        });

        retryButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameApp.setScreen(new GameScreen(gameApp, level));
                dispose();


                super.clicked(event, x, y);
            }
        });


    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(new Color(Color.NAVY));
        ScreenUtils.clear(0.56f, 0.72f, 0.8f, 1);


        endStage.act();
        endStage.draw();

        super.render(delta);

        /*if (Gdx.input.isTouched() && (System.currentTimeMillis() - startTime) > 400) {
            endStage.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    assets.unloadGameAssets();
                }
            }));
            gameApp.setScreen(new MainMenuScreen(gameApp));
            dispose();
        }*/
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
