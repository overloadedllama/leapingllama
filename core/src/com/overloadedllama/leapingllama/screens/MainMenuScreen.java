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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    private Table mainMenuTable;

    private TextButton settingButton;
    private Skin settingButtonSkin;

    private TextButton playButton;
    private Skin playButtonSkin;

    private TextButton creditsButton;
    private Skin creditsButtonSkin;

    public MainMenuScreen(final GameApp game) {
        this.game = game;
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

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

        mainMenuTable = new Table();

        // creation of Play Button
        playButtonSkin = new Skin(Gdx.files.internal("text_button/text_button.json"),
                                   new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        playButton = new TextButton("play", playButtonSkin);

        // creation of creditsButton
        creditsButtonSkin = playButtonSkin;
        creditsButton = new TextButton("credits", creditsButtonSkin);

        // creation of Settings Button
        settingButtonSkin = playButtonSkin;
        settingButton = new TextButton("settings", settingButtonSkin);

        // add the button to the mainMenuTable and next it to the mainMenuStage
        mainMenuTable.add(playButton).width(260F).height(120F);
        mainMenuTable.row();
        mainMenuTable.add(creditsButton).width(260F).height(120F).padTop(10F);
        mainMenuTable.row();
        mainMenuTable.add(settingButton).width(260F).height(120F).padTop(10F);

        mainmenuStage.addActor(mainMenuTable);

        Gdx.input.setInputProcessor(mainmenuStage);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingScreen(game));
            }
        });

        /*
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
        */



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

        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        /*
        mainmenuTablePlay.invalidateHierarchy();
        mainmenuTablePlay.setSize(GameApp.WIDTH, GameApp.HEIGHT);

        settingTable.invalidateHierarchy();
        */
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

        /*
        mainmenuSkinPlay.dispose();
        settingSkin.dispose();
        */
    }
}
