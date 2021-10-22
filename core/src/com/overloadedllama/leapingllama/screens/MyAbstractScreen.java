package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public abstract class MyAbstractScreen implements Screen, LlamaConstants {
    final LlamaUtil llamaUtil;

    OrthographicCamera camera;
    ExtendViewport viewport;

    final float SCREEN_WIDTH, SCREEN_HEIGHT;


    protected MyAbstractScreen(LlamaUtil llamaUtil, float WIDTH, float HEIGHT) {
        this.llamaUtil = llamaUtil;
        this.SCREEN_WIDTH = WIDTH;
        this.SCREEN_HEIGHT = HEIGHT;


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
        llamaUtil.getGameApp().batch.setProjectionMatrix(camera.combined);

    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
