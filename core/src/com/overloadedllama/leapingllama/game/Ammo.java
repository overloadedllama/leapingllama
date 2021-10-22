package com.overloadedllama.leapingllama.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.GameStage;

public class Ammo extends AbstractGameObjectLabel {


    public Ammo(float x, float y, float h, int quantity, GameStage stage, LlamaUtil llamaUtil){
        super(AMMO, x, y, h, quantity, stage, "pixeledWhite", llamaUtil);

        PolygonShape ammoShape = new PolygonShape();
        ammoShape.setAsBox(w/2, h/2);

        super.createBody(BodyDef.BodyType.DynamicBody, ammoShape, 20, 1f, 0f);

        ammoShape.dispose();

        //body.setLinearVelocity(-3f, 5f);

    }



}
