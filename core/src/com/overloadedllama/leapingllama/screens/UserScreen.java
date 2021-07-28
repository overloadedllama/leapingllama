package com.overloadedllama.leapingllama.screens;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.stages.UserStage;

public class UserScreen extends MyAbstractScreen {

    private UserStage userStage;
    Sky sky;
    Batch batch;

    public UserScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        userStage = new UserStage(gameApp);

        sky = new Sky(assets.getTexture("sky"));
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();

        userStage.renderer();

        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        userStage.resizer();
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
        userStage.dispose();
    }
}
