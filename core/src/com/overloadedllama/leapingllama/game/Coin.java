package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.stages.ButtonsStagePlay;

public class Coin extends GameObjectLabel {


    public Coin(float x, float y, float h, int quantity, World world, Batch batch, Assets assets, ButtonsStagePlay stage){
        super(assets.getSkin("coin"), x, y, h, quantity, world, batch, stage, "pixeled");


        PolygonShape coinShape = new PolygonShape();
        coinShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, coinShape, 20, 1f, 0f);

        coinShape.dispose();

        //body.setLinearVelocity(-3f, 5f);


    }

}
