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

public class Enemy extends GameObject{

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;


    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;

    String textureString = "alienCyan";

    public Enemy(float x, float y, float h, World world, Batch batch, Assets assets){
        super(assets.getTexture("alienCyan"), x, y, h, world, batch);

        Random random = new Random();
        int randomNumber = random.nextInt(2);

        if(randomNumber == 1){
            textureString = "alienYellow";
            texture = assets.getTexture(textureString);
            sprite.setTexture(texture);
        }

        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, enemyShape, 20, 1f, 0f);

        enemyShape.dispose();

       // body.setLinearVelocity(-3f, 0);
        body.setFixedRotation(true);

        walkSheet = assets.getTexture(textureString + "Walking");
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


    public String getTextureString() {
        return textureString;
    }
}