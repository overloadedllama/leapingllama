package com.overloadedllama.leapingllama.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.game.GameObject;
import com.overloadedllama.leapingllama.game.Ground;
import com.overloadedllama.leapingllama.game.Llama;


//this is the screen of the gameplay, i start to set up the environment.


public class GameScreen  implements Screen{

    OrthographicCamera camera;
    Viewport viewport;
    GameApp game;

    Stage stage;

    World world;

    TextButton buttonJump;
    Skin buttonJumpSkin;


    Llama llama;
    Ground ground;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;


    static float  UNITS_PER_METER = 128;
    float WORLD_WIDTH = UNITS_PER_METER * 6;
    float WORLD_HEIGHT = UNITS_PER_METER * 3;


    float METER_WIDTH = WORLD_WIDTH / UNITS_PER_METER;
    float METER_HEIGHT = WORLD_HEIGHT / UNITS_PER_METER;

    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        camera.update();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();



        debugRenderer = new Box2DDebugRenderer();
    }


    public static float unitsToMeters(float units) {
        return units / UNITS_PER_METER;
    }

    public static float metersToUnits(float meters) {
        return meters * UNITS_PER_METER;
    }




    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, metersToUnits(-9.8f)), true);

        stage = new Stage(viewport);

        buttonJumpSkin = new Skin(Gdx.files.internal("text_button/text_button.json"), new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        buttonJump = new TextButton("jump", buttonJumpSkin);
        buttonJump.setBounds(300, 100, 20, 20);
        buttonJump.getLabel().setFontScale(0.2f);
        stage.addActor(buttonJump);




        llama = new Llama(new Texture(Gdx.files.internal("llamaphoto.png")), metersToUnits(1f), metersToUnits(2f), metersToUnits(0.5f), metersToUnits(2f), world, game.batch);
        ground = new Ground(new Texture(Gdx.files.internal("wall.jpg")), 0, 0, camera.viewportWidth*2, metersToUnits(0.3f), world, game.batch);

        Gdx.input.setInputProcessor(stage);




    }



    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);
        stepWorld();

        llama.setPosition((llama.getBody().getPosition().x), (llama.getBody().getPosition().y));

        camera.update();

        stage.act();
        stage.draw();


        game.batch.begin();
        llama.draw();
        ground.draw();
        game.batch.end();




        debugRenderer.render(world, camera.combined);

        buttonJump.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Jump button pressed");
                llama.jump();
            }
        });


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
        game.batch.setProjectionMatrix(camera.combined);

        buttonJump.invalidateHierarchy();
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