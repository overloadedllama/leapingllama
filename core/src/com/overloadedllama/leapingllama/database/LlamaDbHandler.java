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
 * create tables/columns for each game level
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

    // gets the user's money
    public int getUserMoney(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LlamaDbContracts.Player.MONEY_COLUMN
        };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = ?";
        String[] selectionArgs = { user };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        System.out.println(cursor.toString());

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
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
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
                    newBest,
                    selection,
                    selectionArgs);

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

}
