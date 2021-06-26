package com.overloadedllama.leapingllama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class LlamaDbHandler {

    private static int userBestScore;
    private static int userMoney;

    Context context;

    private final LlamaDbHelper dbHelper;

    public LlamaDbHandler(Context context) {
        dbHelper = new LlamaDbHelper(context);
    }

    // insert a new user
    public void insertNewUser(String user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newUser = new ContentValues();
        newUser.put(LlamaDbContracts.Player.PRIMARY_KEY, user);

        db.insert(LlamaDbContracts.Player.TABLE_NAME, null, newUser);
    }

    // gets the user's money
    public int getUserMoney(String user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LlamaDbContracts.Player.MONEY_COLUMN
        };

        String selection = LlamaDbContracts.Player.PRIMARY_KEY + " = test";
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

        cursor.moveToPosition(1);
        userMoney = cursor.getInt(1);
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
        userBestScore = cursor.getInt(1);
        cursor.close();
        return userBestScore;
    }


}
