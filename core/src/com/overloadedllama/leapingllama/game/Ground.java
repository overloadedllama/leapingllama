package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.assetman.Assets;


public class Ground extends GameObject{

    TextureRegion textureRegion;

    public Ground(float x, float y, float h,  float length, float v, World world, Batch batch, Assets assets) {
        super(assets.getTexture("ground"), x, y, h, world, batch);

        w=length;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);


        textureRegion = new TextureRegion(texture, 0,0, w , h*2 );

        sprite = new Sprite(textureRegion);
        sprite.setSize(w, h);


        PolygonShape shape = new PolygonShape();

        shape.setAsBox(w / 2, h / 2);

        createBody(BodyDef.BodyType.KinematicBody, shape, 1, 0.01f, 0);

        fixtureDef.filter.categoryBits = CATEGORY_GROUND;
        fixtureDef.filter.maskBits = MASK_GROUND;

        shape.dispose();

        body.setLinearVelocity(-v, 0f);

    }

    @Override
    public void draw(){
       /* batch.draw(texture,
                // position and size of texture
                -1, 0, w +2, h,
                // srcX, srcY, srcWidth, srcHeight
                (int) x, (int)y, texture.getWidth(), texture.getHeight(),
                // flipX, flipY
                true, false);*/
        batch.draw(textureRegion, x-w/2, y-h/2, w, h);

    }
}
