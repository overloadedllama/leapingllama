package com.overloadedllama.leapingllama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * some ideas: inserting data/time of user's best score,
 *
 * REMAP ALL WITH SWITCH-CASE
 *
 */

public class LlamaDbHandler {

    Context context;

    private final LlamaDbHelper dbHelper;

    // BASIC ELEMENTS
    public final String MONEY = "money";
    public final String MAX_LEVEL = "level";

    public LlamaDbHandler(Context context) {
        this.context = context;
        dbHelper = LlamaDbHelper.getInstance(context);

    }

    /**
     * inserts a new user, both in Player and Level tables
     * Firs checks that the user not exists yet, and in case the function creates it
     * @param user the user to create
     * @return true if the user didn't exists yet, false otherwise
     */
    public boolean insertNewUser(String user) {
        if (existsUser(user)) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newUser = new ContentValues();
        newUser.put(LlamaDbContracts.Player.PRIMARY_KEY, user);

        db.insert(LlamaDbContracts.Player.TABLE_NAME, null, newUser);
        db.insert(LlamaDbContracts.Level.TABLE_NAME, null, newUser);
        db.insert(LlamaDbContracts.Settings.TABLE_NAME, null, newUser);
        return true;
    }

    private boolean existsUser(String user) {
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

    public double getUserPlayerTableData(String user, String basic) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection;
        switch (basic) {
            case MONEY:
                projection = new String[] { LlamaDbContracts.Player.MONEY};
                break;
            case MAX_LEVEL:
                projection = new String[] { LlamaDbContracts.Player.MAX_LEVEL};
                break;
             default:
                projection = null;
                break;
        }

        if (projection == null) {
            throw new IllegalArgumentException("Basic column " + basic + " doesn't exists in Player Table.");
        }

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        cursor.moveToFirst();
        double userBasic = cursor.getDouble(0);
        cursor.close();
        return userBasic;
    }

    public double getLevelBestScore(String user, int level) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (level < 0 || level > 5)
            throw new IllegalArgumentException("Level " + level + " doesn't exists.");

        String LEVEL = "level" + level;
        String[] projection = { LEVEL };
        String selection = LlamaDbContracts.Level.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Level.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null, null
        );

        cursor.moveToFirst();
        double score = cursor.getDouble(0);
        cursor.close();
        return score;
    }

    // check and set (eventually) the new user's best score
    public boolean checkSetNewUserBestScore(String user, int level, double score) {
        double BestScore = getLevelBestScore(user, level);

        if (score > BestScore) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues newBest = new ContentValues();
            String LEVEL = "level" + level;
            newBest.put(LEVEL, score);

            String selection = LlamaDbContracts.Level.PRIMARY_KEY + " LIKE ?";
            String[] selectionArgs = { user };

            int count = db.update(
                    LlamaDbContracts.Level.TABLE_NAME,
                    newBest, selection, selectionArgs);

            if (count != 1) {
                throw new SQLException();
            }
            return true;
        }

        return false;
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

        int userMoney = (int) getUserPlayerTableData(user, MONEY);

        userMoney = userMoney + money;

        if (userMoney < 0) {
            // impossible completing the request, show an error message
            // this happens if (money < 0), e.g. when the user try to buy
            // an item but doesn't have enough money
            return false;
        } else {

            ContentValues value = new ContentValues();
            value.put(LlamaDbContracts.Player.MONEY, userMoney);

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

    // set the new user level, which is the previous increased by 1
    public void setUserLevel(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues nextLevel = new ContentValues();
        nextLevel.put(LlamaDbContracts.Player.MAX_LEVEL, getUserPlayerTableData(user, MAX_LEVEL) + 1);

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " LIKE ?";
        String[] selectionArgs = { user };

        db.update(
                LlamaDbContracts.Player.TABLE_NAME,
                nextLevel, selection, selectionArgs
        );
    }

    // resets all the progresses (money earned, levels, guns unlocked...) of the user
    public void resetProgresses(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues resetPlayer = new ContentValues();
        resetPlayer.put(LlamaDbContracts.Player.MAX_LEVEL, 0);
        resetPlayer.put(LlamaDbContracts.Player.MONEY, 0);

        ContentValues resetSetting = new ContentValues();
        resetSetting.put(LlamaDbContracts.Settings.TAN, 0);
        resetSetting.put(LlamaDbContracts.Settings.T_SHIRT, 0);
        resetSetting.put(LlamaDbContracts.Settings.GUN, 0);

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
