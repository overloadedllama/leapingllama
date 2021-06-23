package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Llama extends GameObject {

    public Llama(Texture texture) {
        super(texture);
        sprite = new Sprite(texture);
    }

}