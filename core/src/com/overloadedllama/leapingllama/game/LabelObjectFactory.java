package com.overloadedllama.leapingllama.game;

import com.overloadedllama.leapingllama.llamautils.LlamaUtil;
import com.overloadedllama.leapingllama.stages.GameStage;


public class LabelObjectFactory extends AbstractFactory{
    GameStage stage;

    public LabelObjectFactory(LlamaUtil llamaUtil, GameStage stage) {
        super(llamaUtil);
        this.stage = stage;
    }

    public GameObject createLabelGameObject(String type, float x, float y, float h, int qty) {
        switch (type) {
            case AMMO:
                return new Ammo(x, y, h, qty, stage, llamaUtil);
            case COIN:
                return new Coin(x, y, h, qty, stage, llamaUtil);
            default:
                throw new RuntimeException("Game Object Type " + type + " does not exist.");
        }
    }

    public void setStage(GameStage stage) {
        if (this.stage == null)
            this.stage = stage;
    }
}
