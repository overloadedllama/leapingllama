package com.overloadedllama.leapingllama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

/**
 * some ideas: inserting data/time of user's best score,
 * add the possibility to delete user's progresses,
 */

public class LlamaDbHandler {

    private static int userBestScore;
    private static int userMoney;

    Context context;

    private final LlamaDbHelper dbHelper;

    public LlamaDbHandler(Context context) {
        this.context = context;
        dbHelper = LlamaDbHelper.getInstance(context);

    }

    // inserts a new user
    public void insertNewUser(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newUser = new ContentValues();
        newUser.put(LlamaDbContracts.Player.PRIMARY_KEY, user);

        long ret = 0;
        try {
            ret = db.insert(LlamaDbContracts.Player.TABLE_NAME, null, newUser);
        } catch (SQLiteConstraintException unique) {
            System.out.println("ERROR: user 'test' already exists.");
        } finally {
            System.out.println("ret db.insert() = " + ret);
        }
    }

    public boolean existsUser(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { LlamaDbContracts.Player.PRIMARY_KEY };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        // if exists an user called "user" then the cursor is not empty,
        // so method moveToFirst() will return true, else false
        boolean ret = cursor.moveToFirst();
        cursor.close();
        return ret;
    }

    // insert settings referred to an existing user
    public void insertSettingsNewUser(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newUser = new ContentValues();
        newUser.put(LlamaDbContracts.Settings.PRIMARY_KEY, user);

        long ret = 0;
        try {
            ret = db.insert(LlamaDbContracts.Settings.TABLE_NAME, null, newUser);
        } catch (SQLiteConstraintException unique) {
            System.out.println("ERROR: user 'test' already exists.");
        } finally {
            System.out.println("ret db.insert() = " + ret);
        }
    }

    // gets the user's money
    public int getUserMoney(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { LlamaDbContracts.Player.MONEY_COLUMN };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, selection, selectionArgs,
                null,null, null
        );

        cursor.moveToFirst();
        userMoney = cursor.getInt(0);
        cursor.close();
        return userMoney;
    }

    // gets the user's best score
    public int getUserBestScore(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LlamaDbContracts.Player.SCORE_COLUMN
        };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        cursor.moveToFirst();
        userBestScore = cursor.getInt(0);
        cursor.close();
        return userBestScore;
    }

    // check and set (eventually) the new user's best score
    public void checkSetNewUserBestScore(String user, int score) {
        int BestScore = getUserBestScore(user);

        if (BestScore < score) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues newBest = new ContentValues();
            newBest.put(LlamaDbContracts.Player.SCORE_COLUMN, score);

            String selection = LlamaDbContracts.Player.PRIMARY_KEY + " LIKE ?";
            String[] selectionArgs = { user };

            int count = db.update(
                    LlamaDbContracts.Player.TABLE_NAME,
                    newBest, selection, selectionArgs);

            if (count != 1) {
                throw new SQLException();
            }

        }
    }

    /**
     *
     * @param user owner of the money
     * @param money amount of money used to update db
     * @return true if 'money' is positive or, if is negative, the current userMoney
     *          is greater than it
     */
    public boolean checkSetUserMoney(String user, int money) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        userMoney = getUserMoney(user);

        userMoney = userMoney + money;

        if (userMoney < 0) {
            // impossible completing the request, show an error message
            // this happens if (money < 0), e.g. when the user try to buy
            // an item but doesn't have enough money
            return false;
        } else {

            ContentValues value = new ContentValues();
            value.put(LlamaDbContracts.Player.MONEY_COLUMN, userMoney);

            String selection = LlamaDbContracts.Player.PRIMARY_KEY + " LIKE ?";
            String[] selectionArgs = { user };

            int count = db.update(
                    LlamaDbContracts.Player.TABLE_NAME,
                    value,
                    selection,
                    selectionArgs);

            if (count != 1) {
                throw new SQLException();
            }

            return true;
        }
    }

    public int getUserLevel(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LlamaDbContracts.Player.LEVEL_COLUMN
        };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, selection, selectionArgs,
                null,null, null
        );

        cursor.moveToFirst();
        int levelUser = cursor.getInt(0);
        cursor.close();
        return levelUser;
    }

    // set the new user level, which is the previous increased by 1
    public void setUserLevel(String user, int level) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues nextLevel = new ContentValues();
        nextLevel.put(LlamaDbContracts.Player.LEVEL_COLUMN, getUserLevel(user) + 1);

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " LIKE ?";
        String[] selectionArgs = { user };

        db.update(
                LlamaDbContracts.Player.TABLE_NAME,
                nextLevel, selection, selectionArgs
        );
    }

    public boolean isMusicOn(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { LlamaDbContracts.Settings.MUSIC_COLUMN };

        String selection = LlamaDbContracts.Settings.PRIMARY_KEY + " =? ";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Settings.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        cursor.moveToFirst();
        boolean ret = cursor.getInt(0) == 1;
        cursor.close();
        return ret;
    }

    public void setMusic(String user, boolean isOn) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int music;
        if (isOn) music = 1;
        else music = 0;

        ContentValues value = new ContentValues();
        value.put(LlamaDbContracts.Settings.MUSIC_COLUMN, music);

        String selection = LlamaDbContracts.Settings.PRIMARY_KEY + " LIKE ?";
        String[] selectionArgs = { user };

        db.update(
                LlamaDbContracts.Settings.TABLE_NAME,
                value, selection, selectionArgs
        );
    }

    // resets all the progresses (money earned, levels, guns unlocked...) of the user
    public void resetProgresses(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues resetPlayer = new ContentValues();
        resetPlayer.put(LlamaDbContracts.Player.SCORE_COLUMN, 0);
        resetPlayer.put(LlamaDbContracts.Player.LEVEL_COLUMN, 0);
        resetPlayer.put(LlamaDbContracts.Player.MONEY_COLUMN, 0);

        ContentValues resetSetting = new ContentValues();
        resetSetting.put(LlamaDbContracts.Settings.TAN_COLUMN, 0);
        resetSetting.put(LlamaDbContracts.Settings.T_SHIRT_COLUMN, 0);
        resetSetting.put(LlamaDbContracts.Settings.GUN_COLUMN, 0);

        String selPlayer = LlamaDbContracts.Player.PRIMARY_KEY + " LIKE ?";
        String[] selPlayerArgs = { user };

        String selSettings= LlamaDbContracts.Settings.PRIMARY_KEY + " LIKE ?";
        String[] selSettingsArgs = { user };

        db.update(
                LlamaDbContracts.Player.TABLE_NAME,
                resetPlayer, selPlayer, selPlayerArgs
        );

        db.update(
                LlamaDbContracts.Settings.TABLE_NAME,
                resetSetting, selSettings, selSettingsArgs
        );
    }

}
