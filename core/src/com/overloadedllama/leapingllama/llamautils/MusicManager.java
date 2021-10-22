package com.overloadedllama.leapingllama.llamautils;

import com.badlogic.gdx.audio.Music;

public class MusicManager implements Manager {
    private static MusicManager uniqueInstance;

    private final LlamaAssetManager assetManager;

    private boolean MUSIC = true;

    // Musics
    private static Music gameMusic;
    private static Music mainMenuMusic;



    private MusicManager(LlamaAssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public static MusicManager getUniqueInstance(LlamaAssetManager assetManager) {
        if (uniqueInstance == null) {
            uniqueInstance = new MusicManager(assetManager);
        }
        return uniqueInstance;
    }


    public boolean isMUSIC() { return MUSIC; }
    public void setMUSIC(boolean MUSIC) { this.MUSIC = MUSIC; }


    public void setMainMenuMusic() {
        mainMenuMusic = assetManager.getMusic(MAIN_MENU_MUSIC);
    }

    public void setGameMusic() {
        gameMusic = assetManager.getMusic(GAME_MUSIC1);
    }


    public void playMusic(String music) {
        if (!MUSIC)
            return;

        switch (music) {
            case GAME_MUSIC1: gameMusic.setLooping(true); gameMusic.play(); break;
            case MAIN_MENU_MUSIC: mainMenuMusic.setLooping(true); mainMenuMusic.play(); break;
            default:
                throw new IllegalArgumentException("Music " + music + " doesn't exists.");
        }
    }

    public void stopMusic(String music) {
        switch (music) {
            case GAME_MUSIC1: gameMusic.stop(); break;
            case MAIN_MENU_MUSIC: mainMenuMusic.stop(); break;
            default:
                throw new IllegalArgumentException("Music " + music + " doesn't exists.");
        }
    }

}
