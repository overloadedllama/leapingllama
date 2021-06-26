package com.overloadedllama.leapingllama.database;

import android.provider.BaseColumns;


/**
 * Table Player:
 *      user VARCHAR(20) PK,
 *      score INT DEFAULT 0,
 *      money INT DEFAULT 0
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

    }

    /**
     * Next three String variables are used in onCreate method in
     * LlamaDbHelper
     */
    public static final String SQL_PLAYER_CREATE =
            "CREATE TABLE " + Player.TABLE_NAME + " (" +
                    Player.PRIMARY_KEY + " VARCHAR(20) PRIMARY KEY, " +
                    Player.SCORE_COLUMN + " INT DEFAULT 0, " +
                    Player.MONEY_COLUMN + " INT DEFAULT 0)";

    public static final String SQL_PLAYER_DROP =
            "DROP TABLE " + Player.TABLE_NAME + " IF EXISTS";

}
