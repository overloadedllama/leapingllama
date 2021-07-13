package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.Settings;
import com.overloadedllama.leapingllama.listener.MyContactListener;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.jsonUtil.LevelParser;
import com.overloadedllama.leapingllama.jsonUtil.QueueObject;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;
import org.graalvm.compiler.phases.common.NodeCounterPhase;

import java.awt.*;
import java.util.*;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;


public class GameScreen extends MyAbstractScreen {

    public enum State
    {
        PAUSE,
        RUN,
        STOPPED
    }

    PriorityQueue<QueueObject> queue;
    LevelParser levelParser;
    private State state = State.RUN;

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
    static ArrayList<EnemyDied> enemiesDead;
    static  ArrayList<Coin> coins;
    static ArrayList<Ammo> ammos;

    Box2DDebugRenderer debugRenderer;
    static final double CHUNK_LENGTH = 50;
    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    public static float  UNITS_PER_METER = 128;

    public final float METER_WIDTH = GameApp.WIDTH / UNITS_PER_METER;
    public final float METER_HEIGHT = GameApp.HEIGHT / UNITS_PER_METER;

    long timeLastEnemies;
    long timePunching;

    float accumulator = 0;
    float stateTime = 0;

    double distance;
    double levelLength;

    float llamaX = 3;
    float llamaH = 1.6f;
    float velocity = 2;

    int levelNumber;

    int money = 0;
    int ammunition = 0;

    int coinsCollected = 0;
    int ammosCollected = 0;
    int enemiesKilled = 0;
    double totalLevelScore;

    Tube tube;


    // METHODS

    public GameScreen(final GameApp gameApp, int levelNumber) {
        super(gameApp, GameApp.WIDTH / UNITS_PER_METER, GameApp.HEIGHT / UNITS_PER_METER);
        this.levelNumber = levelNumber;

        enemies = new ArrayList<>();
        timeLastEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

        sky = gameApp.getAssets().getTexture("sky");
        sky.setWrap(Repeat,Repeat);

        actions = new HashMap<>();


        if (levelNumber != -1) {
            levelParser = new LevelParser(levelNumber);
            levelLength = levelParser.getLevelLength();
            queue = levelParser.getQueue();
            totalLevelScore = levelParser.getTotalLevelScore();
        } else {
            levelLength = CHUNK_LENGTH;
            levelParser = new LevelParser();
            queue = levelParser.getQueue();


        }
    }

    @Override
    public void show() {
        Box2D.init();




        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener(this));

        distance = llamaX;


        llama = new Llama(llamaX, 1f, llamaH, world, gameApp.batch, assets);

        stageUi = new ButtonsStagePlay(gameApp);

        stageUi.setUpButtons();

        enemies = new ArrayList<>();
        timeLastEnemies = System.currentTimeMillis();



        tube = new Tube(llamaX, 1f, llamaH);

        bullets = new ArrayList<>();
        platforms = new ArrayList<>();
        grounds = new ArrayList<>();
        ammos = new ArrayList<>();
        coins = new ArrayList<>();
        obstacles = new ArrayList<>();
        enemiesDead = new ArrayList<>();
        Settings.playMusic(gameApp.getAssets().GAME_MUSIC1);

        if (Settings.hasInitialAmmos()) {
            ammunition = 5;
            Settings.setInitialAmmos();
        }

    }


    @Override
    public void render(float delta)     {
        ScreenUtils.clear(new Color(Color.BLACK));
        switch(state) {

            case RUN:


                    stepWorld();



                if (levelNumber!=-1) {
                    checkWin();

                    // need to do anything?
                }

                if (timePunching != 0) {
                    if (System.currentTimeMillis() - timePunching > 300 && llama.isStanding()){
                        llama.punch(false);
                    }
                }

                updatePosition();
                removeObjects();
                loadLevel(distance%levelLength);
                llama.preserveX(llamaX);



                break;


        }

        gameApp.batch.begin();

        gameApp.batch.draw(sky,
                // position and size of texture
                -1, 0, viewport.getScreenWidth()/UNITS_PER_METER + 2, METER_HEIGHT,
                // srcX, srcY, srcWidth, srcHeight
                (int) xSky, 0, sky.getWidth(), sky.getHeight(),
                // flipX, flipY
                false, false);

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
        for (EnemyDied enemy: enemiesDead){
            enemy.draw(delta);
        }
        if(tube.isAnimationFinished()){
            llama.draw(stateTime);
        }


        if(!tube.isAnimationFinished()){
            tube.update();
            tube.draw(gameApp.batch);
        }



        gameApp.batch.end();

        stageUi.renderer();

        actions = stageUi.getActions();

        manageActions();

        stageUi.setActions(actions);
        debugRenderer.render(world, camera.combined);

        super.render(delta);
    }

    private void manageActions() {
        if (actions.get(SHOT)) {
            if (ammunition > 0) {
                bullets.add(new Bullet(llama.getX()+ llama.getW()/2+ 0.1f, llama.getY()-.1f, 0.1f, world, gameApp.batch, gameApp.getAssets()));
                Settings.playSound(SHOT);
                --ammunition;
                stageUi.setBullets(ammunition);
            }
            actions.put(SHOT, false);
        }

        if (actions.get(JUMP)){
            jumps();
        }

        if (actions.get(CROUCH)) {
            // crouches
            crouches(true);

        } else if (!actions.get(CROUCH)) {
            // stands up
            crouches(false);

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
            Settings.stopMusic(gameApp.getAssets().GAME_MUSIC1);
            dispose();
            gameApp.setScreen(new MainMenuScreen(gameApp));
        }
    }

    private void crouches(boolean crouches) {
        if(crouches){
            if (llama.isStanding())
                llama.crouch(true);

            llama.setStanding(false);
            actions.put(CROUCH, true);
        }else{
            if (!llama.isStanding())
                llama.crouch(false);

            llama.setStanding(true);
            actions.put(CROUCH, false);
        }
    }

    private void jumps() {
        //todo sometimes getLinearVelocity().y near -3.442763E-10...
        if (llama.getBody().getLinearVelocity().y < 0.001 && llama.getBody().getLinearVelocity().y > -0.001) {
            llama.jump();
            actions.put(JUMP, false);
        }
    }

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;


            if(tube.isAnimationFinished()) {
                world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

                distance += .05f;
                stageUi.setDistance(distance);
                stateTime += delta;
            }

            xSky += 1;
        }
    }


    /**
     *
     * @param distance the distance reached by llama
     */
    private void loadLevel(double distance) {

        if (distance == 0) {
            levelParser = new LevelParser();
            queue.addAll(levelParser.getQueue());
        }

        QueueObject queueObject = queue.peek();
        if (queueObject == null)
            return;

        if (queueObject.getX() - queueObject.getLength()/2 < distance+METER_WIDTH) {
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
                case GROUND: grounds.add(new Ground(xCreation, 0, 0.6f, lCreation, velocity, world, gameApp.batch, assets)); break;
                case PLATFORM1: platforms.add(new Platform(xCreation, 2.5f, 0.2f, lCreation, velocity, world, gameApp.batch, assets)); break;
                case PLATFORM2: platforms.add(new Platform(xCreation, 4.4f, 0.2f, lCreation, velocity, world, gameApp.batch, assets)); break;
                case ENEMIES: enemies.add(new Enemy(xCreation, 4, 1f, world, gameApp.batch, assets)); break;
                case AMMO: ammos.add(new Ammo(xCreation, 4.0f, 0.5f, queueObject.getNumItem(), world, gameApp.batch, assets, stageUi)); break;
                case COINS: coins.add(new Coin(xCreation, 4.0f, 0.5f, queueObject.getNumItem(), world, gameApp.batch, assets, stageUi)); break;
                case OBSTACLES:
                    float yCreation = 1.7F;
                    Random random = new Random();
                    if (random.nextFloat()<0.4){
                        yCreation = 4.2F;
                    }
                    obstacles.add(new Obstacle(xCreation, yCreation, 1f, velocity*2, world, gameApp.batch, assets));
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

        for (EnemyDied enemy : enemiesDead) {
            enemy.setPosition(enemy.getX()-velocity*STEP_TIME, enemy.getY());
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
                enemiesDead.add(new EnemyDied(String.valueOf(enemy.getTexture()), enemy.getX(), enemy.getY(), enemy.getH(), gameApp.batch, assets));
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
     * for Enemy, Bullets, Ammos and Coins checks if they are also felt out of ground
     * @param go the object checked
     * @param <T> accepts all objects which extend GameObject class
     * @return true if the Object is out of bounds, else false
     */
    public <T extends GameObject> boolean isOutOfBonds(T go) {

        if (go instanceof Enemy || go instanceof Bullet || go instanceof Ammo || go instanceof Coin) {
            return go.getBody().getPosition().x < -viewport.getWorldWidth() ||
                    go.getBody().getPosition().x > viewport.getWorldWidth() * 2 ||
                    go.getBody().getPosition().y < 0;
        } else if (go instanceof Platform || go instanceof Ground) {
            //return go.getBody().getPosition().x + go.getW() < -viewport.getWorldWidth();
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
            Settings.stopMusic(GAME_MUSIC1);
            ((Game) Gdx.app.getApplicationListener()).setScreen(new EndScreen(gameApp, levelNumber, calculatePlayerLevelScore(), totalLevelScore,true));
            return true;
        }
        return false;
    }

    /**
     * if the player has bought the second life when he dies (enemy/obstacle collision, falling out of ground...)
     * all the enemies are destroyed and the llama restarts at y = 6 (does it work fine?)
     */
    public void gameOver() {
        if (!Settings.hasSecondLife()) {
            gameApp.setScreen(new EndScreen(gameApp, levelNumber, calculatePlayerLevelScore(), totalLevelScore, true));
            Settings.stopMusic(GAME_MUSIC1);
            dispose();
        } else {
            for (Enemy enemy : enemies) {
                enemy.setDestroyable(true);
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.setDestroyable(true);
            }

            llama.setY(6);

            Settings.setSecondLife();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        Settings.playSound(CASH);
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
        stageUi.setBullets(ammunition);
        Settings.playSound(LOAD);
    }

    public void updateEnemiesKilled() {
        enemiesKilled++;
    }

    public void updateCoinsTaken() {
        coinsCollected++;
    }

    public void updateAmmosTaken() {
        ammosCollected++;
    }

    private double calculatePlayerLevelScore() {
        return  distance +
                coinsCollected * 30 +
                enemiesKilled * 20 +
                ammosCollected * 10;
    }

}