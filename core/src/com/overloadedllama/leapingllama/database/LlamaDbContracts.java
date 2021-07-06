package com.overloadedllama.leapingllama.database;

import android.provider.BaseColumns;

import java.util.Set;


/**
 * Table Player:
 *      user VARCHAR(20) PK,
 *      score REAL NOT NULL DEFAULT 0,
 *      money INT NOT NULL DEFAULT 0,
 *      level INT NOT NULL DEFAULT 0
 *
 * Table Settings:
 *      user VARCHAR(20) PK REFERENCES Player
 *      music INT NOT NULL DEFAULT 0,
 *      sound INT NOT NULL DEFAULT 0,
 *      tan INT NOT NULL DEFAULT 0,
 *      t-shirt INT NOT NULL DEFAULT 0,
 *      gun INT NOT NULL DEFAULT 0,
 *      gore INT NOT NULL DEFAULT 0,
 *
 */
public class LlamaDbContracts {

    // as Android Studio doc suggests, this class should not be
    // instantiated, so make the constructor private
    private LlamaDbContracts() { }

    // By implementing the BaseColumns interface, your inner class
    // can inherit a primary key field called _ID that some Android classes
    // such as CursorAdapter expect it to have
    public static class Player implements BaseColumns {
        public static final String TABLE_NAME = "player";

        public static final String PRIMARY_KEY = "user";
        public static final String SCORE_COLUMN = "score";
        public static final String MONEY_COLUMN = "money";
        public static final String LEVEL_COLUMN = "level";

    }

    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "settings";

        public static final String PRIMARY_KEY = "user";
        public static final String TAN_COLUMN = "tan";
        public static final String T_SHIRT_COLUMN = "t_shirt";
        public static final String GUN_COLUMN = "gun";
        public static final String MUSIC_COLUMN = "music";
        public static final String SOUND_COLUMN = "sound";
        public static final String GORE_COLUMN = "gore";

    }

    /**
     * Strings referred to Player Table
     */
    public static final String SQL_PLAYER_CREATE =
            "CREATE TABLE " + Player.TABLE_NAME + " (" +
                    Player.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY, " +
                    Player.SCORE_COLUMN + " REAL NOT NULL DEFAULT 0, " +
                    Player.MONEY_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Player.LEVEL_COLUMN + " INT NOT NULL DEFAULT 0)";


    public static final String SQL_PLAYER_DROP =
            "DROP TABLE IF EXISTS " + Player.TABLE_NAME;


    /**
     * Strings referred to Settings Table
     */
    public static final String SQL_SETTINGS_CREATE =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY REFERENCES Player (user), " +
                    Settings.MUSIC_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Settings.SOUND_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Settings.TAN_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Settings.T_SHIRT_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Settings.GUN_COLUMN + " INT NOT NULL DEFAULT 0, " +
                    Settings.GORE_COLUMN + " INT NOT NULL DEFAULT 0)";

    public static final String SQL_SETTINGS_DROP =
            "DROP TABLE IF EXISTS " + Settings.TABLE_NAME;

}
