package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends GameObject{

    public Obstacle(Texture texture, int w, int h, World world, Batch batch) {
        super(texture, w, h, 0, 0, world, batch);
    }
}
