package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.database.LlamaDbHandler;

public class ShopScreen implements Screen {

    private final GameApp game;
    private static final String TEST_USER = "test";

    OrthographicCamera camera;
    ExtendViewport viewport;
    LlamaDbHandler llamaDbHandler;

    private int userMoney;

    private Stage shopStage;
    private Table shopTable;

    // ImageButton
    private ImageButton backButton;
    private ImageButton previousItemButton;
    private ImageButton nextItemButton;

    // TextButton
    private TextButton buyItemButton;

    // TextField
    private TextField itemDescription;
    private TextField userMoneyText;

    // Image
    private TextureArray textureArray;
    private Texture currentTexture;

    // Skin
    private Skin backButtonSkin;
    private Skin userMoneySkin;

    public ShopScreen(GameApp game) {
        this.game = game;

        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();

        llamaDbHandler = new LlamaDbHandler(game.getContext());

        llamaDbHandler.insertNewUser(TEST_USER);
        llamaDbHandler.insertSettingsNewUser(TEST_USER);
    }

    @Override
    public void show() {
        shopStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        shopTable = new Table();

        // creation of backButton
        backButtonSkin = new Skin(Gdx.files.internal("backButton/backButton.json"),
                new TextureAtlas(Gdx.files.internal("backButton/backButton.atlas")));
        backButton = new ImageButton(backButtonSkin);

        // creation of userMoney
        userMoneySkin = new Skin(Gdx.files.internal("text_field/text_field.json"),
                new TextureAtlas(Gdx.files.internal("text_field/text_field.atlas")));
        userMoney = llamaDbHandler.getUserMoney(TEST_USER);
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
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        shopStage.act();
        shopStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


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
