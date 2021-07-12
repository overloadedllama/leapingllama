package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    private Label userMoneyText;

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

        backButtonSkin = assets.getSkin("backButton");
        userMoneySkin = assets.getSkin("coin");

        backButton = new ImageButton(backButtonSkin);
        userMoneyText = new Label("money: " + userMoney, userMoneySkin);
        userMoneyText.setAlignment(Align.center);

        float pad = 15f;
        float itemHeight = 140f;

        shopTable.top().left();
        shopTable.add(backButton).width(260F).height(itemHeight);
        shopTable.add(userMoneyText).width(140f).height(itemHeight).padLeft(GameApp.WIDTH - 400f - pad);

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

        super.render(delta);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

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

    }
}
