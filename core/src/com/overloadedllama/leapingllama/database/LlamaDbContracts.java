package com.overloadedllama.leapingllama.database;

import android.provider.BaseColumns;

/**
 * Table Player:
 *      user VARCHAR(20) PK,
 *      money INT NOT NULL DEFAULT 0,
 *      levelReached INT NOT NULL DEFAULT 0
 *
 * Table Level
 *      user VARCHAR(20) REFERENCES Player,
 *      level INT NOT NULL,
 *      starNum INT NOT NULL DEFAULT 0,
 *      score REAL NOT NULL DEFAULT 0,
 *      PRIMARY KEY (user, level)
 *
 * Are music/sounds really necessaries?
 *
 * Table Settings:
 *      user VARCHAR(20) PK REFERENCES Player
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

    public static class Levels implements BaseColumns {
        public static final String TABLE_NAME = "levels";
        public static final String USER = "user";
        public static final String LEVEL = "level";
        public static final String STAR_NUM = "starNum";
        public static final String SCORE = "score";
        public static final String PRIMARY_KEY = USER + ", " + LEVEL;

    }

    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String PRIMARY_KEY = "user";
        public static final String TAN = "tan";
        public static final String T_SHIRT = "t_shirt";
        public static final String GUN = "gun";
        public static final String GORE = "gore";

    }

    /**
     * Strings referred to Player Table
     */
    public static final String SQL_PLAYER_CREATE =
            "CREATE TABLE " + Player.TABLE_NAME + " (" +
                    Player.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY, " +
                    Player.MONEY + " INT NOT NULL DEFAULT 0, " +
                    Player.MAX_LEVEL + " INT NOT NULL DEFAULT 0)";


    public static final String SQL_PLAYER_DROP =
            "DROP TABLE IF EXISTS " + Player.TABLE_NAME;


    /**
     * SQL Strings referred to Levels Table
     */
    public static final String SQL_LEVELS_CREATE =
            "CREATE TABLE " + Levels.TABLE_NAME + " (" +
                    Levels.USER + " VARCHAR(20) REFERENCES Player, " +
                    Levels.LEVEL + " INT NOT NULL, " +
                    Levels.SCORE + " INT NOT NULL DEFAULT 0, " +
                    Levels.STAR_NUM + " INT NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (" + Levels.PRIMARY_KEY + "))";

    public static final String SQL_LEVELS_DROP =
            "DROP TABLE IF EXISTS " + Levels.TABLE_NAME;



    /**
     * Strings referred to Settings Table
     */
    public static final String SQL_SETTINGS_CREATE =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY REFERENCES Player (user), " +
                    Settings.TAN + " INT NOT NULL DEFAULT 0, " +
                    Settings.T_SHIRT + " INT NOT NULL DEFAULT 0, " +
                    Settings.GUN + " INT NOT NULL DEFAULT 0, " +
                    Settings.GORE + " INT NOT NULL DEFAULT 0)";

    public static final String SQL_SETTINGS_DROP =
            "DROP TABLE IF EXISTS " + Settings.TABLE_NAME;




    // old
    public static final String SQL_LEVEL_DROP =
            "DROP TABLE IF EXISTS " + "level";


}
