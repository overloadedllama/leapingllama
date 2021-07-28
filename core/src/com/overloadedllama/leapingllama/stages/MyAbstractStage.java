package com.overloadedllama.leapingllama.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.LlamaConstants;

/**
 * Abstract class to prevent its instantiation and to implement LlamaConstants
 */
public abstract class MyAbstractStage extends Stage implements LlamaConstants {
    GameApp gameApp;
    Assets assets;

    float tableWidth, tableHeight;

    public MyAbstractStage(GameApp gameApp) {
        super(new ExtendViewport(GameApp.WIDTH, GameApp.HEIGHT));
        this.gameApp = gameApp;
        this.assets = gameApp.getAssets();

        tableWidth = GameApp.WIDTH;
        tableHeight = GameApp.HEIGHT;

    }

    public MyAbstractStage(GameApp gameApp, Viewport viewport) {
        super(viewport);
        this.gameApp = gameApp;
        this.assets = gameApp.getAssets();

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
