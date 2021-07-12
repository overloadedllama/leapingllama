package com.overloadedllama.leapingllama;

import android.annotation.SuppressLint;
import android.content.Context;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.database.LlamaDbHandler;

/**
 * We can convert Settings into an Interface, more readable?
 *
 * a class that contains all the game settings and methods used to change them on
 * the smartphone.
 *
 */
public final class Settings implements LlamaConstants {
    @SuppressLint("StaticFieldLeak")
    private static LlamaDbHandler llamaDbHandler;
    private static Assets assets;

    public static final String TEST_USER = "test";
    private static String currentUser = TEST_USER;

    private static double userBestScore = -1;

    private static final String ON = "on";
    private static final String OFF = "off";

    // if MUSIC/SOUND/GORE == true, then it is set ON, else OFF
    private static boolean SOUND = true;
    private static float SOUND_VOLUME = 1;
    private static boolean MUSIC = true;
    private static float MUSIC_VOLUME = 1;
    private static boolean GORE = true;

    private enum GAME_MODE
    {
        LX_DX,
        DX_LX,
        GESTURES
    }

    private static GAME_MODE gameMode = GAME_MODE.LX_DX;

    // Sounds
    private static Sound punch;
    private static Sound shot;

    // Music
    private static Music gameMusic;
    private static Music mainMenuMusic;

    // GamePlay
    private static boolean initialAmmos = false;
    private static boolean secondLife = false;



    // METHODS
    public Settings(Context context, Assets assets) {
        llamaDbHandler = new LlamaDbHandler(context);
        Settings.assets = assets;

    }

    public static String getCurrentUser() { return currentUser; }
    public static void setCurrentUser(String user) { currentUser = user; }

    public static boolean isMUSIC() { return MUSIC; }
    public static void setMUSIC(boolean MUSIC) { Settings.MUSIC = MUSIC; }

    public static boolean isSOUND() { return SOUND; }
    public static void setSOUND(boolean SOUND) { Settings.SOUND = SOUND; }

    public static String getGameMode() {
        if (gameMode == GAME_MODE.LX_DX) {
            return "LX_DX";
        } else if (gameMode == GAME_MODE.DX_LX) {
            return "DX_LX";
        } else {
            return "GESTURES";
        }
    }

    public static void setGameMode() {
        if (gameMode == GAME_MODE.LX_DX) {
            gameMode = GAME_MODE.DX_LX;
        } else if (gameMode == GAME_MODE.DX_LX) {
            gameMode = GAME_MODE.GESTURES;
        } else {
        gameMode = GAME_MODE.LX_DX;
        }
    }

    public static boolean isGORE() { return GORE; }
    public static void setGORE(boolean GORE) { Settings.GORE = GORE; }

    // this method converts boolean values returned by above methods into String values
    public static String getSetting(String setting) {

        String ret = ON;    // default value
        switch (setting) {
            case "MUSIC": if (isMUSIC()) ret = ON; else ret = OFF; break;
            case "SOUND": if (isSOUND()) ret = ON; else ret = OFF; break;
            case "GORE": if (isGORE()) ret = ON; else ret = OFF; break;
            case "GAME_MODE":
                if (gameMode == GAME_MODE.LX_DX)
                    ret = "LX_DX";
                else if (gameMode == GAME_MODE.DX_LX)
                    ret = "DX_LX";
                else
                    ret = "GESTURES";
                break;
        }

        return ret;
    }


    public static void insertNewUser(String user) {
        llamaDbHandler.insertNewUser(user);
    }


    // NEXT METHODS REFER ONLY TO CURRENT USER

    public static int getUserMoney() { return (int) llamaDbHandler.getUserPlayerTableData(currentUser, llamaDbHandler.MONEY); }

    public static int getUserLevel() { return (int) llamaDbHandler.getUserPlayerTableData(currentUser, llamaDbHandler.MAX_LEVEL); }

    public static void setUserLevel(int level) { llamaDbHandler.setUserLevel(currentUser); }


    /**
     * call the llamaDbHandler method to check and eventually set the new current user best score
     * @param lastScore the last current user score (as distance reached)
     * @return true if lastScore if higher than the bestScore, else false
     */
    public static boolean checkSetNewUserBestScore(int level, double lastScore) {
        if (llamaDbHandler.checkSetNewUserBestScore(currentUser, level, lastScore)) {
            userBestScore = lastScore;
            return true;
        }
        return false;
    }

    public static double getLevelBestScore(int level) {
        return llamaDbHandler.getLevelBestScore(currentUser, level);
    }


    // SOUNDS AND MUSICS

    /**
     * this method can't be called into constructor method because assets aren't loaded yet
     *
     */
    public static void setSoundsMusics() {
        punch = assets.getSound(PUNCH);
        shot = assets.getSound(SHOT);

        gameMusic = assets.getMusic(GAME_MUSIC1);
        mainMenuMusic = assets.getMusic(MAIN_MENU_MUSIC);
    }

    public static void playSound(String sound) {
        if (!SOUND)
            return;

        switch (sound) {
            case PUNCH: punch.play(SOUND_VOLUME); break;
            case SHOT: shot.play(SOUND_VOLUME); break;
        }
    }

    public static void playMusic(String music) {
        if (!MUSIC)
            return;

        switch (music) {
            case GAME_MUSIC1: gameMusic.setLooping(true); gameMusic.play(); break;
            case MAIN_MENU_MUSIC: mainMenuMusic.setLooping(true); mainMenuMusic.play(); break;

        }
    }

    public static void stopMusic(String music) {
        switch (music) {
            case GAME_MUSIC1: gameMusic.stop();
            case MAIN_MENU_MUSIC: mainMenuMusic.stop();
        }
    }



    // GAMEPLAY
   public static boolean hasInitialAmmos() {
        return initialAmmos;
   }

   public static void setInitialAmmos() {
        initialAmmos = !initialAmmos;
   }

   public static boolean hasSecondLife() {
        return secondLife;
   }

   public static void setSecondLife() {
       secondLife = !secondLife;
   }

}


