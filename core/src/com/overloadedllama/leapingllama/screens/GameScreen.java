package com.overloadedllama.leapingllama.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    Table buttonJumpTable;



    Llama llama;
    Body llamaBody;
    Texture llamaTexture;


    GameObject ground;
    Body groundBody;
    Texture groundTexture;

    Box2DDebugRenderer debugRenderer;

    static final float STEP_TIME = 1.0f / 60.0f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;

    float WORLD_WIDTH = 320;
    float WORLD_HEIGHT = 180;

    static float  UNITS_PER_METER = 128;
    float METRE_WIDTH = WORLD_WIDTH / UNITS_PER_METER;
    float METRE_HEIGHT = WORLD_HEIGHT / UNITS_PER_METER;

    public GameScreen(final GameApp game) {
        this.game = game;

        camera = new OrthographicCamera();

        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        //camera.position.set(GameApp.WIDTH / 2, GameApp.HEIGHT  / 2, 0);

        //camera.update();


        debugRenderer = new Box2DDebugRenderer();
    }


    public static float unitsToMeters(float units) {
        return units / UNITS_PER_METER;
    }

    public static float metersToUnits(float metres) {
        return metres * UNITS_PER_METER;
    }




    @Override
    public void show() {
        Box2D.init();
        world = new World(new Vector2(0f, -9.8f), true);

        stage = new Stage(viewport);

        buttonJumpSkin = new Skin(Gdx.files.internal("text_button/text_button.json"), new TextureAtlas(Gdx.files.internal("text_button/text_button.atlas")));
        buttonJump = new TextButton("jump!", buttonJumpSkin);
        buttonJumpTable = new Table();
        buttonJumpTable.bottom().add(buttonJump).size(metersToUnits(1f), metersToUnits(1f)).padBottom(metersToUnits(3f)).padLeft(metersToUnits(3f));

        stage.addActor(buttonJumpTable);



        llamaTexture = new Texture(Gdx.files.internal("llamaphoto.png"));
        llama = new Llama(llamaTexture,  metersToUnits(0.5f),metersToUnits(1f),metersToUnits(0.2f),metersToUnits(0.4f));
        stage.addActor(llama);

        Gdx.input.setInputProcessor(stage);
        buttonJump.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Jump button pressed");
                llama.jump(30);
            }
        });

        groundTexture = new Texture(Gdx.files.internal("wall.jpg"));
        ground = new GameObject(groundTexture, 0, 0, viewport.getScreenWidth(), metersToUnits(0.3f));
        //ground.setPosition(0, 0);
        //ground.setBounds(0,0, metersToUnits(1f), metersToUnits(0.3f));
        stage.addActor(ground);


        // Now create a BodyDefinition. This defines the physics objects type and position in the simulation
        BodyDef llamaBodyDef = new BodyDef();
        BodyDef groundBodyDef = new BodyDef();


        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        groundBodyDef.type = BodyDef.BodyType.StaticBody;



        llamaBodyDef.position.set(llama.getX(), llama.getY());
        groundBodyDef.position.set(ground.getX(), ground.getY());

        // Create a body in the world using our definition
        llamaBody = world.createBody(llamaBodyDef);
        groundBody = world.createBody(groundBodyDef);


        // Now define the dimensions of the physics shape
        PolygonShape llamaShape = new PolygonShape();
        PolygonShape groundShape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        llamaShape.setAsBox(llama.getWidth()/2, llama.getHeight()/2);
        groundShape.setAsBox(ground.getWidth()/2, ground.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others  we will see shortly
        // If you are wondering, density and area are used to calculate over all ass
        FixtureDef llamaFixtureDef = new FixtureDef();
        llamaFixtureDef.shape = llamaShape;
        llamaFixtureDef.density = 985;
        llamaFixtureDef.friction = 1f;
        llamaFixtureDef.restitution = 0f;
        Fixture fixture = llamaBody.createFixture(llamaFixtureDef);


        groundBody.createFixture(groundShape, 0f);
        // Shape is the only disposable of the lot, so get rid of it
        llamaShape.dispose();
        groundShape.dispose();

        System.out.println("Screen width: " + viewport.getScreenWidth());
        System.out.println("Screen height: " + viewport.getScreenHeight());
        System.out.println("Llama dimensions: " + unitsToMeters( llama.getWidth()) + " " + unitsToMeters( llama.getHeight()));


    }



    @Override
    public void render(float delta) {
       // super.render();
        ScreenUtils.clear(0.1f, 0, 0.2f, 1);

        System.out.println(unitsToMeters(llamaBody.getPosition().x) + " " + unitsToMeters(llamaBody.getPosition().y) + " " + llama.getY());

        stepWorld();

        llama.setPosition( unitsToMeters(llamaBody.getPosition().x), unitsToMeters(llamaBody.getPosition().y));




        //camera.update();
        stage.act();
        stage.draw();





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

        buttonJumpTable.invalidateHierarchy();
        buttonJumpTable.setSize(10, 10);
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