package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Llama extends GameObject {

    public Llama(Texture texture, float x, float  y, float w, float h) {
        super(texture, x, y, w, h);
    }

    public void jump(float hJump) {
        super.setPosition(getX(), getY() + hJump);
    }


}