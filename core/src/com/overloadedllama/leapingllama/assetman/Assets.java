package com.overloadedllama.leapingllama.assetman;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private final AssetManager manager;

    public Assets() {
        this.manager = new AssetManager();
    }

    public void loadAllAssets() {
        // adds all the skins to the loading queue
        SkinLoader.SkinParameter bigButton = new SkinLoader.SkinParameter("ui/bigButton.atlas");
        SkinLoader.SkinParameter crouchButton = new SkinLoader.SkinParameter("ui/crouchButton.atlas");
        SkinLoader.SkinParameter fistButton = new SkinLoader.SkinParameter("ui/fistButton.atlas");
        SkinLoader.SkinParameter jumpButton = new SkinLoader.SkinParameter("ui/jumpButton.atlas");
        SkinLoader.SkinParameter shotButton = new SkinLoader.SkinParameter("ui/shotButton.atlas");
        SkinLoader.SkinParameter pauseButton = new SkinLoader.SkinParameter("ui/pauseButton.atlas");
        SkinLoader.SkinParameter coin = new SkinLoader.SkinParameter("ui/coin.atlas");

        // loads all the skins
        manager.load("ui/bigButton.json", Skin.class, bigButton);
        manager.load("ui/coin.json", Skin.class, coin);
        manager.load("ui/crouchButton.json", Skin.class, crouchButton);
        manager.load("ui/fistButton.json", Skin.class, fistButton);
        manager.load("ui/jumpButton.json", Skin.class, jumpButton);
        manager.load("ui/shotButton.json", Skin.class, shotButton);
        manager.load("ui/pauseButton.json", Skin.class, pauseButton);

        // loading textures
        manager.load("enemies/alienCyan.png", Texture.class);
        manager.load("enemies/alienOrange.png", Texture.class);
        manager.load("llama/llamaStanding.png", Texture.class);
        manager.load("llama/llamaCrouching.png", Texture.class);
        manager.load("llama/llamaPunching.png", Texture.class);
        manager.load("bullets/base_bullet.png", Texture.class);
        manager.load("world/sky.png", Texture.class);
        manager.load("world/ground.png", Texture.class);
        manager.load("world/platform.png", Texture.class);
        manager.load("screen_backgrounds/game_over.png", Texture.class);

        manager.finishLoading();
    }

    public void unloadBigLittleButtons() {
        manager.unload("ui/bigButton.json");
        manager.unload("ui/littleButton.json");
    }

    // switch case here
    public Skin getBig() { return manager.get("ui/bigButton.json"); }
    public Skin getCoin() { return manager.get("ui/coin.json"); }
    public Skin getCrouch() { return manager.get("ui/crouchButton.json"); }
    public Skin getFist() { return manager.get("ui/fistButton.json"); }
    public Skin getJump() { return manager.get("ui/jumpButton.json"); }
    public Skin getShot() { return manager.get("ui/shotButton.json"); }
    public Skin getPause() { return manager.get("ui/pauseButton.json"); }

    public boolean update() {
        return manager.update();
    }

    public Texture getTexture(String texture) {
        switch (texture) {
            case "alienOrange": return manager.get("enemies/alienOrange.png");
            case "alienCyan": return manager.get("enemies/alienCyan.png");
            case "llamaStanding": return manager.get("llama/llamaStanding.png");
            case "llamaCrouching": return manager.get("llama/llamaCrouching.png");
            case "llamaPunching": return manager.get("llama/llamaPunching.png");
            case "base_bullet": return manager.get("bullets/base_bullet.png");
            case "sky": return manager.get("world/sky.png");
            case "ground": return manager.get("world/ground.png");
            case "platform": return manager.get("world/platform.png");
        }
        return null;
    }

}
