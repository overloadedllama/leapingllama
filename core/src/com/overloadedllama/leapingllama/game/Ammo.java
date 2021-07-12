package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;

public class Ammo extends GameObjectLabel {


    public Ammo(float x, float y, float h, int quantity, World world, Batch batch, Assets assets, ButtonsStagePlay stage){
        super(assets.getSkin("ammo"), x, y, h, quantity, world, batch, stage, "pixeledWhite");


        PolygonShape ammoShape = new PolygonShape();
        ammoShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, ammoShape, 20, 1f, 0f);

        ammoShape.dispose();

        //body.setLinearVelocity(-3f, 5f);

    }



}
