package com.overloadedllama.leapingllama.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Null;
import com.overloadedllama.leapingllama.assetman.Assets;

import java.util.Random;

public class Enemy extends GameObject{

    public Enemy(float x, float y, float h, World world, Batch batch, Assets assets){
        super(assets.getTexture("alienCyan"), x, y, h, world, batch);

        Random random = new Random();
        int randomNumber = random.nextInt(2);

        if(randomNumber == 1){
            texture = assets.getTexture("alienYellow");
            sprite.setTexture(texture);
        }

        PolygonShape enemyShape = new PolygonShape();
        enemyShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, enemyShape, 20, 1f, 0f);

        enemyShape.dispose();

        body.setLinearVelocity(-3f, 5f);
        body.setFixedRotation(true);
    }

    ParticleEffect gore;
    Boolean isDead = false;

    public Enemy(Texture texture, float x, float y, float h, SpriteBatch batch, Assets assets, boolean shot) {
        super(texture, x, y, h, batch);



        if(shot){

            gore = new ParticleEffect();
            TextureAtlas textureAtlas = new TextureAtlas();
            textureAtlas.addRegion("reddishGore",new TextureRegion(new Texture("enemies/reddishGore.png")));

            gore.load(Gdx.files.internal("enemies/gore.particles"), textureAtlas);
            gore.start();
            gore.setPosition(w/2+this.getX(),h/2+this.getY());
            gore.scaleEffect(0.02f);
        }

    }

    public void draw(float delta){

        gore.update(delta);
        sprite.draw(batch);
        gore.draw(batch);

    }
}