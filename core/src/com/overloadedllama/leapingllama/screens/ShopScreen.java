package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.stages.ShopStage;

public class ShopScreen extends MyAbstractScreen {


    private ShopStage shopStage;

    Sky sky;
    Batch batch;

    public ShopScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        sky = new Sky(assets.getTexture("sky"));
        sky.setXSky(Settings.getXSky());

        shopStage = new ShopStage(gameApp);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();
        Settings.setXSky(sky.getXSky());

        super.render(delta);
        shopStage.renderer();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        shopStage.resizer();

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
