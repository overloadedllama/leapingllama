package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.assetman.Assets;

public class LoadScreen implements Screen {

    final GameApp game;
    OrthographicCamera camera;
    ExtendViewport viewport;

    private Assets assets;
    private ClickListener clickListener;
    private boolean startLoading = false;


    private Texture logo;

    // stage and tables
    private Stage loadScreenStage;
    private Table loadScreenTable;

    // TextButton
    private TextButton launchButton;

    // TextField
    private TextField nameLabel;
    private TextField nameText;

    // Skins
    private Skin textFieldSkin;
    private Skin textButtonSkin;


    public LoadScreen(final GameApp game) {
        this.game = game;
        this.assets = game.getAssets();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();
    }

    @Override
    public void show() {
        logo = new Texture(Gdx.files.internal("logo.png"));

        loadScreenStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        loadScreenTable = new Table();
        loadScreenStage.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                assets.loadAllAssets();
                startLoading = true;
            }
        }));


        // creation of Skins
        textFieldSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));
        textButtonSkin = new Skin(Gdx.files.internal("ui/bigButton.json"), new TextureAtlas(Gdx.files.internal("ui/bigButton.atlas")));

        // creation of TextButton
        launchButton = new TextButton("Go!", textButtonSkin);
        launchButton.setDisabled(false);

        // creation of TextFields
        nameLabel = new TextField("Insert username! ", textFieldSkin);
        nameLabel.setDisabled(true);
        nameLabel.setAlignment(Align.center);
        nameText = new TextField("test", textFieldSkin);
        nameText.setDisabled(false);
        nameText.setAlignment(Align.center);

        // Adding items to loadTable
        loadScreenTable.top();
        loadScreenTable.add(nameLabel).width(280F).height(100F).padRight(15F).padTop(15F);
        loadScreenTable.add(nameText).width(280F).height(100F).padTop(15F);
        loadScreenTable.add(launchButton).width(160F).height(100F).padLeft(15F).padTop(15F);

        loadScreenStage.addActor(loadScreenTable);

        Gdx.input.setInputProcessor(loadScreenStage);

        clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                String user = nameText.getText();

                if (!Settings.existsUser(user)) {
                    Settings.insertNewUser(user);
                }

                Settings.setCurrentUser(user);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        };

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        // only if there isn't any asset on loading queue yet the button works
        if (assets.update() && startLoading) {
            launchButton.addListener(clickListener);
        }

        loadScreenStage.act();
        loadScreenStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setColor(0 , 255, 0, 1);
        game.batch.draw(logo,GameApp.WIDTH/2 - (float) logo.getWidth()/4, GameApp.HEIGHT/2 - (float) logo.getHeight()/4, (float) logo.getWidth()/2, (float) logo.getHeight()/2);
        //the divisions for 4 in the x and y above are due to the resize of the w and h
        game.batch.end();

   }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        loadScreenTable.invalidateHierarchy();
        loadScreenTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
        logo.dispose();

    }
}
