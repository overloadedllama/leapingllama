package com.overloadedllama.leapingllama.assetman;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.overloadedllama.leapingllama.LlamaConstants;

/**
 * Assets are loaded here in Assets through LibGdx class AssetManager
 * Once loaded, they are actually accessed by getAsset() methods (such as getSkin(), getSound()...)
 *
 * Sounds are managed only in Settings class using playSound()
 */
public class Assets implements LlamaConstants {
    private final AssetManager manager;


    public Assets() {
        this.manager = new AssetManager();
    }

    public void loadGameAssets() {
        SkinLoader.SkinParameter crouchButton = new SkinLoader.SkinParameter("ui/crouchButton.atlas");
        SkinLoader.SkinParameter fistButton = new SkinLoader.SkinParameter("ui/fistButton.atlas");
        SkinLoader.SkinParameter jumpButton = new SkinLoader.SkinParameter("ui/jumpButton.atlas");
        SkinLoader.SkinParameter shotButton = new SkinLoader.SkinParameter("ui/shotButton.atlas");
        SkinLoader.SkinParameter pauseButton = new SkinLoader.SkinParameter("ui/pauseButton.atlas");

        manager.load("ui/crouchButton.json", Skin.class, crouchButton);
        manager.load("ui/fistButton.json", Skin.class, fistButton);
        manager.load("ui/jumpButton.json", Skin.class, jumpButton);
        manager.load("ui/shotButton.json", Skin.class, shotButton);
        manager.load("ui/pauseButton.json", Skin.class, pauseButton);

        // loading textures
        manager.load("enemies/alienCyan.png", Texture.class);
        manager.load("enemies/alienOrangeFlying.png", Texture.class);
        manager.load("llama/llamaStanding.png", Texture.class);
        manager.load("llama/llamaCrouching.png", Texture.class);
        manager.load("llama/llamaPunching.png", Texture.class);
        manager.load("bullets/base_bullet.png", Texture.class);
        manager.load("world/ground.png", Texture.class);
        manager.load("world/platform.png", Texture.class);
        manager.load("enemies/alienYellow.png", Texture.class);

        manager.load("llama/llamaWalking.png", Texture.class);
        manager.load("enemies/alienYellowWalking.png", Texture.class);
        manager.load("enemies/alienCyanWalking.png", Texture.class);
        manager.load("enemies/alienOrangeFlyingMoving.png", Texture.class);

        // loading sounds
        manager.load("sounds/punch.wav", Sound.class);
        manager.load("sounds/laser.wav", Sound.class);
        manager.load("sounds/rifleLoad.wav", Sound.class);

        // loading musics
        manager.load("musics/gameMusic1.mp3", Music.class);

    }


    public void unloadGameAssets() {
        // unload skins
        manager.unload("ui/crouchButton.json");
        manager.unload("ui/fistButton.json");
        manager.unload("ui/jumpButton.json");
        manager.unload("ui/shotButton.json");
        manager.unload("ui/pauseButton.json");

        // unload textures
        manager.unload("enemies/alienCyan.png");
        manager.unload("enemies/alienOrangeFlying.png");
        manager.unload("llama/llamaStanding.png");
        manager.unload("llama/llamaCrouching.png");
        manager.unload("llama/llamaPunching.png");
        manager.unload("bullets/base_bullet.png");
        manager.unload("world/ground.png");
        manager.unload("world/platform.png");
        manager.unload("enemies/alienYellow.png");

        manager.unload("llama/llamaWalking.png");
        manager.unload("enemies/alienYellowWalking.png");
        manager.unload("enemies/alienCyanWalking.png");
        manager.unload("enemies/alienYellowWalking.png");
        manager.unload("enemies/alienOrangeFlyingMoving.png");

        // unload sounds
        manager.unload("sounds/punch.wav");
        manager.unload("sounds/laser.wav");

        // unload musics
        manager.unload("music/gameMusic.mp3");

    }
    
    public void loadBasicAssets() {
        SkinLoader.SkinParameter bigButton = new SkinLoader.SkinParameter("ui/bigButton.atlas");
        SkinLoader.SkinParameter backButton = new SkinLoader.SkinParameter("ui/backButton.atlas");
        SkinLoader.SkinParameter coin = new SkinLoader.SkinParameter("ui/coin.atlas");
        SkinLoader.SkinParameter ammo = new SkinLoader.SkinParameter("ui/ammo.atlas");
        SkinLoader.SkinParameter justTextButton = new SkinLoader.SkinParameter("ui/justTextButton.atlas");
        SkinLoader.SkinParameter leftArrow = new SkinLoader.SkinParameter("ui/leftArrow.atlas");
        SkinLoader.SkinParameter rightArrow = new SkinLoader.SkinParameter("ui/rightArrow.atlas");

        // loading skins
        manager.load("ui/bigButton.json", Skin.class, bigButton);
        manager.load("ui/backButton.json", Skin.class, backButton);
        manager.load("ui/coin.json", Skin.class, coin);
        manager.load("ui/ammo.json", Skin.class, ammo);
        manager.load("ui/justTextButton.json", Skin.class, justTextButton);
        manager.load("ui/leftArrow.json", Skin.class, leftArrow);
        manager.load("ui/rightArrow.json", Skin.class, rightArrow);


        // loading textures
        manager.load("world/sky.png", Texture.class);
        manager.load("screen_backgrounds/quiteBlack.png", Texture.class);
        manager.load("world/starWon.png", Texture.class);
        manager.load("world/starLost.png", Texture.class);

        // loading sounds
        manager.load("sounds/cashRegister.wav", Sound.class);

        // loading musics
        manager.load("musics/mainMenuMusic.mp3", Music.class);

    }

    public Skin getSkin(String skin) {
        switch (skin) {
            case "bigButton":  return manager.get("ui/bigButton.json");
            case "backButton": return manager.get("ui/backButton.json");
            case "coin":  return manager.get("ui/coin.json");
            case "crouch":  return manager.get("ui/crouchButton.json");
            case "punch":  return manager.get("ui/fistButton.json");
            case "jump":  return manager.get("ui/jumpButton.json");
            case "shot": return manager.get("ui/shotButton.json");
            case "pause":  return manager.get("ui/pauseButton.json");
            case "ammo": return  manager.get("ui/ammo.json");
            case "justText": return manager.get("ui/justTextButton.json");
            case "leftArrow": return manager.get("ui/leftArrow.json");
            case "rightArrow": return manager.get ("ui/rightArrow.json");
            default:
                throw new IllegalArgumentException("Skin " + skin + " doesn't exist.");
        }
    }

    public Texture getTexture(String texture) {
        switch (texture) {
            case "alienOrangeFlying": return manager.get("enemies/alienOrangeFlying.png");
            case "alienCyan": return manager.get("enemies/alienCyan.png");
            case "alienYellow": return manager.get("enemies/alienYellow.png");
            case "alienCyanWalking": return manager.get("enemies/alienCyanWalking.png");
            case "alienYellowWalking": return manager.get("enemies/alienYellowWalking.png");
            case "alienOrangeFlyingMoving": return manager.get("enemies/alienOrangeFlyingMoving.png");
            case "llamaStanding": return manager.get("llama/llamaStanding.png");
            case "llamaCrouching": return manager.get("llama/llamaCrouching.png");
            case "llamaPunching": return manager.get("llama/llamaPunching.png");
            case "llamaWalking": return manager.get("llama/llamaWalking.png");
            case "sky": return manager.get("world/sky.png");
            case "ground": return manager.get("world/ground.png");
            case "platform": return manager.get("world/platform.png");
            case "base_bullet": return manager.get("bullets/base_bullet.png");
            case "quiteBlack": return manager.get("screen_backgrounds/quiteBlack.png");
            case "starWon": return manager.get("world/starWon.png");
            case "starLost": return manager.get("world/starLost.png");
            default:
                throw new IllegalArgumentException("Texture " + texture + "doesn't exist.");
        }
    }

    public Sound getSound(String sound) {
        switch (sound) {
            case PUNCH: return manager.get("sounds/punch.wav");
            case SHOT: return manager.get("sounds/laser.wav");
            case CASH: return manager.get("sounds/cashRegister.wav");
            case LOAD: return manager.get("sounds/rifleLoad.wav");
            default:
                throw new IllegalArgumentException("Sound " + sound + " doesn't exist.");
        }
    }

    public Music getMusic(String music) {
        switch (music) {
            case GAME_MUSIC1: return manager.get("musics/gameMusic1.mp3");
            case MAIN_MENU_MUSIC: return manager.get("musics/mainMenuMusic.mp3");
            default:
                throw new IllegalArgumentException("Music " + music + " doesn't exist.");
        }
    }

    public boolean update() { return manager.update(); }

    public float getProgress() { return manager.getProgress(); }

    public void disposeAll() { manager.dispose(); }

}
