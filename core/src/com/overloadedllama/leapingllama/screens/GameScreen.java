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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.Bullet;
import com.overloadedllama.leapingllama.game.Enemy;
import com.overloadedllama.leapingllama.game.Ground;
import com.overloadedllama.leapingllama.game.Llama;
import com.overloadedllama.leapingllama.screens.ButtonsStage;

import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.overloadedllama.leapingllama.GameApp.HEIGHT;
import static com.overloadedllama.leapingllama.GameApp.WIDTH;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen{

    OrthographicCamera camera;
    Viewport viewport;
    GameApp game;

    ButtonsStage uistage;

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

    float timeBetweenEnemies;
    float accumulator = 0;


    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.position.set(METER_WIDTH / 2, METER_HEIGHT / 2, 5);

        viewport = new FitViewport(METER_WIDTH, METER_HEIGHT, camera);
        viewport.apply();

        enemies = new ArrayList<>();
        timeBetweenEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

        sky = new Texture(Gdx.files.internal("sky.png"));
        sky.setWrap(Repeat,Repeat);

        actions = new HashMap<>();

    }

    public static void removeBullet(Bullet b) {
        bullets.remove(b);
    }

    public static void removeEnemy(Enemy e) {
        enemies.remove(e);
    }


    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener());



        llama = new Llama(3, 1, 2, world, game.batch);
        ground = new Ground( -1, 0, 0.6f, world, game.batch);
        ground.setMyW(METER_WIDTH);
        uistage = new ButtonsStage();
        uistage.setUpButtonAction();

        enemies=new ArrayList<>();
        enemies.add(new Enemy(METER_WIDTH, 1, 1.2f, world, game.batch));

        bullets = new ArrayList<>();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        stepWorld();

        xSky-=1;
       // ground.setPosition(ground.getSprite().getX()-1, ground.getSprite().getY());

       /* if(Gdx.graphics.getDeltaTime()-timeBetweenEnemies>0.5){
            enemies.add(new Enemy(new Texture(Gdx.files.internal("enemy.png")), METER_WIDTH, 2, 0.5f, 0.5f, world, game.batch));
            timeBetweenEnemies = Gdx.graphics.getDeltaTime();
            System.out.println("Enemy Created");
        }*/


        llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());

        for (Enemy enemy : enemies) {
            enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
        }

        for (Bullet bullet : bullets){
            bullet.setPosition(bullet.getBody().getPosition().x, bullet.getBody().getPosition().y, bullet.getBody().getAngle());
        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


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

        uistage.drawer();
        actions = uistage.getActions();
        //System.out.println(actions);

        if (actions.get("shot")) {
            shot();
        }

        if (actions.get("jump")){
            jump();
        }

        if (actions.get("crouch")){
            crouch();
        }

        if (actions.get("pause")){
            pause();
        }

        if (actions.get("fist")){
            fist();
        }

        uistage.setActions(actions);
        debugRenderer.render(world, camera.combined);


    }

    private void crouch() {
    }

    private void fist() {
    }

    private void shot() {
        bullets.add(new Bullet(llama.getX() + llama.getW(), 1.5f, 0.1f, world, game.batch));
        actions.remove("shot");
        actions.put("shot", false);
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
        uistage.dispose();
    }
}