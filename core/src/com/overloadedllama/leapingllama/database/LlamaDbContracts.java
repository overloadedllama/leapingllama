package com.overloadedllama.leapingllama.database;

import android.provider.BaseColumns;

/**
 * Table Player:
 *      user VARCHAR(20) PK,
 *      money INT NOT NULL DEFAULT 0,
 *      levelReached INT NOT NULL DEFAULT 0
 *
 *
 *  It is not the best solution, a table with (user, numLevel) as PK would works best
 *  Table Level
 *      user VARCHAR(20) PK REFERENCES Player,
 *      level0 REAL NOT NULL DEFAULT 0,
 *      level1 REAL NOT NULL DEFAULT 0,
 *      level2 REAL NOT NULL DEFAULT 0,
 *      level3 REAL NOT NULL DEFAULT 0,
 *      level4 REAL NOT NULL DEFAULT 0,
 *      level5 REAL NOT NULL DEFAULT 0
 *
 * Are music/sounds really necessaries?
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
        public static final String MONEY = "money";
        public static final String MAX_LEVEL = "max_level";

    }

    public static class Level implements BaseColumns {
        public static final String TABLE_NAME = "level";
        public static final String PRIMARY_KEY = "user";
        public static final String LEVEL_0 = "level0";
        public static final String LEVEL_1 = "level1";
        public static final String LEVEL_2 = "level2";
        public static final String LEVEL_3 = "level3";
        public static final String LEVEL_4 = "level4";
        public static final String LEVEL_5 = "level5";
    }

    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String PRIMARY_KEY = "user";
        public static final String TAN = "tan";
        public static final String T_SHIRT = "t_shirt";
        public static final String GUN = "gun";
        public static final String MUSIC = "music";
        public static final String SOUND = "sound";
        public static final String GORE = "gore";

    }

    /**
     * Strings referred to Player Table
     */
    public static final String SQL_PLAYER_CREATE =
            "CREATE TABLE " + Player.TABLE_NAME + " (" +
                    Player.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY, " +
          //          Player.SCORE_COLUMN + " REAL NOT NULL DEFAULT 0, " +
                    Player.MONEY + " INT NOT NULL DEFAULT 0, " +
                    Player.MAX_LEVEL + " INT NOT NULL DEFAULT 0)";


    public static final String SQL_PLAYER_DROP =
            "DROP TABLE IF EXISTS " + Player.TABLE_NAME;


    /**
     * String referred to Level Table
     */
    public static final String SQL_CREATE_LEVEL =
            "CREATE TABLE " + Level.TABLE_NAME + " (" +
                    Level.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY REFERENCES Player, " +
                    Level.LEVEL_0 + " REAL NOT NULL DEFAULT 0, " +
                    Level.LEVEL_1 + " REAL NOT NULL DEFAULT 0, " +
                    Level.LEVEL_2 + " REAL NOT NULL DEFAULT 0, " +
                    Level.LEVEL_3 + " REAL NOT NULL DEFAULT 0, " +
                    Level.LEVEL_4 + " REAL NOT NULL DEFAULT 0, " +
                    Level.LEVEL_5 + " REAL NOT NULL DEFAULT 0)";

    public static final String SQL_DELETE_LEVEL =
            "DROP TABLE IF EXISTS " + Level.TABLE_NAME;


    /**
     * Strings referred to Settings Table
     */
    public static final String SQL_SETTINGS_CREATE =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY REFERENCES Player (user), " +
                    Settings.MUSIC + " INT NOT NULL DEFAULT 0, " +
                    Settings.SOUND + " INT NOT NULL DEFAULT 0, " +
                    Settings.TAN + " INT NOT NULL DEFAULT 0, " +
                    Settings.T_SHIRT + " INT NOT NULL DEFAULT 0, " +
                    Settings.GUN + " INT NOT NULL DEFAULT 0, " +
                    Settings.GORE + " INT NOT NULL DEFAULT 0)";

    public static final String SQL_SETTINGS_DROP =
            "DROP TABLE IF EXISTS " + Settings.TABLE_NAME;

}
