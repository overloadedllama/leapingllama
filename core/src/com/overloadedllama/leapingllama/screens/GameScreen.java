package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Llama;


//this is the screen of the gameplay, i start to set up the environment.

public class GameScreen implements Screen {
    private Texture llamaImage;
  // private OrthographicCamera camera;
    private Viewport viewport;
    GameApp game;

    private Stage stage;
    private Llama llama;

    public GameScreen(final GameApp game) {
        this.game = game;



        llamaImage = new Texture(Gdx.files.internal("llamaphoto.png"));


    }

    @Override
    public void show() {
        //camera = new OrthographicCamera();

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT);
       //viewport.apply();
        //camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        //camera.update();

        stage = new Stage(viewport);
        llama = new Llama(llamaImage);


        llama.setColor(Color.RED);
        System.out.println(GameApp.WIDTH/5);
        llama.setBounds(200,50,200,400);

        stage.addActor(llama);


    }



    @Override
    public void render(float delta) {
       // super.render();
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        //camera.update();
        stage.act(delta);
        stage.draw();

        /*game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(lamaImage,
                GameApp.WIDTH/2 - (float) lamaImage.getWidth()/4,
                GameApp.HEIGHT/2 - (float) lamaImage.getHeight()/4,
                (float) lamaImage.getWidth()/2,
                (float) lamaImage.getHeight()/2);
        game.font.draw(game.batch, "Tap the llamas!", 1000, 50);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            // back to MainMenuScreen
            // Is it necessary to create a new xScreen every time?
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }*/




    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
     //   lamaImage.dispose();
    }
}