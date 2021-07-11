package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.overloadedllama.leapingllama.assetman.Assets;

public class EnemyDied extends  GameObjectDied{
    public EnemyDied(Texture texture, float x, float y, float h, Batch batch, Assets assets) {
        super(texture, x, y, h, batch);
    }
}
