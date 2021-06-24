package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Llama;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen {
    private Texture llamaImage;
  // private OrthographicCamera camera;
    private Viewport viewport;
    GameApp game;

    //private ImageButton buttonJump;
    private TextButton buttonJump;
    private Skin buttonJumpSkin;
    private Table buttonJumpTable;

    private Stage stage;
    private Llama llama;
    World world;

    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    InputEvent inputEvent;

    public GameScreen(final GameApp game) {
        this.game = game;


        llamaImage = new Texture(Gdx.files.internal("llamaphoto.png"));


        Box2D.init();
        world = new World(new Vector2(0, -10), true);

        inputEvent = new InputEvent();
    }

    @Override
    public void show() {
        //camera = new OrthographicCamera();

        viewport = new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT);
       //viewport.apply();
        //camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        //camera.update();

        stage = new Stage(viewport);
        llama = new Llama(llamaImage, 200,50,200,400);

        //buttonJumpSkin = new Skin(Gdx.files.internal("button/button_prova.json"), new TextureAtlas(Gdx.files.internal("button/button_prova.atlas")));
        //buttonJump = new ImageButton(buttonJumpSkin);

        buttonJumpSkin = new Skin(Gdx.files.internal("text_button/text_button.json"), new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        buttonJump = new TextButton("jump!", buttonJumpSkin);

        buttonJumpTable = new Table();

        buttonJumpTable.bottom().add(buttonJump).size(152F, 164F).padBottom(20F);
        stage.addActor(buttonJumpTable);


        //buttonJump.setBounds(2,2,100, 50);

        stage.addActor(llama);

        Gdx.input.setInputProcessor(stage);

        buttonJump.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tasto premuto");
            }
        });
    }

    @Override
    public void create(){


    }

    @Override
    public void render(float delta) {
       // super.render();
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        //camera.update();
        stage.act();
        stage.draw();
        stepWorld();





    }


    float accumulator = 0;

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

           world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        buttonJumpTable.invalidateHierarchy();
        buttonJumpTable.setSize(GameApp.WIDTH, GameApp.HEIGHT);
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
        world.dispose();
    }
}