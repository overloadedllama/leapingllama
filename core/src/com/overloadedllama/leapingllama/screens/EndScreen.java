package com.overloadedllama.leapingllama.screens;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;


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

    public EndScreen(final GameApp gameApp, int level, double lastScore, double totalLevelScore, boolean win) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
        this.level = level;
        this.lastScore = lastScore;
        this.totalLevelScore = totalLevelScore;
        this.win = win;

        starArray = new Image[3];
    }

    @Override
    public void show() {
        startTime = System.currentTimeMillis();

        endStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        endTable = new Table();

        scoreLabelSkin = assets.getSkin("bigButton");
        if (!win) {
            @SuppressLint("DefaultLocale") String bestScoreText = String.format("%.1f", Settings.getLevelBestScore(level));
            scoreLabel = new Label("Best Score Level " + level + ": " + bestScoreText, scoreLabelSkin);

            if (Settings.checkSetNewUserBestScore(level, lastScore)) {
                System.out.println("NEW BEST SCORE: " + lastScore);
            } else {
                System.out.println("SCORE: " + lastScore);
            }
        } else {
            Settings.checkSetNewUserBestScore(level, totalLevelScore);
            scoreLabel = new Label("LEVEL COMPLETED!", scoreLabelSkin);
        }

        // use this to check next levels work fine
        Settings.updateUserLevel();

        scoreLabel.setFontScale(1.5f);
        scoreLabel.setAlignment(Align.center);

        if (lastScore < totalLevelScore / 3) {
            starNum = 0;
        } else {
            if (lastScore < totalLevelScore / 3 * 2) {
                starNum = 1;
            } else {
                if (lastScore < totalLevelScore - 50)
                    starNum = 2;
                else
                    starNum = 3;
            }
        }

        for (int i = 0; i < 3; ++i) {
            if (i < starNum) {
                starArray[i] = new Image(new Texture(Gdx.files.internal("world/starWon.png")));
            } else {
                starArray[i] = new Image(new Texture(Gdx.files.internal("world/starLost.png")));
            }
        }

        float starWidth = 200f;
        float starHeight = 200f;

        endTable.add(starArray[0]).width(starWidth).height(starHeight).padRight(100f);
        endTable.add(starArray[1]).width(starWidth).height(starHeight).padBottom(150f);
        endTable.add(starArray[2]).width(starWidth).height(starHeight).padLeft(100f);
        endTable.row();
        endTable.add(scoreLabel).width(600f).height(180f).padTop(40f).colspan(3);

        endStage.addActor(endTable);

        for (float i = 0; i < 3; ++i) {
            if (i <= starNum) {
                starArray[(int) i].addAction(Actions.sequence(Actions.alpha(0.0f + i / 10), Actions.fadeIn((float) (2.0 + 1 * i))));
            }
        }

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(Color.NAVY));

        endStage.act();
        endStage.draw();

        super.render(delta);

        if (Gdx.input.isTouched() && (System.currentTimeMillis() - startTime) > 400) {
            gameApp.setScreen(new MainMenuScreen(gameApp));
            dispose();
        }
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
