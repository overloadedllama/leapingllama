package com.overloadedllama.leapingllama;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenMainMenu implements Screen {

    final GameApp game;
    OrthographicCamera camera;

    public ScreenMainMenu(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(0 , 255, 0, 1);
        game.font.draw(game.batch, "Tap anywhere to begin!", 20, 300);
        game.batch.draw((new Texture(Gdx.files.internal("logo.png"))),20, 20 );
        game.batch.end();

        if (Gdx.input.isTouched()) {
           // game.setScreen(new Space(game));
            dispose();
        }



    }



    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

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
