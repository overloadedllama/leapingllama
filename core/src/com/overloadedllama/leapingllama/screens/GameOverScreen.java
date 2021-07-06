package com.overloadedllama.leapingllama.screens;

import android.annotation.SuppressLint;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;

public class GameOverScreen implements Screen {

    private final double lastScore;

    final GameApp game;
    OrthographicCamera camera;
    ExtendViewport viewport;

    private Texture gameOver;

    private Stage gameOverStage;
    private Table gameOverTable;
    private Label scoreLabel;
    private Skin scoreLabelSkin;


    public GameOverScreen(final GameApp game, double lastScore) {
        this.game = game;
        this.lastScore = lastScore;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();

    }

    @Override
    public void show() {
        gameOver = game.getAssets().getTexture("game_over");
        if (Settings.checkSetNewUserBestScore(lastScore)) {
            System.out.println("NEW BEST SCORE: " + lastScore);
        } else {
            System.out.println("SCORE: " + lastScore);
        }

        gameOverStage = new Stage();
        gameOverTable = new Table();

        scoreLabelSkin = game.getAssets().getSkin("bigButton");
        @SuppressLint("DefaultLocale") String bestScoreText = String.format("%.1f", Settings.getUserBestScore());
        scoreLabel = new Label("Best Score: " + bestScoreText, scoreLabelSkin);
        scoreLabel.setFontScale(2f);
        scoreLabel.setAlignment(Align.center);

        gameOverTable.bottom();
        gameOverTable.add(scoreLabel).width(400f).height(140f).padBottom(30f);
        gameOverStage.addActor(gameOverTable);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        gameOverStage.act();
        gameOverStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(gameOver, GameApp.WIDTH / 2 - (float) gameOver.getWidth() / 4, GameApp.HEIGHT / 2 - (float) gameOver.getHeight() / 4,
                (float) gameOver.getWidth() / 2, (float) gameOver.getHeight() / 2);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        gameOverTable.invalidateHierarchy();
        gameOverTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
    }
}
