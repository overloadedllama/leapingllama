package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.LlamaNumericConstants;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.listener.MyContactListener;
import com.overloadedllama.leapingllama.game.*;
import com.overloadedllama.leapingllama.jsonUtil.LevelParser;
import com.overloadedllama.leapingllama.jsonUtil.QueueObject;
import com.overloadedllama.leapingllama.stages.GameStage;

import java.util.*;


public class GameScreen extends MyAbstractScreen implements LlamaNumericConstants {
    // Game Screen Constants
    static final int CHUNK_LENGTH = 50;
    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    public static float  UNITS_PER_METER = 128;

    public final float METER_WIDTH = GameApp.WIDTH / UNITS_PER_METER;
    public final float METER_HEIGHT = GameApp.HEIGHT / UNITS_PER_METER;

    // Game State
    public enum State
    {
        PAUSE,
        RUN,
        STOPPED,
        END
    }
    private State state = State.RUN;

    // Game utilities and basics
    PriorityQueue<QueueObject> queue;
    LevelParser levelParser;
    boolean levelLoaded = true;

    GameStage stageUi;
    HashMap<String, Boolean> actions;

    World world;
    Tube tube;
    Sky sky;
    Box2DDebugRenderer debugRenderer;

    // Game Objects
    private final GameObjectFactory goFactory;
    private final LabelObjectFactory loFactory;

    Llama llama;
    static ArrayList<Enemy> enemies;
    static ArrayList<Bullet> bullets;
    static ArrayList<Platform> platforms;
    static ArrayList<Ground> grounds;
    static ArrayList<Obstacle> obstacles;
    static ArrayList<EnemyDied> enemiesDead;
    static ArrayList<Coin> coins;
    static ArrayList<Ammo> ammos;

    // Game variables
    long timeLastEnemies;
    long timePunching;

    float accumulator = 0;
    float stateTime = 0;

    double distance;
    double levelLength;


    float llamaX = 3;
    float llamaH = 1.6f;

    int levelNumber;
    float difficulty = 0.05f;

    int money = 0;
    int ammunition = 0;

    // Score variables
    int coinsCollected = 0;
    int ammosCollected = 0;
    int enemiesKilled = 0;
    double totalLevelScore;

    //for ending camera movement
    double cameraMovement = 0;

    double llamaToEndScreenDistance = METER_WIDTH - llamaX;

    float initialGroundLength = 20;

    // METHODS

    public GameScreen(LlamaUtil llamaUtil, int levelNumber) {
        super(llamaUtil, GameApp.WIDTH / UNITS_PER_METER, GameApp.HEIGHT / UNITS_PER_METER );
        System.out.println("Init of GameScreen");
        this.levelNumber = levelNumber;

        goFactory = new GameObjectFactory(llamaUtil);
        loFactory = new LabelObjectFactory(llamaUtil, null);

        enemies = new ArrayList<>();
        timeLastEnemies = 0;

        debugRenderer = new Box2DDebugRenderer();
        camera.update();

        sky = new Sky(llamaUtil.getAssetManager().getTexture("sky"));

        actions = new HashMap<>();

        if (levelNumber != -1) {
            levelParser = new LevelParser(levelNumber);
            levelLength = levelParser.getLevelLength();
            queue = levelParser.getQueue();
            totalLevelScore = levelParser.getTotalLevelScore();
        } else {
            levelLength = CHUNK_LENGTH;
            levelParser = new LevelParser(difficulty, CHUNK_LENGTH);
            queue = levelParser.getQueue();

        }

        System.out.println("Init of GameScreen completed");
    }

    @Override
    public void show() {
        Box2D.init();

        world = new World(new Vector2(0f, -9.8f), true);
        world.setContactListener(new MyContactListener(this, llamaUtil));

        llamaUtil.getGameplayManager().setWorld(world);

        distance = 0;
        llama = (Llama) goFactory.createGameObject(LLAMA, llamaX, 1f, llamaH, 0);

        tube = new Tube(llamaX, 1f, llamaH);

        stageUi = new GameStage(llamaUtil);
        stageUi.setUpButtons();
        loFactory.setStage(stageUi);

        enemies = new ArrayList<>();
        timeLastEnemies = System.currentTimeMillis();

        bullets = new ArrayList<>();
        platforms = new ArrayList<>();
        grounds = new ArrayList<>();
        ammos = new ArrayList<>();
        coins = new ArrayList<>();
        obstacles = new ArrayList<>();
        enemiesDead = new ArrayList<>();

        if (llamaUtil.getGameplayManager().hasBonusAmmo()) {
            ammunition = 5;
            llamaUtil.getGameplayManager().setBonusAmmo();
            stageUi.setBullets(5);
        }
        loadLevel(distance);

        //final ground, for ending the game with a good ux
        if (levelNumber!=-1) {
            //grounds.add(new Ground((float) (levelLength + llamaToEndScreenDistance + initialGroundLength/2), 0, 0.6f, initialGroundLength, VELOCITY, world, llamaUtil.getGameApp().batch, llamaUtil.getAssetManager()));
            grounds.add((Ground) goFactory.createGameObject(GROUNDS, (float) (levelLength + llamaToEndScreenDistance + initialGroundLength/2),
                    0, 0.6f, initialGroundLength));
        }
        llamaUtil.getMusicManager().playMusic(LlamaConstants.GAME_MUSIC1);

        System.out.println("Show of GameScreen completed");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(Color.BLACK));

        switch(state) {
            case RUN: case END:
                stepWorld();

                if (timePunching != 0) {
                    if (System.currentTimeMillis() - timePunching > 300 && llama.isStanding()){
                        llama.punch(false);
                    }
                }

                updatePosition();
                removeObjects();
                llama.preserveX(llamaX);

                if(state == State.END){
                    if (cameraMovement > llamaToEndScreenDistance) {
                        callEndScreen(true);
                    }

                    cameraMovement += 0.05;
                    llamaX += 0.05;

                } else {

                    if (levelNumber!=-1) {
                        checkWin();
                        // need to do anything?
                    }
                }

                break;
        }

        llamaUtil.getGameApp().batch.begin();

        sky.draw(llamaUtil.getGameApp().batch, viewport.getScreenWidth()/UNITS_PER_METER + 2, METER_HEIGHT);

        for (Ground ground : grounds)
            ground.draw();
        for (Bullet bullet : bullets)
            bullet.draw();
        for (Enemy enemy : enemies)
            enemy.draw(stateTime);
        for (Platform platform : platforms)
            platform.draw();
        for (Coin coin : coins)
            coin.draw();
        for (Ammo ammo : ammos)
            ammo.draw();
        for (Obstacle obstacle : obstacles)
            obstacle.draw(stateTime);
        for (EnemyDied enemy: enemiesDead){
            enemy.draw(delta);
        }
        if (tube.isAnimationFinished()) {
            llama.draw(stateTime);
        }

        if (!tube.isAnimationFinished()) {
            tube.update();
            tube.draw(llamaUtil.getGameApp().batch);
        }

        llamaUtil.getGameApp().batch.end();

        stageUi.renderer();

        actions = stageUi.getActions();
        manageActions();
        stageUi.setActions(actions);

        if (levelNumber == -1){
            if (distance % CHUNK_LENGTH <= 0.1 && !levelLoaded) {
                levelLoaded = true;
                difficulty+=0.005;
                levelParser = new LevelParser(difficulty, CHUNK_LENGTH);
                queue.addAll(levelParser.getQueue());
                loadLevel(distance);
            } else if (distance > 0.1) {
                levelLoaded = false;
            }
        }

        // decomment next line for rendering the debug frames
        debugRenderer.render(world, camera.combined);

        super.render(delta);
    }

    private void manageActions() {
        if (actions.get(SHOT)) {
            if (ammunition > 0) {
                bullets.add((Bullet) goFactory.createGameObject(BULLET, llama.getX()+ llama.getW()/2+ 0.1f, llama.getY()-.1f, 0.1f, 0));
                llamaUtil.getSoundManager().playSound(SHOT);
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
            llamaUtil.getMusicManager().stopMusic(GAME_MUSIC1);
            dispose();
            llamaUtil.getGameApp().setScreen(new MainMenuScreen(llamaUtil));
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
        // sometimes getLinearVelocity().y near -3.442763E-10...
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


            if (tube.isAnimationFinished()) {
                world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

                distance += .05f;
                stageUi.setDistance(distance);
                stateTime += delta;
            }

            sky.update();

        }
    }

    Coin c;

    private void loadLevel(double distanceToCreate) {

        while (queue.size() > 0) {
            QueueObject queueObject = queue.peek();
            if (queueObject == null)
                return;

            queueObject = queue.poll();
            if (queueObject == null)
                return;

            float xCreation;
            float lCreation;

            if (levelNumber == -1 && distanceToCreate != 0)
                xCreation = (float) (METER_WIDTH + queueObject.getX() + queueObject.getLength() / 2);
            else
                xCreation = (float) (queueObject.getX() + queueObject.getLength() / 2);

            lCreation = (float) queueObject.getLength();

            switch (queueObject.getClassObject()) {
                case GROUNDS:
                    grounds.add((Ground) goFactory.createGameObject(GROUNDS, xCreation, 0, 0.6f, lCreation));
                    break;
                case PLATFORM1:
                    platforms.add((Platform) goFactory.createGameObject(PLATFORM, xCreation, 2.5f, 0.2f, lCreation));
                    break;
                case PLATFORM2:
                    platforms.add((Platform) goFactory.createGameObject(PLATFORM, xCreation, 4.4f, 0.2f, lCreation));
                    break;
                case ENEMIES:
                    enemies.add((Enemy) goFactory.createGameObject(ALIEN, xCreation, 4, 1f, 0));
                    break;
                case AMMO:
                    ammos.add((Ammo) loFactory.createLabelGameObject(AMMO, xCreation, 4.0f, 0.5f, queueObject.getNumItem()));
                    break;
                case COINS:
                    coins.add((Coin) loFactory.createLabelGameObject(COIN, xCreation, 4.0f, 0.5f, queueObject.getNumItem()));
                    break;
                case OBSTACLES:
                    float yCreation = 1.7F;
                    Random random = new Random();
                    if (random.nextFloat() < 0.4) {
                        yCreation = 4.2F;
                    }
                    //obstacles.add((Obstacle) goFactory.createGameObject(OBSTACLE, xCreation, yCreation, 1f, 0));
                    break;
            }
        }

        c = coins.get(0);
        System.out.println("Coin c created");
        System.out.println(c);
        System.out.println("c.body.x = " + c.getBody().getPosition().x +
                " --- c.body.y = " + c.getBody().getPosition().y);

    }



    /**
     * Update the position of all the game objects in the world. Check also
     * if any of them is out of bounds, and it that case mark it as destroyable
     */
    private void updatePosition() {
        llama.setPosition(llama.getBody().getPosition().x, llama.getBody().getPosition().y, llama.getBody().getAngle());
        if (isOutOfBonds(llama)) {
            System.out.println("LLAMA IS OUT OF BOUNDS");
            gameOver();
        }

        for (Enemy enemy : enemies) {
            enemy.setPosition(enemy.getBody().getPosition().x, enemy.getBody().getPosition().y, enemy.getBody().getAngle());
        }

        for (Bullet bullet : bullets) {
            bullet.setPosition(bullet.getBody().getPosition().x, bullet.getBody().getPosition().y, bullet.getBody().getAngle());
            if (isOutOfBonds(bullet)) {
                bullet.setDestroyable(true);
            }
        }

        for (Platform platform : platforms) {
            platform.setPosition(platform.getBody().getPosition().x, platform.getBody().getPosition().y, platform.getBody().getAngle());
            if (isOutOfBonds(platform)) {
                platform.setDestroyable(true);
            }
        }

        for(Ground ground : grounds) {
            ground.setPosition(ground.getBody().getPosition().x, ground.getBody().getPosition().y, ground.getBody().getAngle());
        }

        if (tube.isAnimationFinished()) {
            System.out.println("TUBE ANIMATION FINISHED");
        }

        for(Coin coin : coins) {
            // coin.setPosition(coin.getBody().getPosition().x, coin.getBody().getPosition().y, coin.getBody().getAngle());
        }
        System.out.println(c);
        System.out.println("c.body.x = " + c.getBody().getPosition().x +
                " --- c.body.y = " + c.getBody().getPosition().y);

        for(Ammo ammo : ammos) {
            ammo.setPosition(ammo.getBody().getPosition().x, ammo.getBody().getPosition().y, ammo.getBody().getAngle());
        }

        for(Obstacle obstacle : obstacles) {
            obstacle.setPosition(obstacle.getBody().getPosition().x, obstacle.getBody().getPosition().y, obstacle.getBody().getAngle());
        }

        for (EnemyDied enemy : enemiesDead) {
            enemy.setPosition(enemy.getX()- VELOCITY * STEP_TIME, enemy.getY());
        }
    }

    /**
     * Removes all the GameObjects that are destroyable. First checks that llama
     * is not out of bounds (if is not felt out of ground mainly), next all the others.
     */
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
                llamaUtil.getSoundManager().playSound(ALIEN_GROWL);
                if (!llamaUtil.getGameplayManager().hasBonusLife()) {
                    enemiesDead.add((EnemyDied) goFactory.createGameObject(enemy.getName() + "Dead", enemy.getX(), enemy.getY(), enemy.getH(), 0));
                }
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

        for (Iterator<Ground> g = grounds.iterator(); g.hasNext(); ) {
            final Ground ground = g.next();
            if (ground.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(ground.getBody());
                    }
                });
                g.remove();
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
                System.out.println("coin removed");
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
                System.out.println("ammo removed");
                a.remove();
            }
        }

        for (Iterator<Obstacle> o = obstacles.iterator(); o.hasNext(); ) {
            final Obstacle obstacle = o.next();
            if (obstacle.isDestroyable()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        world.destroyBody(obstacle.getBody());
                    }
                });
                o.remove();
            }
        }

    }

    /**
     * for Enemy, Bullets, Ammos and Coins checks if they are also felt out of ground
     * @param go the object checked
     * @param <T> accepts all objects which extend GameObject class
     * @return true if the Object is out of bounds, else false
     */
    public <T extends AbstractGameObject> boolean isOutOfBonds(T go) {

        if (go instanceof Enemy || go instanceof Bullet || go instanceof Ammo || go instanceof Coin) {
            return go.getBody().getPosition().y + go.getW()/2 < 0;
        } else if (go instanceof Platform || go instanceof Ground) {
            return go.getBody().getPosition().x + go.getW()/2 < 0;
        } else if (go instanceof Llama) {
            return go.getY() < 0;
        }

        return false;
    }

    /**
     * check if the llama is arrived to 7 meter far from the end. 7 meter, in this way there will be ground on the entire screen for the entire level.
     * (the screen is 10 meter long and the llama is at 3 meters from the screen x origin)
     */
    private void checkWin() {
        if (distance >= levelLength + llamaToEndScreenDistance + 10) {
            state = State.END;
            System.out.println("WIN");
            System.out.printf(Locale.ENGLISH,
                    "distance %f >= levelLength %f + llamaToEndScreenDistance %f + 10",
                    distance, levelLength, llamaToEndScreenDistance);
            callEndScreen(true);
        }
    }

    /**
     * if the player has bought the second life when he dies (enemy/obstacle collision, falling out of ground...)
     * all the enemies are destroyed
     */
    public void gameOver() {
        if (!llamaUtil.getGameplayManager().hasBonusLife()) {
            System.out.println("GAME OVER");
            callEndScreen(false);
        } else {
            for (Enemy enemy : enemies) {
                enemy.setDestroyable(true);
            }
            for (Obstacle obstacle : obstacles) {
                obstacle.setDestroyable(true);
            }
            removeObjects();

            llamaUtil.getGameplayManager().setBonusLife();
        }
    }

    /**
     * call the End Screen, stop the game music and update user money
     * @param win if true llama has reached the level's end, false otherwise
     */
    private void callEndScreen(boolean win) {
        llamaUtil.getMusicManager().stopMusic(GAME_MUSIC1);
        llamaUtil.getLlamaDbHandler().checkSetUserMoney(llamaUtil.getCurrentUser(), money);
        llamaUtil.getGameApp().setScreen(new EndScreen(llamaUtil, levelNumber, calculatePlayerLevelScore(), totalLevelScore, win));
        dispose();
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

    public Llama getLlama() {
        return llama;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
        stageUi.setMoney(money);
        llamaUtil.getSoundManager().playSound(CASH);
    }

    public int getAmmunition() {
        return ammunition;
    }
    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
        stageUi.setBullets(ammunition);
        llamaUtil.getSoundManager().playSound(CASH);
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