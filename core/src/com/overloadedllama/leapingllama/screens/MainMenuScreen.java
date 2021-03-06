package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.MainMenuStage;

// todo fix screen size

public class MainMenuScreen extends MyAbstractScreen {
    private MainMenuStage mainMenuStage;

    Sky sky;
    Batch batch;

    public MainMenuScreen(LlamaUtil llamaUtil) {
        super(llamaUtil, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        mainMenuStage = new MainMenuStage(llamaUtil);
        llamaUtil.getMusicManager().playMusic(MAIN_MENU_MUSIC);

        sky = new Sky(llamaUtil.getAssetManager().getTexture("sky"));
        sky.setXSky(llamaUtil.getGameplayManager().getXSky());
        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();
        llamaUtil.getGameplayManager().setXSky(sky.getXSky());

        mainMenuStage.renderer();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
