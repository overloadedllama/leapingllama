package com.overloadedllama.leapingllama.llamautils;

import com.overloadedllama.leapingllama.GameApp;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.database.LlamaDbHandler;

public class LlamaUtil implements LlamaConstants {
    private static LlamaUtil uniqueInstance;

    private final LlamaDbHandler llamaDbHandler;
    private final MusicManager musicManager;
    private final SoundManager soundManager;
    private final LlamaAssetManager assetManager;
    private final GameplayManager gameplayManager;

    private String currentUser;

    private final GameApp gameApp;


    private LlamaUtil(GameApp gameApp) {
        this.gameApp = gameApp;

        currentUser = "user0001";
        llamaDbHandler = LlamaDbHandler.getUniqueInstance(gameApp.context);

        assetManager = LlamaAssetManager.getUniqueInstance();

        musicManager = MusicManager.getUniqueInstance(assetManager);
        soundManager = SoundManager.getUniqueInstance(assetManager);
        gameplayManager = GameplayManager.getUniqueInstance();

    }

    public static LlamaUtil getUniqueInstance(GameApp gameApp) {
        if (uniqueInstance == null) {
            uniqueInstance = new LlamaUtil(gameApp);
        }
        return uniqueInstance;
    }

    public LlamaDbHandler getLlamaDbHandler() { return llamaDbHandler; }

    public MusicManager getMusicManager() { return musicManager; }

    public SoundManager getSoundManager() { return soundManager; }

    public LlamaAssetManager getAssetManager() { return assetManager; }

    public GameplayManager getGameplayManager() { return gameplayManager; }

    public GameApp getGameApp() {
        return gameApp;
    }

    public String getCurrentUser() { return currentUser; }

    public void setCurrentUser(String user) { currentUser = user; }


    public String getStringSetting(String setting) {
        final String ON = "ON", OFF = "OFF";
        switch (setting) {
            case MUSIC: if (musicManager.isMUSIC()) return ON; else return OFF;
            case SOUND: if (soundManager.isSOUND()) return ON; else return OFF;
            case GORE: if (gameplayManager.isGORE()) return ON; else return OFF;
            case GAME_MODE_: return gameplayManager.getGameMode();
            default:
                throw new IllegalArgumentException("Setting " + setting + " doesn't exist");
        }
    }


}
