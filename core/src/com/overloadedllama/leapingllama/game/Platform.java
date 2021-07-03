package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

public class Platform extends GameObject{
    public Platform(float x, float y, float h, World world, Batch batch) {
        super(new Texture(Gdx.files.internal("platform.png")), x, y, h, world, batch);
    }
}
