package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;
import com.overloadedllama.leapingllama.resources.ShopItem;
import com.overloadedllama.leapingllama.screens.MainMenuScreen;

public class ShopStage extends MyAbstractStage {

    int index;
    int itemValue;
    int userMoney;
    final int numItems = 2;

    private final Table upperTable;
    private final Table itemListTable;


    private final ShopItem[] shopItems;

    // ImageButton
    private final ImageButton backButton;

    // Images
    private final Image image;

    // Buttons
    private final Button previousItem;
    private final Button nextItem;
    private final TextButton buyButton;

    // Labels
    private final Label userMoneyText;
    private final Label itemCost;
    private final Label itemTitle;

    // Skins
    private final Skin backButtonSkin;
    private final Skin coinLabelSkin;
    private final Skin bigButtonSkin;
    private final Skin leftArrow;
    private final Skin rightArrow;
    private final Skin justTextSkin;


    public ShopStage(GameApp gameApp) {
        super(gameApp);

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
        setItemButtons(0);

        float pad = 15f;
        float size = 120f;

        upperTable.top().left();
        upperTable.add(backButton).size(size).padLeft(pad).padTop(pad);
        upperTable.add(userMoneyText).size(size).padLeft(GameApp.WIDTH - size * 2 - pad * 2).padTop(pad);
        addActor(upperTable);

        itemListTable.center();
        itemListTable.add(itemTitle).colspan(3).padBottom(pad);
        itemListTable.row();
        itemListTable.add(previousItem).size(100).padRight(pad);
        itemListTable.add(image).size(400f);
        itemListTable.add(nextItem).size(100).padLeft(pad);
        itemListTable.row();
        itemListTable.add(itemCost).colspan(3).padTop(pad);
        itemListTable.row();
        itemListTable.add(buyButton).colspan(3).width(200f).height(120f).padTop(pad);

        addActor(itemListTable);

        setUpButtons();
    }

    void setUpButtons() {
        Gdx.input.setInputProcessor(this);

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
                setItemButtons(index);
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
                setItemButtons(index);

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

                        setItemButtons(index);
                    }
                }
            }
        });
    }

    private void setItemButtons(int i) {
        itemValue = shopItems[i].getValue();
        itemCost.setText("ITEM COST: " + itemValue);
        itemTitle.setText(shopItems[i].getName());

        if (shopItems[i].getId().equals(BONUS_AMMO)) {
            if (Settings.hasBonusAmmo()) {
                buyButton.setDisabled(true);
                buyButton.setText("BOUGHT!");
            } else {
                buyButton.setDisabled(false);
                buyButton.setText("BUY!");
            }
        } else if (shopItems[i].getId().equals(BONUS_LIFE)) {
            if (Settings.hasBonusLife()) {
                buyButton.setDisabled(true);
                buyButton.setText("BOUGHT!");
            } else {
                buyButton.setDisabled(false);
                buyButton.setText("BUY!");
            }
        }

        if (Settings.getUserMoney() < itemValue) {
            buyButton.setDisabled(true);
            buyButton.setText("U R 2 POOR");
        }


    }

    @Override
    public void renderer() {
        super.renderer();
    }

    @Override
    public void resizer() {
        upperTable.invalidateHierarchy();
        upperTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        itemListTable.invalidateHierarchy();
        itemListTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
    }
}
