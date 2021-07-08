package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.contactlistener.MyContactListener;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.jsonUtil.LevelParser;
import com.overloadedllama.leapingllama.jsonUtil.QueueObject;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.overloadedllama.leapingllama.GameApp.HEIGHT;
import static com.overloadedllama.leapingllama.GameApp.WIDTH;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen {

    public enum State
    {
        PAUSE,
        RUN,
        STOPPED
    }

    PriorityQueue<QueueObject> queue;
    LevelParser levelParser;
    private State state = State.RUN;


    OrthographicCamera camera;
    Viewport viewport;
    GameApp game;

    Assets assets;

    ButtonsStagePlay stageUi;
    World world;

    Texture sky;
    float xSky = 0;
    HashMap<String, Boolean> actions;

    Llama llama;
    static ArrayList<Enemy> enemies;
    static ArrayList<Bullet> bullets;
    static ArrayList<Platform> platforms;
    static ArrayList<Ground> grounds;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    public static float  UNITS_PER_METER = 128;

    public final float METER_WIDTH = WIDTH / UNITS_PER_METER;
    public final float METER_HEIGHT = HEIGHT / UNITS_PER_METER;

    long timeLastEnemies;
    long timePunching;


    float accumulator = 0;
    float stateTime = 0;

    double distance;

    float llamaX = 3;
    float velocity = 2;

    int money = 0;
    int bulletsGun = 10;


    public GameScreen(final GameApp game, int levelNumber) {
        this.game = game;
        this.assets = game.getAssets();
        camera = new OrthographicCamera();
        camera.position.set(METER_WIDTH / 2, METER_HEIGHT / 2, 5);
        viewport = new ExtendViewport(METER_WIDTH, METER_HEIGHT, camera);
        viewport.apply();

        enemies = new ArrayList<>();
        timeLastEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

        sky = game.getAssets().getTexture("sky");
        sky.setWrap(Repeat,Repeat);

        actions = new HashMap<>();

        levelParser = new LevelParser(0);
    }


    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener(this));

        llama = new Llama(llamaX, 1, 2, world, game.batch, assets);
        distance = llamaX;

        stageUi = new ButtonsStagePlay(game.getAssets());
        stageUi.setUpButtonAction();

        //stagePause = new ButtonsStagePause();

        enemies = new ArrayList<>();
        timeLastEnemies = System.currentTimeMillis();

        bullets = new ArrayList<>();
        platforms = new ArrayList<>();
        grounds = new ArrayList<>();
        // add this for debug
        //grounds.add(new Ground(0, 0, 0.6f, 20, velocity, world, game.batch, assets));

        Settings.playMusic(game.getAssets().GAME_MUSIC1);

    }

    @Override
    public void render(float delta)     {
        //ScreenUtils.clear(0.56f, 0.73f, 0.8f, 1);
        ScreenUtils.clear(new Color(Color.BLACK));
        switch(state) {

            case RUN:


                stepWorld();



                if (timePunching != 0) {
                    if (System.currentTimeMillis() - timePunching > 300 && llama.isStanding()){
                        llama.punch(false);
                    }
                }

                updatePosition();
                removeObjects();
                loadLevel(distance);
                llama.preserveX(llamaX);

                break;

        }

        game.batch.begin();
       /* game.batch.draw(sky,
                // position and size of texture
                -1, 0, viewport.getScreenWidth()/UNITS_PER_METER +2, METER_HEIGHT,
                // srcX, srcY, srcWidth, srcHeight
                (int) xSky, 0, sky.getWidth(), sky.getHeight(),
                // flipX, flipY
                false, false);


        llama.draw(stateTime);
        for (Ground ground : grounds)
            ground.draw();
        for (Bullet bullet : bullets)
            bullet.draw();
        for (Enemy enemy : enemies)
            enemy.draw();
        for (Platform platform : platforms)
            platform.draw();*/
        game.batch.end();


        stageUi.drawer();

        actions = stageUi.getActions();

        if (actions.get("shot")) {
            if (bulletsGun > 0) {
                shot();
                --bulletsGun;
                stageUi.setBullets(bulletsGun);
            }
        }

        //System.out.println("llama ...y: " + llama.getBody().getLinearVelocity());
        if (actions.get("jump")){
            // sometimes getLinearVelocity().y near -3.442763E-10...
            if (llama.getBody().getLinearVelocity().y < 0.001 && llama.getBody().getLinearVelocity().y > -0.001)
                jump();
        }

        crouch();

        if (actions.get("pause")) {
            pause();
        }

        if (actions.get("punch")) {
            if (llama.isStanding()) {
                punch();
            }
        }

        if(actions.get("play")) {
            resume();
        }

        if(actions.get("exit")){
            exit();
        }

        stageUi.setActions(actions);
        debugRenderer.render(world, camera.combined);



        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

    }


    /**
     *
     * @param distance the distance reached by llama
     */
    private void loadLevel(double distance) {

        queue = levelParser.getQueue();

        QueueObject queueObject = queue.peek();
        if (queueObject == null)
            return;

        // ???????????????????????
        if (queueObject.getX() - queueObject.getLength() < distance) {
            System.out.println("llama.X: " + llama.getX() + " - Distance: " + distance + " - " + queueObject);
            queueObject = queue.poll();
            if (queueObject == null)
                return;

            float xCreation;
            if (queueObject.getX() < METER_WIDTH) {
                xCreation = (float) queueObject.getX();
            } else {
                xCreation = (float) (METER_WIDTH + queueObject.getLength() / 2);
            }
            switch (queueObject.getClassObject()) {
                case "enemies": enemies.add(new Enemy(xCreation, 2, 1f, world, game.batch, assets)); break;
                case "grounds": grounds.add(new Ground(xCreation, 0, 0.6f, (float) queueObject.getLength(), velocity, world, game.batch, assets)); break;
                case "platformI": platforms.add(new Platform(xCreation, 2.8f, 0.4f, (float) queueObject.getLength(), velocity, world, game.batch, assets)); break;
                case "platformII": platforms.add(new Platform(xCreation, 3.8f, 0.4f, (float) queueObject.getLength(), velocity, world, game.batch, assets)); break;
            }
        }
    }

    private void crouch() {
        if (actions.get("crouch")) {
            // crouches
            if (llama.isStanding())
                llama.crouch(true);

            llama.setStanding(false);
            actions.put("crouch", true);
        } else if (!actions.get("crouch")) {
            // stands up
            if (!llama.isStanding())
                llama.crouch(false);

            llama.setStanding(true);
            actions.put("crouch", false);
        }
    }

    private void jump(){
        llama.jump();

        actions.put("jump", false);
    }

    private void punch() {
        llama.punch(true);
        timePunching=System.currentTimeMillis();
        actions.put("punch", false);

    }

    private void shot() {
        bullets.add(new Bullet(llama.getX()+ llama.getW()/2+ 0.1f, llama.getY()-.1f, 0.1f, world, game.batch, game.getAssets()));
        Settings.playSound("shot");
        actions.put("shot", false);
    }

    private void exit() {
        //here there will be to develop the stuff for save the checkpoint;
        state = State.STOPPED;

        actions.put("exit", false);

        Settings.stopMusic(game.getAssets().GAME_MUSIC1);
        dispose();
        game.setScreen(new MainMenuScreen(game));

    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);


        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

            distance += .05f;
            stageUi.setDistance(distance);
            stateTime += delta;
            xSky += 0.1;

        }
    }

    private void updatePosition() {
        llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());

        for (Enemy enemy : enemies) {
            enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
        }

        for (Bullet bullet : bullets) {
            bullet.setPosition(bullet.getBody().getPosition().x, bullet.getBody().getPosition().y, bullet.getBody().getAngle());
            if (isOutOfBonds(bullet)) {
                bullet.setDestroyable(true);
            }
        }

        for (Platform platform : platforms){
            platform.setPosition(platform.getBody().getPosition().x, platform.getBody().getPosition().y, platform.getBody().getAngle());
            if (isOutOfBonds(platform)) {
                platform.setDestroyable(true);
            }
        }

        for(Ground ground : grounds){
            ground.setPosition(ground.getBody().getPosition().x, ground.getBody().getPosition().y, ground.getBody().getAngle());

        }
    }

    private void removeObjects() {
        // maybe with an Enum or something else is possible to collapse all for(...) in one
        for (Iterator<Bullet> b = bullets.iterator(); b.hasNext(); ) {
            final Bullet bullet = b.next();
            if (bullet.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(bullet.getBody());
                    }
                });
                b.remove();
            }
        }

        for (Iterator<Enemy> e = enemies.iterator(); e.hasNext(); ) {
            final Enemy enemy = e.next();
            if (enemy.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(enemy.getBody());
                    }
                });
                e.remove();
            }
        }

        for (Iterator<Platform> p = platforms.iterator(); p.hasNext(); ) {
            final Platform platform = p.next();
            if (platform.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(platform.getBody());
                    }
                });
                p.remove();
            }
        }
    }

    /**
     * for Enemy and Bullets checks if they are also felt out of ground
     * @param go the object checked
     * @param <T> accepts all objects which extend GameObject class
     * @return true if the Object is out of bounds, else false
     */
    public <T extends GameObject> boolean isOutOfBonds(T go) {


        if (go instanceof Enemy || go instanceof Bullet) {
            return go.getBody().getPosition().x < -viewport.getWorldWidth() ||
                    go.getBody().getPosition().x > viewport.getWorldWidth() * 2 ||
                    go.getBody().getPosition().y < 0;
        } /*else if (go instanceof Platform || go instanceof Ground) {
            return go.getBody().getPosition().x + go.getW() < -viewport.getWorldWidth();
        }
         */

        return false;
    }

    public Llama getLlama() {
        return llama;
    }

    public void gameOver() {
        /*
        game.setScreen(new GameOverScreen(game, distance));
        Settings.stopMusic("gameMusic");
        dispose();
        */
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
}