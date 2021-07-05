package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;

public class Platform extends GameObject{
    float v = 1;
    float lenght;
    float w0;

    int tileCount;
    TextureRegion textureRegion;

    public Platform(float x, float y, float h, float lenght, World world, Batch batch, Assets assets) {
        super(assets.getTexture("platform"), x, y, h, world, batch);

        w0 = w;
        w = lenght;
        tileCount = (int) (w0/w);


        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);


        textureRegion = new TextureRegion(texture, 0,0, (int)w0/120, (int)h/120);



        sprite = new Sprite(textureRegion);
        sprite.setSize(w, h);


        PolygonShape shape = new PolygonShape();

        shape.setAsBox(w / 2, h / 2);

        createBody(BodyDef.BodyType.KinematicBody, shape, 1, 0.01f, 0);

        shape.dispose();

        body.setLinearVelocity(-v, 0f);
    }

    @Override
    public void draw(){

        batch.draw(textureRegion, x-w/2, y-h/2, w, h);
    }

}
