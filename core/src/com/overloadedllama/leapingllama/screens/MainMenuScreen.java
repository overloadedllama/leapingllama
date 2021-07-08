package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;

import com.overloadedllama.leapingllama.stages.MainMenuStage;

public class MainMenuScreen implements Screen {
    private final GameApp game;

    OrthographicCamera camera;
    ExtendViewport viewport;

    private MainMenuStage mainMenuStage;

    public MainMenuScreen(final GameApp game) {
        this.game = game;
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();

    }

    @Override
    public void show() {
        mainMenuStage = new MainMenuStage(game);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        mainMenuStage.renderer();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        mainMenuStage.resizer();

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

    }

}
