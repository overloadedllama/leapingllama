package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;

import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.stages.MainMenuStage;

public class MainMenuScreen extends MyAbstractScreen {
    private MainMenuStage mainMenuStage;

    public MainMenuScreen(final GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        mainMenuStage = new MainMenuStage(gameApp);
        Settings.playMusic(gameApp.getAssets().MAIN_MENU_MUSIC);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
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
