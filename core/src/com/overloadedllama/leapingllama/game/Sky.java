package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.assetman.Assets;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class Sky {

    Texture sky;
    float xSky = 0;
    public Sky(Texture sky) {
        this.sky = sky;


        sky.setWrap(Repeat,Repeat);
    }




    public void draw (Batch batch, float w, float h)
    {
        batch.draw(sky,
                // position and size of texture
                -1, 0, w, h,
                // srcX, srcY, srcWidth, srcHeight
                (int) xSky, 0, sky.getWidth(), sky.getHeight(),
                // flipX, flipY
                false, false);
    }


    public void update(){
        xSky++;
    }
}
