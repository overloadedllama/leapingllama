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
    private Table upperTable;
    private Table itemListTable;

    float userMoney;

    // ImageButton
    private ImageButton backButton;

    // TextButtons
    private TextButton previousItem;
    private TextButton nextItem;

    // Labels
    private Label userMoneyText;
    private Label item;

    // Skins
    private Skin backButtonSkin;
    private Skin coinLabelSkin;
    private Skin ammunitionSkin;
    private Skin prevNextSkin;

    public ShopScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        shopStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        upperTable = new Table();
        itemListTable = new Table();

        backButtonSkin = assets.getSkin("backButton");
        coinLabelSkin = assets.getSkin("coin");
        prevNextSkin = assets.getSkin("bigButton");
        ammunitionSkin = assets.getSkin("ammo");

        backButton = new ImageButton(backButtonSkin);
        userMoneyText = new Label("money: " + userMoney, coinLabelSkin);
        userMoneyText.setAlignment(Align.center);
        previousItem = new TextButton("previous", prevNextSkin);
        previousItem.setDisabled(true);
        nextItem = new TextButton("next", prevNextSkin);
        nextItem.setDisabled(true);
        item = new Label("", ammunitionSkin);

        float pad = 15f;
        float itemHeight = 140f;

        upperTable.top().left();
        upperTable.add(backButton).width(260F).height(itemHeight);
        upperTable.add(userMoneyText).width(140f).height(itemHeight).padLeft(GameApp.WIDTH - 400f - pad);

        itemListTable.center();
        itemListTable.add(previousItem).width(200f).height(100f).padRight(15f);
        itemListTable.add(item).width(400f).height(200f);
        itemListTable.add(nextItem).width(200f).height(100f).padLeft(15f);

        shopStage.addActor(itemListTable);
        shopStage.addActor(upperTable);

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

        upperTable.invalidateHierarchy();
        upperTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        itemListTable.invalidateHierarchy();
        itemListTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
