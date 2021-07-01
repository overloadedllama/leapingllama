package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.contactlistener.MyContactListener;
import com.overloadedllama.leapingllama.game.*;

import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.overloadedllama.leapingllama.GameApp.HEIGHT;
import static com.overloadedllama.leapingllama.GameApp.WIDTH;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen{

    public enum State
    {
        PAUSE,
        RUN,
        STOPPED
    }


    private State state = State.RUN;


    OrthographicCamera camera;
    Viewport viewport;
    GameApp game;

    ButtonsStagePlay stageUi;
    World world;

    Texture sky;
    float xSky = 0;
    HashMap<String, Boolean> actions;

    Llama llama;
    Ground ground;
    static ArrayList<Enemy> enemies;
    static ArrayList<Bullet> bullets;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    static float  UNITS_PER_METER = 128;

    public final float METER_WIDTH = WIDTH / UNITS_PER_METER;
    public final float METER_HEIGHT = HEIGHT / UNITS_PER_METER;

    long timeLastEnemies;
    long timePunching;


    float accumulator = 0;



    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.position.set(METER_WIDTH / 2, METER_HEIGHT / 2, 5);

        viewport = new ExtendViewport(METER_WIDTH, METER_HEIGHT, camera);
        viewport.apply();

        enemies = new ArrayList<>();
        timeLastEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

        sky = new Texture(Gdx.files.internal("sky.png"));
        sky.setWrap(Repeat,Repeat);

        actions = new HashMap<>();

    }

    public void removeBullet(Bullet b) {
        bullets.remove(b);
        final Body toRemove = b.getBody();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                world.destroyBody(toRemove);
            }
        });
    }

    public void removeEnemy(Enemy e) {
        enemies.remove(e);
        final Body toRemove = e.getBody();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                world.destroyBody(toRemove);
            }
        });
    }

    public void gameOver() {
       // game.setScreen(new GameOverScreen(game));
       // dispose();
    }


    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener(this));



        llama = new Llama(3, 1, 2, world, game.batch);
        ground = new Ground( -1, 0, 0.6f, world, game.batch);
        ground.setMyW(METER_WIDTH);



        stageUi = new ButtonsStagePlay();
        stageUi.setUpButtonAction();

        //stagePause = new ButtonsStagePause();

        enemies=new ArrayList<>();
        enemies.add(new Enemy(METER_WIDTH, 1, 1.2f, world, game.batch));
        timeLastEnemies = System.currentTimeMillis();

        bullets = new ArrayList<>();
    }

    @Override
    public void render(float delta)     {
        ScreenUtils.clear(0.56f, 0.73f, 0.8f, 1);

        game.batch.begin();
        game.batch.draw(sky,
                // position and size of texture
                -1, 0, viewport.getScreenWidth()/UNITS_PER_METER +2, METER_HEIGHT,
                // srcX, srcY, srcWidth, srcHeight
                (int) xSky, 0, sky.getWidth(), sky.getHeight(),
                // flipX, flipY
                false, false);

        llama.draw();
        ground.draw();
        for (Bullet bullet : bullets)
            bullet.draw();
        for (Enemy enemy : enemies)
            enemy.draw();
        game.batch.end();


        stageUi.drawer();


        actions = stageUi.getActions();
        //System.out.println(actions);

        if (actions.get("shot")) {
            shot();
        }

        if (actions.get("jump")){
            jump();
        }

        if (actions.get("crouch")){
            //System.out.println("LLAMA CROUCHES!");
            crouch();
        }

        if (actions.get("pause")){
            pause();
        }

        if (actions.get("punch")){
            if (llama.isStanding()) {
                punch();
            }
        }

        if(actions.get("play")){
            resume();
        }

        if(actions.get("exit")){
            exit();
        }

        stageUi.setActions(actions);
        debugRenderer.render(world, camera.combined);

        switch(state) {

            case RUN:


                stepWorld();

                xSky += 1;
                // ground.setPosition(ground.getSprite().getX()-1, ground.getSprite().getY());

                if(System.currentTimeMillis()-timeLastEnemies>2000){
                    //enemies.add(new Enemy( METER_WIDTH, 2,  1.2f, world, game.batch));
                    timeLastEnemies = System.currentTimeMillis();
                    System.out.println("Enemy Created");
                }

                if (timePunching != 0) {
                    if (System.currentTimeMillis() - timePunching > 300 && llama.isStanding()){
                        llama.depunch();
                    }
                }

                llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());

                for (Enemy enemy : enemies) {
                    enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
                }

                for (Bullet bullet : bullets) {
                    bullet.setPosition(bullet.getBody().getPosition().x, bullet.getBody().getPosition().y, bullet.getBody().getAngle());
                }


                break;

            case PAUSE:



                break;

            case STOPPED:




                break;

        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

    }


    private void crouch() {
        if (llama.isStanding()) {
            // crouches
            world.destroyBody(llama.getBody());
            llama = new Llama(llama.getX(), llama.getY(), llama.getH() / 2, world, game.batch, new Texture(Gdx.files.internal("llamaCrouching.png")));
            llama.setStanding(false);
            actions.put("crouch", false);
        } else {
            // stands up
            world.destroyBody(llama.getBody());
            llama = new Llama(llama.getX(), llama.getY(), 2, world, game.batch, new Texture(Gdx.files.internal("llamaStanding.png")));
            llama.setStanding(true);
            actions.put("crouch", false);
        }
    }

    private void punch() {
        llama.punch();
        timePunching=System.currentTimeMillis();
        actions.remove("punch");
        actions.put("punch", false);

    }

    private void shot() {
        bullets.add(new Bullet(llama.getX() + llama.getW(), 1.5f, 0.1f, world, game.batch));
        actions.remove("shot");
        actions.put("shot", false);
    }

    private void exit() {
        //here there will be to develop the stuff for save the checkpoint;
        actions.remove("exit");
        actions.put("exit", false);

        dispose();
        game.setScreen(new MainMenuScreen(game));



    }

    private void jump(){
        llama.jump();
        actions.remove("jump");
        actions.put("jump", false);
    }

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

        stageUi.resizer();
    }

    @Override
    public void pause() {

        state = State.PAUSE;

        actions.remove("pause");
        actions.put("pause", false);
    }

    @Override
    public void resume() {
        state = State.RUN;

        actions.remove("play");
        actions.put("play", false);
    }

    @Override
    public void hide() {
       pause();
    }

    @Override
    public void dispose() {
        stageUi.dispose();
        //world.dispose();

    }
}