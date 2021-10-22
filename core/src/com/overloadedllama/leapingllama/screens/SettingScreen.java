package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.SettingStage;


public class SettingScreen extends MyAbstractScreen {

    private SettingStage settingStage;

    //Sky
    Sky sky;
    Batch batch;

    public SettingScreen(LlamaUtil llamaUtil) {
        super(llamaUtil, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        sky = new Sky(llamaUtil.getAssetManager().getTexture("sky"));
        sky.setXSky(llamaUtil.getGameplayManager().getXSky());

        settingStage = new SettingStage(llamaUtil);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();
        llamaUtil.getGameplayManager().setXSky(sky.getXSky());

        super.render(delta);
        settingStage.renderer();
        llamaUtil.getGameApp().batch.begin();
        llamaUtil.getGameApp().font.setColor(0 , 255, 0, 1);
        llamaUtil.getGameApp().batch.end();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        settingStage.resizer();

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

    }
}
