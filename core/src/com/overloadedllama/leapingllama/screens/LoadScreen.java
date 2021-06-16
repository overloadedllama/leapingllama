package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;

public class LoadScreen extends Stage implements Screen {

    final GameApp game;
    OrthographicCamera camera;
    ExtendViewport viewport;

    private Texture logo;


    public LoadScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(0 , 255, 0, 1);
        game.font.draw(game.batch, "(in case we need to load anything?)", 2, 50);
        logo = new Texture(Gdx.files.internal("logo.png"));
        game.batch.draw(logo,GameApp.WIDTH/2 - (float) logo.getWidth()/4, GameApp.HEIGHT/2 - (float) logo.getHeight()/4, (float) logo.getWidth()/2, (float) logo.getHeight()/2);
        //the divisions for 4 in the x and y above are due to the resize of the w and h
        game.batch.end();

        if (Gdx.input.isTouched()) {
            // ad esempio parte subito la schermata di gioco?
            game.setScreen(new MainMenuScreen(game));

            // game.setScreen(new Space(game));
            dispose();
        }
   }



    @Override
    public void show() {

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
        logo.dispose();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        logo.dispose();
    }
}
