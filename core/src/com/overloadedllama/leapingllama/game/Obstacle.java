package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;

import java.util.Random;

public class Obstacle extends GameObject{

    //alienOrangeFlyingMoving
    private static final int FRAME_COLS = 2, FRAME_ROWS = 1;


    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;


    public Obstacle(float x, float y, float h, float velocity, World world, Batch batch, Assets assets){
        super(assets.getTexture("alienOrangeFlying"), x, y, h, world, batch);


        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.KinematicBody, enemyShape, 20, 1f, 0f);

        enemyShape.dispose();

        body.setLinearVelocity(-velocity, 0f);

        walkSheet = assets.getTexture("alienOrangeFlyingMoving");
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


    public void draw(float stateTime){


        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        sprite = new Sprite(currentFrame);

        //sprite = new Sprite(new Texture(Gdx.files.internal("llama/llamaStanding.png")));

        sprite.setSize(w, h);
        sprite.setPosition(x - w / 2, y - h / 2);

        sprite.draw(batch);

    }


}
