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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

    // Buttons
    private Button previousItem;
    private Button nextItem;
    private TextButton buyButton;


    // Labels
    private Label userMoneyText;
    private Label itemCost;
    private Label itemTitle;



    // Skins
    private Skin backButtonSkin;
    private Skin coinLabelSkin;
    private Skin bigButtonSkin;
    private Skin leftArrow;
    private Skin rightArrow;
    private Skin justTextSkin;

    Sky sky;
    Batch batch;

    public ShopScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        sky = new Sky(assets.getTexture("sky"));
        batch = new SpriteBatch();

        shopStage = new Stage(new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT));
        upperTable = new Table();
        itemListTable = new Table();

        index = 0;
        shopItems = new ShopItem[2];
        shopItems[0] = new ShopItem("Start the next round with an ammunition bonus.", BONUS_AMMO, new Texture(Gdx.files.internal("world/bonusAmmo.png")), 5);
        shopItems[1] = new ShopItem("Get a second chance for the next round.", BONUS_LIFE, new Texture(Gdx.files.internal("world/heart.png")), 10);
        image = new Image(shopItems[0].getTexture());

        backButtonSkin = assets.getSkin("backButton");
        coinLabelSkin = assets.getSkin("coin");
        bigButtonSkin = assets.getSkin("bigButton");
        leftArrow = assets.getSkin("leftArrow");
        rightArrow = assets.getSkin("rightArrow");
        justTextSkin = assets.getSkin("justText");


        backButton = new ImageButton(backButtonSkin);
        userMoney = Settings.getUserMoney();
        userMoneyText = new Label("COINS:\n" + userMoney, coinLabelSkin);
        userMoneyText.setAlignment(Align.center);
        previousItem = new Button(leftArrow);
        nextItem = new Button(rightArrow);

        itemValue = shopItems[0].getValue();
        buyButton = new TextButton("", bigButtonSkin);


        itemCost = new Label("", justTextSkin);
        itemTitle = new Label("", justTextSkin);
        setButtons(0);



        float pad = 15f;
        float itemHeight = 140f;
        float itemWidth = 140f;

        upperTable.top().left();
        upperTable.add(backButton).size(itemWidth, itemHeight).padLeft(pad);
        upperTable.add(userMoneyText).size(itemWidth, itemHeight).padLeft(GameApp.WIDTH - itemWidth * 2 - pad * 2);
        shopStage.addActor(upperTable);

        itemListTable.center();
        itemListTable.add(itemTitle).colspan(3).padBottom(pad);
        itemListTable.row();
        itemListTable.add(previousItem).size(100).padRight(15f);
        itemListTable.add(image).width(400f).height(400f);
        itemListTable.add(nextItem).size(100).padLeft(15f);
        itemListTable.row();
        itemListTable.add(itemCost).colspan(3).padTop(15f);
        itemListTable.row();
        itemListTable.add(buyButton).colspan(3).width(200f).height(120f).padTop(15f);




        shopStage.addActor(itemListTable);

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
                setButtons(index);
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
                setButtons(index);

            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(!buyButton.isDisabled()) {
                    if (Settings.checkSetUserMoney(-itemValue)) {       // set itemValue negative
                        if (Settings.isSOUND())
                            Settings.playSound(CASH);
                        if (shopItems[index].getId().equals(BONUS_AMMO)) {
                            Settings.setBonusAmmo();
                        } else if (shopItems[index].getId().equals(BONUS_LIFE)) {
                            Settings.setBonusLife();
                        }

                        userMoneyText.setText("COINS:\n" + (userMoney - itemValue));
                        Settings.playSound("cash");

                        setButtons(index);
                    }
                }
            }
        });
    }

    private void setButtons(int i) {
        itemValue = shopItems[i].getValue();
        itemCost.setText("ITEM COST: " + itemValue);
        itemTitle.setText(shopItems[i].getName());



        if (shopItems[i].getId().equals(BONUS_AMMO)) {
            if(Settings.hasBonusAmmo()){
                buyButton.setDisabled(true);
                buyButton.setText("BOUGHT!");
            }else{
                buyButton.setDisabled(false);
                buyButton.setText("BUY!");
            }
        } else if (shopItems[i].getId().equals(BONUS_LIFE)) {
            if(Settings.hasBonusLife()){
                buyButton.setDisabled(true);
                buyButton.setText("BOUGHT!");
            }else{
                buyButton.setDisabled(false);
                buyButton.setText("BUY!");
            }
        }

        if(Settings.getUserMoney() < itemValue){
            buyButton.setDisabled(true);
            buyButton.setText("U R 2 POOR");
        }


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
