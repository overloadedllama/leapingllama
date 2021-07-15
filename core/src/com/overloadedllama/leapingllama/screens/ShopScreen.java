package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.resources.ShopItem;

public class ShopScreen extends MyAbstractScreen {

    private Stage shopStage;
    private Table upperTable;
    private Table itemListTable;

    int index;
    int itemValue;
    int userMoney;
    final int numItems = 2;

    private ShopItem[] shopItems;

    // ImageButton
    private ImageButton backButton;

    // Images
    private Image image;

    // TextButtons
    private TextButton previousItem;
    private TextButton nextItem;
    private TextButton buyButton;

    // Labels
    private Label userMoneyText;

    // Skins
    private Skin backButtonSkin;
    private Skin coinLabelSkin;
    private Skin bigButtonSkin;

    Sky sky;
    Batch batch;

    public ShopScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        sky = new Sky(new Texture(Gdx.files.internal("world/sky.png")));
        batch = new SpriteBatch();

        shopStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        upperTable = new Table();
        itemListTable = new Table();

        index = 0;
        shopItems = new ShopItem[2];
        shopItems[0] = new ShopItem(BONUS_AMMO, new Texture(Gdx.files.internal("world/bonusAmmo.png")), 5);
        shopItems[1] = new ShopItem(BONUS_LIFE, new Texture(Gdx.files.internal("world/heart.png")), 10);
        image = new Image(shopItems[0].getTexture());

        backButtonSkin = assets.getSkin("backButton");
        coinLabelSkin = assets.getSkin("coin");
        bigButtonSkin = assets.getSkin("bigButton");

        backButton = new ImageButton(backButtonSkin);
        userMoney = Settings.getUserMoney();
        userMoneyText = new Label("money: " + userMoney, coinLabelSkin);
        userMoneyText.setAlignment(Align.center);
        previousItem = new TextButton("previous", bigButtonSkin);
        previousItem.setDisabled(true);
        nextItem = new TextButton("next", bigButtonSkin);
        nextItem.setDisabled(true);
        itemValue = shopItems[0].getValue();
        buyButton = new TextButton("" + shopItems[0].getValue(), bigButtonSkin);
        buyButton.setDisabled(true);

        float pad = 15f;
        float itemHeight = 140f;

        upperTable.top().left();
        upperTable.add(backButton).width(260F).height(itemHeight);
        upperTable.add(userMoneyText).width(140f).height(itemHeight).padLeft(GameApp.WIDTH - 400f - pad);

        itemListTable.center();
        itemListTable.add(previousItem).width(200f).height(100f).padRight(15f);
        itemListTable.add(image).width(400f).height(400f);
        itemListTable.add(nextItem).width(200f).height(100f).padLeft(15f);
        itemListTable.row();
        itemListTable.add(buyButton).colspan(3).width(200f).height(120f).padTop(15f);

        shopStage.addActor(itemListTable);
        shopStage.addActor(upperTable);

        Gdx.input.setInputProcessor(shopStage);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));
            }
        });

        previousItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (index == 0) {
                    index = numItems - 1;
                } else {
                    index--;
                }
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(shopItems[index].getTexture())));
                itemValue = shopItems[index].getValue();
                buyButton.setText("" + itemValue);
            }
        });

        nextItem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (index == numItems - 1)
                    index = 0;
                else
                    index++;
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(shopItems[index].getTexture())));
                itemValue = shopItems[index].getValue();
                buyButton.setText("" + itemValue);
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (Settings.checkSetUserMoney(-itemValue)) {       // set itemValue negative
                    if (shopItems[index].getId().equals(BONUS_AMMO)) {
                        Settings.setBonusAmmo();
                    } else if (shopItems[index].getId().equals(BONUS_LIFE)) {
                        Settings.setBonusLife();
                    }

                    userMoneyText.setText("" + (userMoney - itemValue));
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        sky.update();

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
