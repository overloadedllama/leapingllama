package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class LoadScreen extends MyAbstractScreen {

    private boolean startLoading = false;

    private Texture logo;

    // stage and tables
    private Stage loadScreenStage;
    private Table loadScreenTable;

    private ProgressBar progressBar;
    private Skin progressBarSkin;

    Sky sky;
    Batch batch;


    public LoadScreen(LlamaUtil llamaUtil) {
        super(llamaUtil, GameApp.WIDTH, GameApp.HEIGHT);
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
                llamaUtil.getAssetManager().loadBasicTexturesSkins();
                llamaUtil.getAssetManager().loadSounds();
                llamaUtil.getAssetManager().loadMainMenuMusic();
                startLoading = true;
            }
        }));


        loadScreenStage.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                llamaUtil.getLlamaDbHandler().insertNewUser(llamaUtil.getCurrentUser());
            }
        }));

        progressBarSkin = new Skin(Gdx.files.internal("ui/progressBar.json"), new TextureAtlas("ui/progressBar.atlas"));
        progressBar = new ProgressBar((float) 1, (float) 100, (float) 1, false, progressBarSkin);

        loadScreenTable.top();
        loadScreenTable.add(progressBar).height(120f).width(260f).padTop(30f);
        loadScreenStage.addActor(loadScreenTable);

        sky = new Sky(new Texture(Gdx.files.internal("world/sky.png")));
        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();


        // only if there isn't any asset on loading queue yet the button works
        if (llamaUtil.getAssetManager().update() && startLoading) {
            llamaUtil.getSoundManager().setSounds();
            llamaUtil.getMusicManager().setMainMenuMusic();
            llamaUtil.getGameApp().setScreen(new MainMenuScreen(llamaUtil));
        } else {
            progressBar.setValue(llamaUtil.getAssetManager().getProgress() * 100);
        }

        loadScreenStage.act();
        loadScreenStage.draw();

        super.render(delta);

        llamaUtil.getGameApp().batch.begin();
        llamaUtil.getGameApp().font.setColor(0 , 255, 0, 1);
        llamaUtil.getGameApp().batch.draw(logo,GameApp.WIDTH/2 - (float) logo.getWidth()/8, GameApp.HEIGHT/2 - (float) logo.getHeight()/8, (float) logo.getWidth()/4, (float) logo.getHeight()/4);
        //the divisions for 4 in the x and y above are due to the resize of the w and h
        llamaUtil.getGameApp().batch.end();

        sky.update();

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
