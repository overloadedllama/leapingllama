package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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

import java.util.*;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.overloadedllama.leapingllama.GameApp.HEIGHT;
import static com.overloadedllama.leapingllama.GameApp.WIDTH;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen extends ApplicationAdapter implements Screen, TestConstant {

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
    static ArrayList<Obstacle> obstacles;

    static  ArrayList<Coin> coins;
    static ArrayList<Ammo> ammos;

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

    double score;
    double distance;
    double levelLength;

    float llamaX = 3;
    float velocity = 2;

    int levelNumber;

    int money = 0;
    int bulletsGun = 0;


    // METHODS

    public GameScreen(final GameApp game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
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

        levelParser = new LevelParser(levelNumber);
        levelLength = levelParser.getLevelLength();
        queue = levelParser.getQueue();

    }


    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener(this));

        llama = new Llama(llamaX, 6, (float) 1.6, world, game.batch, assets);
        distance = llamaX;

        stageUi = new ButtonsStagePlay(game.getAssets());
        stageUi.setUpButtonAction();

        enemies = new ArrayList<>();
        timeLastEnemies = System.currentTimeMillis();

        bullets = new ArrayList<>();
        platforms = new ArrayList<>();
        grounds = new ArrayList<>();
        ammos = new ArrayList<>();
        coins = new ArrayList<>();
        obstacles = new ArrayList<>();
        Settings.playMusic(game.getAssets().GAME_MUSIC1);

    }

    @Override
    public void render(float delta)     {
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

        game.batch.draw(sky,
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
            platform.draw();
        for (Coin coin : coins)
            coin.draw();
        for (Ammo ammo : ammos)
            ammo.draw();
        for (Obstacle obstacle : obstacles)
            obstacle.draw();
        game.batch.end();

        stageUi.drawer();

        actions = stageUi.getActions();

        manageActions();

        stageUi.setActions(actions);
        debugRenderer.render(world, camera.combined);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

    }


    private void manageActions() {
        if (actions.get(SHOT)) {
            if (bulletsGun > 0) {
                bullets.add(new Bullet(llama.getX()+ llama.getW()/2+ 0.1f, llama.getY()-.1f, 0.1f, world, game.batch, game.getAssets()));
                Settings.playSound(SHOT);
                --bulletsGun;
                stageUi.setBullets(bulletsGun);
            }
            actions.put(SHOT, false);
        }

        if (actions.get(JUMP)){
            // sometimes getLinearVelocity().y near -3.442763E-10...
            if (llama.getBody().getLinearVelocity().y < 0.001 && llama.getBody().getLinearVelocity().y > -0.001) {
                llama.jump();
                actions.put(JUMP, false);
            }
        }

        if (actions.get(CROUCH)) {
            // crouches
            if (llama.isStanding())
                llama.crouch(true);

            llama.setStanding(false);
            actions.put(CROUCH, true);
        } else if (!actions.get(CROUCH)) {
            // stands up
            if (!llama.isStanding())
                llama.crouch(false);

            llama.setStanding(true);
            actions.put(CROUCH, false);
        }

        if (actions.get(PUNCH)) {
            if (llama.isStanding()) {
                llama.punch(true);
                timePunching=System.currentTimeMillis();
                actions.put(PUNCH, false);
            }
        }

        if (actions.get(PAUSE)) {
            pause();
        }

        if (actions.get(PLAY)) {
            resume();
        }

        if (actions.get(EXIT)) {
            //here there will be to develop the stuff for save the checkpoint;
            state = State.STOPPED;
            actions.put(EXIT, false);
            Settings.stopMusic(game.getAssets().GAME_MUSIC1);
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
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


    /**
     *
     * @param distance the distance reached by llama
     */
    private void loadLevel(double distance) {

        QueueObject queueObject = queue.peek();
        if (queueObject == null)
            return;

        if (queueObject.getX() - queueObject.getLength()/2 < distance+METER_WIDTH) {
            //System.out.println("llama.X: " + llama.getX() + " - Distance: " + distance + " - " + queueObject);
            queueObject = queue.poll();
            if (queueObject == null)
                return;

            float xCreation;
            float lCreation;
            if (queueObject.getX() < METER_WIDTH) {
                xCreation = (float) queueObject.getX();
                lCreation = (float) (queueObject.getLength() + METER_WIDTH- queueObject.getX());
            } else {
                xCreation = (float) (METER_WIDTH + queueObject.getLength() / 2);
                lCreation = (float) queueObject.getLength();
            }
            switch (queueObject.getClassObject()) {
                case ENEMIES: enemies.add(new Enemy(xCreation, 4, 1f, world, game.batch, assets)); break;
                case GROUND: grounds.add(new Ground(xCreation, 0, 0.6f, lCreation, velocity, world, game.batch, assets)); break;
                case PLATFORM1: platforms.add(new Platform(xCreation, 2.5f, 0.2f, lCreation, velocity, world, game.batch, assets)); break;
                case PLATFORM2: platforms.add(new Platform(xCreation, 4.4f, 0.2f, lCreation, velocity, world, game.batch, assets)); break;
                case AMMO: ammos.add(new Ammo(xCreation, 4.0f, 0.5f, queueObject.getNumItem(), world, game.batch, assets, stageUi)); break;
                case COINS: coins.add(new Coin(xCreation, 4.0f, 0.5f, queueObject.getNumItem(), world, game.batch, assets, stageUi)); break;
                case OBSTACLES:
                    float yCreation = 1.7F;
                    Random random = new Random();
                    if (random.nextFloat()<0.4){
                        yCreation = 4.2F;
                    }
                    obstacles.add(new Obstacle(xCreation, yCreation, 1f, world, game.batch, assets));
            }
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

        for(Coin coin : coins){
            coin.setPosition(coin.getBody().getPosition().x, coin.getBody().getPosition().y, coin.getBody().getAngle());

        }

        for(Ammo ammo : ammos){
            ammo.setPosition(ammo.getBody().getPosition().x, ammo.getBody().getPosition().y, ammo.getBody().getAngle());

        }

        for(Obstacle obstacle : obstacles){
            obstacle.setPosition(obstacle.getBody().getPosition().x, obstacle.getBody().getPosition().y, obstacle.getBody().getAngle());
        }

    }

    private void removeObjects() {
        if (isOutOfBonds(llama))
            gameOver();

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

        for (Iterator<Coin> c = coins.iterator(); c.hasNext(); ) {
            final Coin coin = c.next();
            if (coin.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(coin.getBody());
                    }
                });
                c.remove();
            }
        }

        for (Iterator<Ammo> a = ammos.iterator(); a.hasNext(); ) {
            final Ammo ammo = a.next();
            if (ammo.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(ammo.getBody());
                    }
                });
                a.remove();
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
        } else if (go instanceof Platform || go instanceof Ground) {
         //   return go.getBody().getPosition().x + go.getW() < -viewport.getWorldWidth();
        } else if (go instanceof Llama) {
            return go.getY() < 0;
        }

        return false;
    }

    public Llama getLlama() {
        return llama;
    }


    private boolean checkWin() {
        if (distance >= levelLength) {
            System.out.println("WIN!");

            ((Game) Gdx.app.getApplicationListener()).setScreen(new EndScreen(game, levelNumber, score, 0,true));
            return true;
        }
        return false;
    }


    public void gameOver() {
        game.setScreen(new EndScreen(game, levelNumber , 300, 100, true));
        Settings.stopMusic("gameMusic");
        dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        stageUi.resizer();
    }

    @Override
    public void pause() {

        state = State.PAUSE;

        actions.remove(PAUSE);
        actions.put(PAUSE, false);
    }

    @Override
    public void resume() {
        state = State.RUN;

        actions.remove(PLAY);
        actions.put(PLAY, false);
    }

    @Override
    public void hide() {
        pause();
    }

    @Override
    public void dispose() {
        stageUi.dispose();

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        stageUi.setMoney(money);
    }

    public int getBulletsGun() {
        return bulletsGun;
    }

    public void setBulletsGun(int bulletsGun) {
        this.bulletsGun = bulletsGun;
        stageUi.setBullets(bulletsGun);
    }
}