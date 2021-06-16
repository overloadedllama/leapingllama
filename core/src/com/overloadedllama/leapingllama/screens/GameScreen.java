package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;


public class GameScreen implements Screen {
    private Texture lamaImage;
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    GameApp game;

    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        camera.update();
    }

    @Override
    public void show() {
        lamaImage = new Texture(Gdx.files.internal("lamaphoto.png"));
        lamaImage.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(lamaImage,
                GameApp.WIDTH/2 - (float) lamaImage.getWidth()/4,
                GameApp.HEIGHT/2 - (float) lamaImage.getHeight()/4,
                (float) lamaImage.getWidth()/2,
                (float) lamaImage.getHeight()/2);
        game.font.draw(game.batch, "Tap the llamas!", 1000, 50);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            // back to MainMenuScreen
            // Is it necessary to create a new xScreen every time?
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        dispose();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        lamaImage.dispose();
    }
}