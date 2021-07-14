package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.resources.Settings;

public class LoadScreen extends MyAbstractScreen {

    private boolean startLoading = false;

    private Texture logo;

    // stage and tables
    private Stage loadScreenStage;
    private Table loadScreenTable;

    private ProgressBar progressBar;
    private Skin progressBarSkin;


    public LoadScreen(final GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }

    @Override
    public void show() {
        logo = new Texture(Gdx.files.internal("logo.png"));

        loadScreenStage = new Stage(new FitViewport(GameApp.WIDTH, GameApp.HEIGHT));
        loadScreenTable = new Table();

        // loading assets and user's data/settings on background
        loadScreenStage.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                assets.loadAllAssets();
                startLoading = true;
            }
        }));

        // used just for debug now
        //Settings.resetAllProgresses();

        loadScreenStage.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                Settings.insertNewUser(Settings.TEST_USER);
            }
        }));

        progressBarSkin = new Skin(Gdx.files.internal("ui/progressBar.json"), new TextureAtlas("ui/progressBar.atlas"));
        progressBar = new ProgressBar((float) 1, (float) 100, (float) 1, false, progressBarSkin);

        loadScreenTable.top();
        loadScreenTable.add(progressBar).height(120f).width(260f).padTop(30f);
        loadScreenStage.addActor(loadScreenTable);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        // only if there isn't any asset on loading queue yet the button works
        if (assets.update() && startLoading) {
            Settings.setSoundsMusics();
            gameApp.setScreen(new MainMenuScreen(gameApp));
        } else {
            //System.out.println("PROGRESS BAR VALUE: " + assets.getProgress() * 100);
            progressBar.setValue(assets.getProgress() * 100);
        }

        loadScreenStage.act();
        loadScreenStage.draw();

        super.render(delta);

        gameApp.batch.begin();
        gameApp.font.setColor(0 , 255, 0, 1);
        gameApp.batch.draw(logo,GameApp.WIDTH/2 - (float) logo.getWidth()/4, GameApp.HEIGHT/2 - (float) logo.getHeight()/4, (float) logo.getWidth()/2, (float) logo.getHeight()/2);
        //the divisions for 4 in the x and y above are due to the resize of the w and h
        gameApp.batch.end();

   }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        progressBarSkin.dispose();
    }
}
