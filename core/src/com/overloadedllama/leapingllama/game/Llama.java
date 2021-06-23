package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Llama extends GameObject {

    public Llama(Texture texture, int x, int y, int w, int h){

        super(texture);

        sprite.setBounds(x, y, w, h);
    }

}