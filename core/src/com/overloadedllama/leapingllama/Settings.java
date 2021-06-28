package com.overloadedllama.leapingllama;

/**
 * a class that contains all the game settings and methods used to change them on
 * the smartphone.
 */
public class Settings {

    // if MUSIC/SOUND/GORE == true, then it is set ON, else OFF
    private static boolean MUSIC;
    private static boolean SOUND;
    private static boolean GORE;

    // if true is set for dx-players, else for sx-players
    private static boolean LX_DX;


    public Settings() {
        MUSIC = true;
        SOUND = true;
        GORE = true;
        LX_DX = true;
    }


    public static boolean isLxDx() { return LX_DX; }

    public static void setLxDx(boolean lxDx) { LX_DX = lxDx; }

    public static boolean isMUSIC() { return MUSIC; }

    public static void setMUSIC(boolean MUSIC) { Settings.MUSIC = MUSIC; }

    public static boolean isSOUND() { return SOUND; }

    public static void setSOUND(boolean SOUND) { Settings.SOUND = SOUND; }

    public static boolean isGORE() { return GORE; }

    public static void setGORE(boolean GORE) { Settings.GORE = GORE; }
}
