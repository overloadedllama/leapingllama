package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameObjectDied extends  GameObject{

    ParticleEffect gore;

    public GameObjectDied(Texture texture, float x, float y, float h, Batch batch) {
        super(texture, x, y, h, batch);

        gore = new ParticleEffect();
        TextureAtlas textureAtlas = new TextureAtlas();
        textureAtlas.addRegion("reddishGore",new TextureRegion(new Texture("enemies/reddishGore.png")));

        gore.load(Gdx.files.internal("enemies/gore.particles"), textureAtlas);
        gore.start();
        gore.setPosition(w/3+this.getX(),h/3+this.getY());
        gore.scaleEffect(0.02f);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        gore.setPosition(w/3+this.getX(),h/3+this.getY());


    }

    public void draw(float delta){

        gore.update(delta);
        sprite.draw(batch);
        gore.draw(batch);

    }



}
