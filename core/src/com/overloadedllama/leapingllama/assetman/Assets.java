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

    /**
     * loads every asset needed, except for loadScreen's assets
     */
    public void loadAllAssets() {
        // adds all the skins to the loading queue
        SkinLoader.SkinParameter bigButton = new SkinLoader.SkinParameter("ui/bigButton.atlas");
        SkinLoader.SkinParameter crouchButton = new SkinLoader.SkinParameter("ui/crouchButton.atlas");
        SkinLoader.SkinParameter fistButton = new SkinLoader.SkinParameter("ui/fistButton.atlas");
        SkinLoader.SkinParameter jumpButton = new SkinLoader.SkinParameter("ui/jumpButton.atlas");
        SkinLoader.SkinParameter shotButton = new SkinLoader.SkinParameter("ui/shotButton.atlas");
        SkinLoader.SkinParameter pauseButton = new SkinLoader.SkinParameter("ui/pauseButton.atlas");
        SkinLoader.SkinParameter coin = new SkinLoader.SkinParameter("ui/coin.atlas");

        // loading skins
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
        manager.load("screen_backgrounds/quiteBlack.png", Texture.class);

    }

    // switch case here
    public Skin getSkin(String skin) {
        switch (skin) {
            case "bigButton":  return manager.get("ui/bigButton.json");
            case "coin":  return manager.get("ui/coin.json");
            case "crouch":  return manager.get("ui/crouchButton.json");
            case "punch":  return manager.get("ui/fistButton.json");
            case "jump":  return manager.get("ui/jumpButton.json");
            case "shot": return manager.get("ui/shotButton.json");
            case "pause":  return manager.get("ui/pauseButton.json");
        }
        return null;
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
            case "game_over": return manager.get("screen_backgrounds/game_over.png");
            case "quiteBlack": return manager.get("screen_backgrounds/quiteBlack.png");
        }
        return null;
    }

    public boolean update() { return manager.update(); }

    public float getProgress() { return manager.getProgress(); }

}
