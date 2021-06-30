package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.overloadedllama.leapingllama.GameApp;

public class Ground extends GameObject{

    public Ground(float x, float y, float h, World world, Batch batch) {
        super(new Texture(Gdx.files.internal("ground.png")), x, y, h, world, batch);






    }

    public void setMyW(float meter_width) {
        w = meter_width;

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion imgTextureRegion = new TextureRegion(texture);
        imgTextureRegion.setRegion(0,0, w, h);


        sprite = new Sprite(imgTextureRegion);
        sprite.setSize(w, h);



        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(w, h); //dunno why h instead of h/2, but it works ;)

        super.createBody(BodyDef.BodyType.StaticBody, groundShape, 20, 0.5f, 0);

        groundShape.dispose();

    }

    @Override
    public void draw(){
        batch.draw(texture,
                // position and size of texture
                -1, 0, w +2, h,
                // srcX, srcY, srcWidth, srcHeight
                (int) x, (int)y, texture.getWidth(), texture.getHeight(),
                // flipX, flipY
                true, false);
    }
}
