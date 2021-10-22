package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.utils.ScreenUtils;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.ShopStage;

public class ShopScreen extends MyAbstractScreen {


    private ShopStage shopStage;

    Sky sky;

    public ShopScreen(LlamaUtil llamaUtil) {
        super(llamaUtil, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        sky = new Sky(llamaUtil.getAssetManager().getTexture("sky"));
        sky.setXSky(llamaUtil.getGameplayManager().getXSky());

        shopStage = new ShopStage(llamaUtil);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        llamaUtil.getGameApp().batch.begin();
        sky.draw(llamaUtil.getGameApp().batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        llamaUtil.getGameApp().batch.end();

        sky.update();
        llamaUtil.getGameplayManager().setXSky(sky.getXSky());

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
