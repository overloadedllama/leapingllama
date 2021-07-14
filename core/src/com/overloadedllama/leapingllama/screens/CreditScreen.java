package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;

import java.awt.*;

public class CreditScreen extends MyAbstractScreen{
    Stage stage;
    Table mainTable;
    Label credits;
    Batch batch;

    Sky sky;

    public CreditScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }


    @Override
    public void show() {
        String longCreditString = "LEAPING LLAMA IS DEVELOPED BY OVERLOADED LLAMA, A SOFTWARE-HOUSE BASED IN ITALY RUN BY JACK SALICI AND GIOVANNI M.\n\nTHE GAME IS DEVELOPED IN JAVA USING LIBGDX.\n\nTHE MUSIC AND THE SOUNDS ARE TAKEN FROM ZAPSLAT.COM.";

        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        sky = new Sky(new Texture(Gdx.files.internal("world/sky.png")));

        credits = new Label(longCreditString, new Skin(Gdx.files.internal("ui/bigLabel.json"), new TextureAtlas(Gdx.files.internal("ui/bigLabel.atlas"))));
        credits.setWrap(true);
        credits.setAlignment(Align.center);
        credits.setSize(GameApp.WIDTH/2, GameApp.HEIGHT/1.5f);
        //credits.setColor(Color.BLACK);


        mainTable = new Table();


        mainTable.center().addActor(credits);
        mainTable.padLeft(GameApp.WIDTH/2 - mainTable.getWidth()/2);
        mainTable.padTop(GameApp.HEIGHT/2 - mainTable.getHeight()/2);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        camera.update();
        gameApp.batch.setProjectionMatrix(camera.combined);

        batch.begin();
        sky.draw(batch, viewport.getScreenWidth()/2, viewport.getScreenHeight()/2);
        batch.end();


        stage.act();
        stage.draw();

        sky.update();
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

    }
}
