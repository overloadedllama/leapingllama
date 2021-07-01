package com.overloadedllama.leapingllama.assetman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// NOT FINISHED
public class AssetMan {
    private final AssetManager manager;

    public AssetMan() {
        this.manager = new AssetManager();
    }

    public void loadAllAssets() {
        SkinLoader.SkinParameter bigButton = new SkinLoader.SkinParameter("ui/bigButton.atlas");
        SkinLoader.SkinParameter littleButton = new SkinLoader.SkinParameter("ui/littleButton.atlas");

        /*
        SkinLoader.SkinParameter crouchButton = new SkinLoader.SkinParameter("ui/crouchButton.atlas");
        SkinLoader.SkinParameter fistButton = new SkinLoader.SkinParameter("ui/fistButton.atlas");
        SkinLoader.SkinParameter jumpButton = new SkinLoader.SkinParameter("ui/jumpButton.atlas");
        SkinLoader.SkinParameter shotButton = new SkinLoader.SkinParameter("ui/shotButton.atlas");

        SkinLoader.SkinParameter pauseButton = new SkinLoader.SkinParameter("ui/pauseButton.atlas");
        */
        SkinLoader.SkinParameter coin = new SkinLoader.SkinParameter("ui/coin.atlas");

        manager.load("ui/bigButton.json", Skin.class, bigButton);
        manager.load("ui/littleButton.json", Skin.class, littleButton);

    }

    public void unloadBigLittleButtons() {
        manager.unload("ui/bigButton.json");
        manager.unload("ui/littleButton.json");
    }

    public Skin getBig() {
        return manager.get("ui/bigButton.json");
    }

    public Skin getLittle() {
        return manager.get("ui/littleButton.json");
    }

    public boolean update() {
        return manager.update();
    }

}
