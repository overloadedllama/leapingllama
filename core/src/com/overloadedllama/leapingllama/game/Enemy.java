package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

import java.util.Random;

public class Enemy extends AbstractGameObject {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;
    private int numLife = 1;

    Animation<TextureRegion> walkAnimation;
    Texture walkSheet;

    String textureString = ALIEN_CYAN;

    public Enemy(float x, float y, float h, LlamaUtil llamaUtil){
        super(ALIEN_CYAN, x, y, h, llamaUtil);

        Random random = new Random();
        int randomNumber = random.nextInt(2);

        if (randomNumber == 1) {
            textureString = ALIEN_YELLOW;
            texture = llamaUtil.getAssetManager().getTexture(textureString);
            sprite.setTexture(texture);
            numLife = 2;
        }

        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, enemyShape, 800, 1f, 0f);

        enemyShape.dispose();

        body.setFixedRotation(true);

        setAnimation();

    }

    private void setAnimation() {
        walkSheet = llamaUtil.getAssetManager().getTexture(textureString + "Walking");
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


    public void draw(float stateTime) {
            TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            sprite = new Sprite(currentFrame);

            sprite.setSize(w, h);
            sprite.setPosition(x - w / 2, y - h / 2);
            sprite.draw(batch);
    }


    public String getTextureString() {
        return textureString;
    }

    public void decreaseNumLife() {
        --numLife;
        if (numLife == 0) {
            destroyable = true;
        } else {
            body.applyForceToCenter(-100000000, 0, true);
            body.setLinearVelocity(0, 0f);

            textureString = ALIEN_CYAN;
            setAnimation();
        }
    }
}