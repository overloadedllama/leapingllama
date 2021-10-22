package com.overloadedllama.leapingllama.llamautils;

import com.badlogic.gdx.audio.Sound;


public class SoundManager implements Manager {
    private static SoundManager uniqueInstance;

    private final LlamaAssetManager llamaAssetManager;

    private boolean SOUND = true;
    private final float SOUND_VOLUME = 1;

    // Sounds
    private Sound punch;
    private Sound shot;
    private Sound cash;
    private Sound load;
    private Sound alienGrowl;


    private SoundManager(LlamaAssetManager llamaAssetManager) {
        this.llamaAssetManager = llamaAssetManager;
    }

    public static SoundManager getUniqueInstance(LlamaAssetManager llamaAssetManager) {
        if (uniqueInstance == null) {
            uniqueInstance = new SoundManager(llamaAssetManager);
        }
        return uniqueInstance;
    }




    public boolean isSOUND() { return SOUND; }
    public void setSOUND(boolean SOUND) { this.SOUND = SOUND; }


    public void setSounds() {
        cash = llamaAssetManager.getSound(CASH);
        punch = llamaAssetManager.getSound(PUNCH);
        shot = llamaAssetManager.getSound(SHOT);
        load = llamaAssetManager.getSound(LOAD);
        alienGrowl = llamaAssetManager.getSound(ALIEN_GROWL);

    }


    public void playSound(String sound) {
        if (!SOUND)
            return;

        switch (sound) {
            case PUNCH: punch.play(SOUND_VOLUME); break;
            case SHOT: shot.play(SOUND_VOLUME); break;
            case CASH: cash.play(SOUND_VOLUME); break;
            case LOAD: load.play(SOUND_VOLUME); break;
            case ALIEN_GROWL: alienGrowl.play(SOUND_VOLUME); break;
            default:
                throw new IllegalArgumentException("Sound " + sound + " doesn't exists.");
        }
    }

}
