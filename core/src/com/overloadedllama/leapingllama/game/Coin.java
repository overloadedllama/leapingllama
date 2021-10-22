package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.GameStage;

public class Coin extends AbstractGameObjectLabel {


    public Coin(float x, float y, float h, int quantity, GameStage stage, LlamaUtil llamaUtil){
        super(COIN, x, y, h, quantity, stage, "pixeled", llamaUtil);


        PolygonShape coinShape = new PolygonShape();
        coinShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, coinShape, 20, 1f, 0f);

        coinShape.dispose();

        //body.setLinearVelocity(-3f, 5f);


    }

}
