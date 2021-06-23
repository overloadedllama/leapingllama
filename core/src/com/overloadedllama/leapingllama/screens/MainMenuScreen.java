package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;

public class MainMenuScreen implements Screen {

    final GameApp game;

    private Texture logo;
    OrthographicCamera camera;
    ExtendViewport viewport;

    private Stage mainmenuStage;

    private ImageButton mainmenuimagebuttonPlay;
    private Table mainmenuTablePlay;
    private Skin mainmenuSkinPlay;

    private ImageButton settingButton;
    private Table settingTable;
    private Skin settingSkin;

    private ImageButton quitButton;
    private Table quitTable;
    private Skin quitSkin;

    public MainMenuScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);
        camera.update();
    }

    @Override
    public void show() {

        logo = new Texture(Gdx.files.internal("logo.png"));
        logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        mainmenuStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));

        // create the play button
        mainmenuTablePlay = new Table();
        mainmenuSkinPlay = new Skin(Gdx.files.internal("skins/play.json"), new TextureAtlas(Gdx.files.internal("mainMenu/mainMenuPack.atlas")));
        mainmenuimagebuttonPlay = new ImageButton(mainmenuSkinPlay);
        mainmenuTablePlay.bottom().add(mainmenuimagebuttonPlay).size( 152F, 164F).padBottom(20F);
        mainmenuStage.addActor(mainmenuTablePlay);

        // create the setting button
        settingTable = new Table();
        settingSkin = new Skin(Gdx.files.internal("skins/setting.json"), new TextureAtlas(Gdx.files.internal("settings/settingIconPack.atlas")));
        settingButton = new ImageButton(settingSkin);
        settingTable.bottom().add(settingButton).size(152F, 164F).padBottom(550F).padLeft(2200F);
        mainmenuStage.addActor(settingTable);

        /*
        // create the quit button
        quitTable = new Table();
        quitSkin = new Skin(Gdx.files.internal("skins/quit.json"), new TextureAtlas(Gdx.files.internal("quit/quitPack.atlas")));
        quitButton = new ImageButton(quitSkin);
        quitTable.bottom().add(quitButton).size(152F, 164F).padBottom(20F);
        mainmenuStage.addActor(quitTable);
*/
        Gdx.input.setInputProcessor(mainmenuStage);

        mainmenuimagebuttonPlay.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });


        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new SettingScreen(game));
            }
        });

        /*
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // close the app (is this really necessary?)
            }
        });*/
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        mainmenuStage.act();
        mainmenuStage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font.setColor(0 , 255, 0, 1);
        game.batch.draw(logo,
                (float) logo.getWidth()/8,  (GameApp.HEIGHT/3)*2 - (float) logo.getHeight()/8,
                (float) logo.getWidth()/4,  (float) logo.getHeight()/4);
        //the divisions for 4 in the x and y above are due to the resize of the w and h

        game.font.draw(game.batch, "Tap the button to begin!", 2, 50);

        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        mainmenuTablePlay.invalidateHierarchy();
        mainmenuTablePlay.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        settingTable.invalidateHierarchy();
        //settingTable.setSize(1280, 720);

        //quitTable.invalidateHierarchy();
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
        mainmenuStage.dispose();

        mainmenuSkinPlay.dispose();

        settingSkin.dispose();

       // quitSkin.dispose();
    }
}
