package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.LlamaConstants;

public abstract class MyAbstractScreen implements Screen, LlamaConstants {
    final GameApp gameApp;
    final Assets assets;

    OrthographicCamera camera;
    ExtendViewport viewport;

    final float SCREEN_WIDTH, SCREEN_HEIGHT;


    protected MyAbstractScreen(GameApp gameApp, float WIDTH, float HEIGHT) {
        this.gameApp = gameApp;
        this.SCREEN_WIDTH = WIDTH;
        this.SCREEN_HEIGHT = HEIGHT;

        assets = gameApp.getAssets();

        ScreenUtils.clear(0.56f, 0.72f, 0.8f, 1);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        viewport.apply();

        camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 5);
        camera.update();

    }


    @Override
    public void render(float delta) {
        camera.update();
        gameApp.batch.setProjectionMatrix(camera.combined);

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
