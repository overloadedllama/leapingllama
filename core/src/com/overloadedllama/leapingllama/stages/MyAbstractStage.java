package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

/**
 * Abstract class to prevent its instantiation and to implement LlamaConstants
 */
public abstract class MyAbstractStage extends Stage implements LlamaConstants {
    LlamaUtil llamaUtil;
    String currentUser;
    float tableWidth, tableHeight;

    public MyAbstractStage(LlamaUtil llamaUtil) {
        super(new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT));
        this.llamaUtil = llamaUtil;

        currentUser = llamaUtil.getCurrentUser();

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

    }

    public MyAbstractStage(LlamaUtil llamaUtil, Viewport viewport) {
        super(viewport);
        this.llamaUtil = llamaUtil;

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

    }

    public void renderer() {
        act();
        draw();
    }

    public abstract void resizer();

    abstract void setUpButtons();

}
