package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.stages.SettingStage;


public class SettingScreen extends MyAbstractScreen {

    private SettingStage settingStage;

    //Sky
    Sky sky;
    Batch batch;

    public SettingScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        sky = new Sky(assets.getTexture("sky"));
        sky.setXSky(Settings.getXSky());

        settingStage = new SettingStage(gameApp);
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
        settingStage.renderer();
        gameApp.batch.begin();
        gameApp.font.setColor(0 , 255, 0, 1);
        gameApp.batch.end();

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
