package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;

public class ShopScreen extends MyAbstractScreen {

    private Stage shopStage;
    private Table shopTable;

    float userMoney;

    // ImageButton
    private ImageButton backButton;

    // TextField
    private TextField userMoneyText;

    // Skin
    private Skin backButtonSkin;
    private Skin userMoneySkin;

    public ShopScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        shopStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        shopTable = new Table();

        // creation of backButton
        backButtonSkin = new Skin(Gdx.files.internal("ui/backButton.json"),
                new TextureAtlas(Gdx.files.internal("ui/backButton.atlas")));
        backButton = new ImageButton(backButtonSkin);

        // creation of userMoney
        userMoneySkin = new Skin(Gdx.files.internal("ui/bigButton.json"),
                new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        userMoneyText = new TextField("money: " + userMoney, userMoneySkin);

        shopTable.top().left();
        shopTable.add(backButton).width(260F).height(120F);
        shopTable.top().right();
        shopTable.add(userMoneyText).width(260F).height(120F);

        shopStage.addActor(shopTable);

        Gdx.input.setInputProcessor(shopStage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        shopStage.act();
        shopStage.draw();

        camera.update();
        gameApp.batch.setProjectionMatrix(camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        shopTable.invalidateHierarchy();
        shopTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
        shopStage.dispose();

        backButtonSkin.dispose();

    }
}
