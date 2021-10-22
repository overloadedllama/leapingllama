package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class Platform extends AbstractGameObject {
    TextureRegion textureRegion;

    public Platform(float x, float y, float h, float length, float velocity, LlamaUtil llamaUtil) {
        super(PLATFORM, x, y, h, llamaUtil);

        w = length;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        textureRegion = new TextureRegion(texture, 0,0, w , h*2 );

        sprite = new Sprite(textureRegion);
        sprite.setSize(w, h);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(w / 2, h / 2);

        createBody(BodyDef.BodyType.KinematicBody, shape, 1, 0.01f, 0);

        shape.dispose();

        body.setLinearVelocity(-velocity, 0f);
    }

    @Override
    public void draw(){

        batch.draw(textureRegion, x-w/2, y-h/2, w, h);
    }

}
