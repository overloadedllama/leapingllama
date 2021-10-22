package com.overloadedllama.leapingllama.game;

import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class EnemyDied extends AbstractGameObjectDied {
    public EnemyDied(String name, float x, float y, float h, LlamaUtil llamaUtil) {
        super(name, x, y, h, llamaUtil);
        sprite.rotate90(true);
    }
}
