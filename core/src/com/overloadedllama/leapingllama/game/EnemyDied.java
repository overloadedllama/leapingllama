package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.overloadedllama.leapingllama.assetman.Assets;

public class EnemyDied extends  GameObjectDied{
    public EnemyDied(String texture, float x, float y, float h, Batch batch, Assets assets) {

        super(new Texture(Gdx.files.internal(texture.split(".png")[0] + "Dead.png")), x, y, h, batch); System.out.println(texture);
    }
}
