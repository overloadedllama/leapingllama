package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class AbstractGameObjectDied extends AbstractGameObject {

    ParticleEffect gore;

    public AbstractGameObjectDied(String name, float x, float y, float h, LlamaUtil llamaUtil) {
        super(name, x, y, h, llamaUtil);

        if (llamaUtil.getGameplayManager().isGORE()) {
            gore = new ParticleEffect();
            TextureAtlas textureAtlas = new TextureAtlas();
            textureAtlas.addRegion("reddishGore", new TextureRegion(new Texture("enemies/reddishGore.png")));

            gore.load(Gdx.files.internal("enemies/gore.particles"), textureAtlas);
            gore.start();
            gore.setPosition(w / 3 + this.getX(), h / 3 + this.getY());
            gore.scaleEffect(0.02f);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (gore != null)
            gore.setPosition(w/3+this.getX(),h/3+this.getY());
    }

    public void draw(float delta){
        sprite.draw(batch);

        if (gore != null) {
            gore.update(delta);
            gore.draw(batch);
        }
    }



}
