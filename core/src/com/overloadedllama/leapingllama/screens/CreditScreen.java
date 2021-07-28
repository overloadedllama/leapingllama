package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Sky;
import com.overloadedllama.leapingllama.resources.Settings;


public class CreditScreen extends MyAbstractScreen{
    Stage stage;
    Table mainTable, creditTable;
    Label credits, title;
    Batch batch;
    Button buttonBack;
    Button buttonGooglePlay;
    Button buttonGithub;

    Sky sky;

    public CreditScreen(GameApp gameApp) {
        super(gameApp, GameApp.WIDTH, GameApp.HEIGHT);
    }


    @Override
    public void show() {
        String longCreditString = "LEAPING LLAMA IS DEVELOPED BY OVERLOADED LLAMA, A SOFTWARE-HOUSE BASED IN ITALY RUN BY JACK SALICI AND GIOVANNI M.\n\nTHE GAME IS DEVELOPED IN JAVA USING LIBGDX. THE MUSIC AND THE SOUNDS ARE TAKEN FROM ZAPSLAT.COM.";

        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);

        sky = new Sky(assets.getTexture("sky"));
        sky.setXSky(Settings.getXSky());

        credits = new Label(longCreditString, assets.getSkin("justText"));
        credits.setWrap(true);
        credits.setAlignment(Align.center);
        credits.setSize(GameApp.WIDTH/2, GameApp.HEIGHT/1.5f);


        buttonGooglePlay = new TextButton("GIVE US 5 STARS", assets.getSkin("bigButton"));
        buttonGithub = new TextButton("COLLAB ON GITHUB", assets.getSkin("bigButton"));

        buttonGooglePlay.setDisabled(true);

        mainTable = new Table();
        mainTable.setFillParent(true);

        //mainTable.setDebug(true);

        creditTable = new Table();
        creditTable.add(credits).width(GameApp.WIDTH/2).height(GameApp.HEIGHT/1.5f).space(20, 20, 20, 20);
        creditTable.background(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/bigTextPane.png")))));

        title = new Label("CREDITS", assets.getSkin("justText"));
        title.setFontScale(2f);

        buttonBack = new ImageButton(assets.getSkin("backButton"));

        mainTable.add(title);
        mainTable.add(buttonBack).padBottom(5).align(Align.right);

        mainTable.row();
        mainTable.add(creditTable).width(GameApp.WIDTH/2).colspan(2).padBottom(15);

        mainTable.row();
        mainTable.add(buttonGooglePlay).width(GameApp.WIDTH/4 - 7).align(Align.left);
        mainTable.add(buttonGithub).width(GameApp.WIDTH/4 -8).align(Align.right);

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);


        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(gameApp));

                dispose();

            }
        });

        buttonGithub.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI("https://github.com/overloadedllama/leapingllama");

            }
        });

    }

    @Override
    public void render(float delta) {

        camera.update();
        gameApp.batch.setProjectionMatrix(camera.combined);

        batch.begin();
        sky.draw(batch, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.act();
        stage.draw();

        sky.update();
        Settings.setXSky(sky.getXSky());

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
        stage.dispose();
    }
}
