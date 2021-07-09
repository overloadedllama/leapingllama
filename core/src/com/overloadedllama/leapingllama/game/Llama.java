package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.resources.BodyEditorLoader;

public class Llama extends GameObject {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;

    private BodyEditorLoader loader;

    boolean isStanding;
    boolean isJumping = false;
    boolean isPunching = false;
    boolean isCrouching = false;

    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;

    public Llama(float x, float y, float h, World world, Batch batch, Assets assets) {
        super(assets.getTexture("llamaStanding"), x, y, h, world, batch);

        this.assets = assets;

        PolygonShape llamaShape = new PolygonShape();
        llamaShape.setAsBox(w / 2, h / 2);
        super.createBody(BodyDef.BodyType.DynamicBody, llamaShape, 1500, 0.05f, 0);
        llamaShape.dispose();

        /*
        BodyDef llamaBodyDef = new BodyDef();
        llamaBodyDef.type = BodyDef.BodyType.DynamicBody;
        llamaBodyDef.position.set(x, y);
        body = world.createBody(llamaBodyDef);
        loader = new BodyEditorLoader(Gdx.files.internal("llama/llamaStandingBody.json"));
        fixtureDef = new FixtureDef();
        fixtureDef.density = 1500f;
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = CATEGORY_LLAMA;
        fixtureDef.filter.maskBits = MASK_LLAMA;
        loader.attachFixture(body, "llamaStandingBody", fixtureDef, 1);
         */

        isStanding = true;

        // initializing Llama Animation
        walkSheet = assets.getTexture("llamaWalking");
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<>(0.16f, walkFrames);

    }

    public void jump() {
        isJumping = true;
        body.applyForceToCenter(0, 1000000, true);
    }

    public void crouch(Boolean isCrouching) {
        PolygonShape shape = new PolygonShape();
        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;
        this.isCrouching = isCrouching;
        if (isCrouching) {
            // then crouches
            h /= 2;

            newFixtureDef.density = fixtureDef.density * 2;

            this.body.setTransform(x, y - h / 2, 0);

            sprite.set(new Sprite(assets.getTexture("llamaCrouching")));

        } else {
            // stands up
            h *= 2;

            this.body.setTransform(x, y + h / 4, 0);

            sprite.set(new Sprite(assets.getTexture("llamaStanding")));
        }

        sprite.setSize(w, h);
        sprite.setPosition(body.getPosition().x, body.getPosition().y); // i'm not sure it works...

        this.body.destroyFixture(this.getBody().getFixtureList().first());

        shape.setAsBox(w / 2, h / 2);
        newFixtureDef.shape = shape;

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

    }

    public void punch(Boolean punch) {
        PolygonShape shape = new PolygonShape();
        FixtureDef newFixtureDef = new FixtureDef();
        newFixtureDef.density = fixtureDef.density;
        newFixtureDef.friction = fixtureDef.friction;
        newFixtureDef.restitution = fixtureDef.restitution;

        if (punch) {
            shape.setAsBox(w / 2 + 0.1f, h / 2);
            sprite.set(new Sprite(assets.getTexture("llamaPunching")));
            isPunching = true;
        } else {
            shape.setAsBox(w / 2, h / 2);
            sprite.set(new Sprite(assets.getTexture("llamaStanding")));
            isPunching = false;
        }

        sprite.setSize(w, h);

        this.body.destroyFixture(this.getBody().getFixtureList().first());
        newFixtureDef.shape = shape;

        body.createFixture(newFixtureDef).setUserData(this);

        shape.dispose();

        sprite.setPosition(x, y);
    }

public boolean isStanding() { return isStanding; }
    public void setStanding(boolean value) { isStanding = value; }

    public boolean isPunching() { return isPunching; }
    public void setPunching(boolean isPunching) { this.isPunching = isPunching; }

    public void preserveX(float llamaX) {
        body.setTransform(llamaX, body.getPosition().y, 0);
    }

    public void draw(float stateTime){


       if (isPunching || isCrouching ){

           draw();
       } else {


           TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
           sprite = new Sprite(currentFrame);

           //sprite = new Sprite(new Texture(Gdx.files.internal("llama/llamaStanding.png")));

           sprite.setSize(w, h);
           sprite.setPosition(x - w / 2, y - h / 2);

           sprite.draw(batch);
       }
    }

}
