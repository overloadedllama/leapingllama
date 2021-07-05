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
import java.util.Iterator;

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
    static ArrayList<Platform> platforms;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    static float  UNITS_PER_METER = 128;

    public final float METER_WIDTH = WIDTH / UNITS_PER_METER;
    public final float METER_HEIGHT = HEIGHT / UNITS_PER_METER;

    long timeLastEnemies;
    long timePunching;

    Level level;

    float accumulator = 0;

    double distance = 0;

    public GameScreen(final GameApp game, int levelNumber) {
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

        level = new Level(levelNumber);
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

        enemies = new ArrayList<>();
        //enemies.add(new Enemy(METER_WIDTH, 1, 1.2f, world, game.batch));
        timeLastEnemies = System.currentTimeMillis();

        bullets = new ArrayList<>();
        platforms = new ArrayList<>();

        platforms.add(new Platform(METER_WIDTH, 1, 0.2f, 3, world, game.batch));



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
        for (Platform platform : platforms)
            platform.draw();
        game.batch.end();


        stageUi.drawer();


        actions = stageUi.getActions();
        //System.out.println(actions);

        if (actions.get("shot")) {
            shot();
        }

        if (actions.get("jump")){
            if (!llama.isJumping())
                jump();
        }

        if (actions.get("crouch")){
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

                if (timePunching != 0) {
                    if (System.currentTimeMillis() - timePunching > 300 && llama.isStanding()){
                        llama.punch(false);
                    }
                }

                updatePosition();
                /*
                level loading
                 */
                loadLevel(distance);

                break;

        }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

    }

    private void updatePosition() {
        llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());

        for (Enemy enemy : enemies) {
            enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
        }

        System.out.println("bullets array size: " + bullets.size());
        for (Iterator<Bullet> i = bullets.listIterator(); i.hasNext();) {
            Bullet bullet = i.next();
            bullet.setPosition(bullet.getBody().getPosition().x, bullet.getBody().getPosition().y, bullet.getBody().getAngle());
            if (isOutOfBonds(bullet)) {
                final Body toRemove = bullet.getBody();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(toRemove);
                    }
                });
                i.remove();
            }
        }

        for (Platform platform : platforms){
            platform.setPosition(platform.getBody().getPosition().x, platform.getBody().getPosition().y, platform.getBody().getAngle());
            if (isOutOfBonds(platform)) {
                removeObject(platform, platforms);
            }
        }
    }

    private void loadLevel(double distance) {
        ArrayList<String> strings = level.getActor(distance);

        Iterator<String> i = strings.iterator();

        while(i.hasNext()){
            String s = i.next();
            if (s.equals("enemy")){
                enemies.add(new Enemy(METER_WIDTH, 2,  1.2f, world, game.batch));
                i.remove();
            }

        }

    }


    private void crouch() {
        if (llama.isStanding()) {
            // crouches
            llama.crouch(true);

            llama.setStanding(false);
        } else {
            // stands up
            llama.crouch(false);

            llama.setStanding(true);
        }
        actions.put("crouch", false);
    }

    private void punch() {
        llama.punch(true);
        timePunching=System.currentTimeMillis();
        actions.put("punch", false);

    }

    private void shot() {
        bullets.add(new Bullet(llama.getX()+ llama.getW()/2+ 0.1f, llama.getY()-.1f, 0.1f, world, game.batch));
        actions.put("shot", false);
    }

    private void exit() {
        //here there will be to develop the stuff for save the checkpoint;
        state = State.STOPPED;

        actions.put("exit", false);

        dispose();
        game.setScreen(new MainMenuScreen(game, null));



    }

    private void jump(){
        if (!llama.isJumping()) {
            llama.jump();
        }
        actions.put("jump", false);
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

    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        stageUi.setDistance(distance);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

            distance += .03f;
            System.out.println(distance);
        }
    }

    public void removeObject(GameObject object, ArrayList<? extends GameObject> arrayList) {
        arrayList.remove(object);
        final Body toRemove = object.getBody();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                world.destroyBody(toRemove);
            }
        });
    }

    public void removeEnemy(Enemy e) {
        removeObject(e, enemies);
    }

    public void removeBullet(Bullet b){
        removeObject(b, bullets);
    }

    public boolean isOutOfBonds(GameObject go) {
        return go.getBody().getPosition().x < 0 || go.getBody().getPosition().x > viewport.getWorldWidth();
    }



    public void gameOver() {
        // game.setScreen(new GameOverScreen(game));
        // dispose();
        System.out.println("GAME OVER");
    }

    public Llama getLlama() {
        return llama;
    }
}