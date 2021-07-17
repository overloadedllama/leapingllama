package com.overloadedllama.leapingllama.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.assetman.Assets;
import com.overloadedllama.leapingllama.database.LlamaDbHandler;

/**
 *
 *
 * a class that contains all the game settings and methods used to change them on
 * the smartphone.
 *
 */
//We can convert Settings into an Interface, more readable

public final class Settings implements LlamaConstants {
    @SuppressLint("StaticFieldLeak")
    private static LlamaDbHandler llamaDbHandler;
    private static Assets assets;

    public static final String TEST_USER = "User0001";
    private static String currentUser = TEST_USER;

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
    private static Sound cash;
    private static Sound load;
    private static Sound alienGrowl;

    // Music
    private static Music gameMusic;
    private static Music mainMenuMusic;

    // GamePlay
    private static boolean bonusAmmos = false;
    private static boolean bonusLife = false;



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
            return "LEFT HANDED";
        } else if (gameMode == GAME_MODE.DX_LX) {
            return "RIGHT HANDED";
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

    public static String getStringSetting(String setting) {
        final String ON = "ON", OFF = "OFF";
        switch (setting) {
            case LlamaConstants.MUSIC: if (MUSIC) return ON; else return OFF;
            case LlamaConstants.SOUND: if (SOUND) return ON; else return OFF;
            case LlamaConstants.GORE: if (GORE) return ON; else return OFF;
            case LlamaConstants.GAME_MODE_: return getGameMode();
            default:
                throw new IllegalArgumentException("Setting " + setting + " doesn't exist");
        }
    }




    /**
     * Checks if the username doesn't exist yet, and, in that case, creates it,
     * with all his basic values.
     * @param user the new user
     */
    public static void insertNewUser(String user) {
        llamaDbHandler.insertNewUser(user);
    }


    // NEXT METHODS REFER ONLY TO CURRENT USER

    public static int getUserMoney() { return (int) llamaDbHandler.getUserPlayerTableData(currentUser, llamaDbHandler.MONEY); }

    public static boolean checkSetUserMoney(int value) { return llamaDbHandler.checkSetUserMoney(currentUser, value); }

    public static int getUserLevel() { return (int) llamaDbHandler.getUserPlayerTableData(currentUser, llamaDbHandler.MAX_LEVEL); }

    public static void updateUserMaxLevel() { llamaDbHandler.setUserMaxLevel(currentUser); }

    public static int getLevelStarNum(int level) { return llamaDbHandler.getLevelStarNum(currentUser, level); }

    public static void setLevelStarNum(int level, int starNum) { llamaDbHandler.setLevelStarNum(currentUser, level, starNum); }

    /**
     * call the llamaDbHandler method to check and eventually set the new current user best score
     * @param lastScore the last current user score (as distance reached)
     * @param level the level related to the score
     * @return true if lastScore if higher than the bestScore, else false
     */
    public static boolean checkSetNewUserBestScore(int level, double lastScore) {
        return llamaDbHandler.checkSetNewUserBestScore(currentUser, level, lastScore);
    }

    public static double getLevelBestScore(int level) { return llamaDbHandler.getLevelBestScore(currentUser, level); }

    public static void resetAllProgresses() {
        llamaDbHandler.resetProgresses(currentUser);
    }




    // SOUNDS AND MUSICS

    // this method can't be called into constructor method because assets aren't loaded yet
    public static void setBasicSoundsMusics() {
        cash = assets.getSound(CASH);

        mainMenuMusic = assets.getMusic(MAIN_MENU_MUSIC);
    }

    public static void setGameSoundsMusics() {
        punch = assets.getSound(PUNCH);
        shot = assets.getSound(SHOT);
        load = assets.getSound(LOAD);
        alienGrowl = assets.getSound(ALIEN_GROWL);

        gameMusic = assets.getMusic(GAME_MUSIC1);
    }

    public static void playSound(String sound) {
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

    public static void playMusic(String music) {
        if (!MUSIC)
            return;

        switch (music) {
            case GAME_MUSIC1: gameMusic.setLooping(true); gameMusic.play(); break;
            case MAIN_MENU_MUSIC: mainMenuMusic.setLooping(true); mainMenuMusic.play(); break;
            default:
                throw new IllegalArgumentException("Music " + music + " doesn't exists.");
        }
    }

    public static void stopMusic(String music) {
        switch (music) {
            case GAME_MUSIC1: gameMusic.stop(); break;
            case MAIN_MENU_MUSIC: mainMenuMusic.stop(); break;
            default:
                throw new IllegalArgumentException("Music " + music + " doesn't exists.");
        }
    }



    // GAMEPLAY
   public static boolean hasBonusAmmo() {
        return bonusAmmos;
   }

   public static void setBonusAmmo() {
        bonusAmmos = !bonusAmmos;
   }

   public static boolean hasBonusLife() {
        return bonusLife;
   }

   public static void setBonusLife() {
       bonusLife = !bonusLife;
   }

}


