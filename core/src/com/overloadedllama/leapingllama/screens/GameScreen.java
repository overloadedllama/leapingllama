package com.overloadedllama.leapingllama.screens;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Enemy;
import com.overloadedllama.leapingllama.game.Ground;
import com.overloadedllama.leapingllama.game.Llama;

import java.util.ArrayList;

import static com.overloadedllama.leapingllama.GameApp.HEIGHT;
import static com.overloadedllama.leapingllama.GameApp.WIDTH;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen{

    OrthographicCamera camera;
    Viewport viewport;
    GameApp game;

    ButtonsStage uistage;

    World world;



    Llama llama;
    Ground ground;
    ArrayList<Enemy> enemies;
    Enemy enemy;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;


    static float  UNITS_PER_METER = 128;


    float METER_WIDTH = WIDTH / UNITS_PER_METER;
    float METER_HEIGHT = HEIGHT / UNITS_PER_METER;

    float timeBetweenEnemies;


    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.position.set(METER_WIDTH / 2, METER_HEIGHT / 2, 5);

        viewport = new ExtendViewport(METER_WIDTH, METER_HEIGHT, camera);
        viewport.apply();

        enemies = new ArrayList<Enemy>();
        timeBetweenEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

    }






    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);

        uistage = new ButtonsStage(viewport, METER_WIDTH, METER_HEIGHT);




        //buttonJump.setPosition(0.1f, 0.1f);
        //buttonJump.getLabel().setFontScale(0.005f);
        //buttonJump.setScale(0.005f);


        //buttonJump.setZIndex(3);



        Texture llamaTexture = new Texture(Gdx.files.internal("llamaStanding.png"));
        float desiredLlamaHeight = 2; //2 meters
        llama = new Llama(llamaTexture, 3, 1, desiredLlamaHeight / llamaTexture.getHeight() * llamaTexture.getWidth(), desiredLlamaHeight, world, game.batch);
        ground = new Ground(new Texture(Gdx.files.internal("wall.jpg")), 0, 0, camera.viewportWidth*2, 0.3f, world, game.batch);
        enemy = new Enemy(new Texture(Gdx.files.internal("enemy.png")), METER_WIDTH, 2, 0.5f, 0.5f, world, game.batch);





    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        stepWorld();


       /* if(Gdx.graphics.getDeltaTime()-timeBetweenEnemies>0.5){
            enemies.add(new Enemy(new Texture(Gdx.files.internal("enemy.png")), METER_WIDTH, 2, 0.5f, 0.5f, world, game.batch));
            timeBetweenEnemies = Gdx.graphics.getDeltaTime();
            System.out.println("Enemy Created");
        }*/

        llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());

        //for (Enemy e : enemies) {
        enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
        //}

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        uistage.drawer();


        game.batch.begin();
        llama.draw();
        ground.draw();
        // for (Enemy e : enemies) {
        enemy.draw();
        //}
        game.batch.end();

        uistage.getButtonJump().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Jump button pressed");
                llama.jump();
            }
        });


        debugRenderer.render(world, camera.combined);



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
        //game.batch.setProjectionMatrix(camera.combined);

        uistage.resizer();
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