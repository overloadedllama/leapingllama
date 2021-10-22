package com.overloadedllama.leapingllama.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class LlamaDbHandler {
    private static LlamaDbHandler uniqueInstance;

    private final LlamaDbHelper dbHelper;

    public final String MONEY = "money";
    public final String MAX_LEVEL = "level";


    private LlamaDbHandler(Context context) {
        dbHelper = LlamaDbHelper.getInstance(context);

    }

    public static LlamaDbHandler getUniqueInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new LlamaDbHandler(context);
        }
        return uniqueInstance;
    }

    // PLAYER OPERATIONS

    /**
     * insert a new user, with relative entries in Player, Setting and Levels tables
     * Firs check that the user does not exists yet
     * @param user the user to create
     */
    public void insertNewUser(String user) {
        if (existsUser(user)) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newUser = new ContentValues();
        newUser.put(LlamaDbContracts.Player.PRIMARY_KEY, user);

        db.insert(LlamaDbContracts.Player.TABLE_NAME, null, newUser);
        db.insert(LlamaDbContracts.Settings.TABLE_NAME, null, newUser);

        for (int i = -1; i < 6; ++i) {
            ContentValues level = new ContentValues();
            level.put(LlamaDbContracts.Levels.USER, user);
            level.put(LlamaDbContracts.Levels.LEVEL, i);

            db.insert(LlamaDbContracts.Levels.TABLE_NAME, null, level);
        }
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
        if (ret)
            System.out.println("User " + user + " exists.");
        return ret;
    }

    private double getUserPlayerTableData(String user, String basic) {
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

    public int getUserMaxLevel(String user) {
        return (int) getUserPlayerTableData(user, MAX_LEVEL);
    }

    public int getUserMoney(String user) {
        return (int) getUserPlayerTableData(user, MONEY);
    }

    public String[] getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = { LlamaDbContracts.Player.PRIMARY_KEY };

        Cursor cursor = db.query(
                LlamaDbContracts.Player.TABLE_NAME,
                projection, null, null,
                null, null, null
        );

        int n = cursor.getCount();
        System.out.println("Total users: " + n);
        String[] users = new String[n];

        for (int i = 0; i < n; ++i) {
            cursor.moveToNext();
            users[i] = cursor.getString(0);
            System.out.println("user " + i + ": " + users[i]);
        }
        cursor.close();
        return users;
    }

    // LEVEL OPERATIONS

    public int getLevelStarNum(String user, int level) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (level < -1 || level > 5)
            throw new IllegalArgumentException("Level " + level + " doesn't exists.");

        String query =
                        "SELECT " + LlamaDbContracts.Levels.STAR_NUM +
                        " FROM " + LlamaDbContracts.Levels.TABLE_NAME +
                        " WHERE " + LlamaDbContracts.Levels.USER + " = ? " +
                        " AND " + LlamaDbContracts.Levels.LEVEL + " = ?";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{user, String.valueOf(level)}
        );

        cursor.moveToFirst();
        int starNum = cursor.getInt(0);
        cursor.close();
        return starNum;
    }

    public void setLevelStarNum(String user, int level, int starNum) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues newStar = new ContentValues();
        newStar.put(LlamaDbContracts.Levels.STAR_NUM, starNum);

        String selection =
                LlamaDbContracts.Levels.USER + " LIKE ? AND " + LlamaDbContracts.Levels.LEVEL + " = ?";
        String[] selectionArgs =
                { user, String.valueOf(level) };

        int count = db.update(
                LlamaDbContracts.Levels.TABLE_NAME,
                newStar, selection, selectionArgs
        );

        if (count != 1) {
            throw new SQLiteException("Update Star Num failed");
        }

    }

    public double getLevelBestScore(String user, int level) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (level < -1 || level > 5)
            throw new IllegalArgumentException("Level " + level + " doesn't exists.");

        String query =
                "SELECT " + LlamaDbContracts.Levels.SCORE +
                        " FROM " + LlamaDbContracts.Levels.TABLE_NAME +
                        " WHERE " + LlamaDbContracts.Levels.USER + " = ? " +
                        " AND " + LlamaDbContracts.Levels.LEVEL + " = ?";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{user, String.valueOf(level)}
        );

        cursor.moveToFirst();
        double score = cursor.getDouble(0);
        cursor.close();
        return score;
    }

    /**
     * Check if the the last game score of a certain level is better than the current best score,
     * in that case update it.
     *
     * @param user the player who made the score
     * @param level the level relative of the last game
     * @param score the last game score
     * @return true if the last level score is grater than the best score of the same level, false otherwise
     */
    public boolean checkSetNewUserBestScore(String user, int level, double score) {
        if (level < -1 || level > 5)
            throw new IllegalArgumentException("Level " + level + " doesn't exist");

        double BestScore = getLevelBestScore(user, level);

        if (score > BestScore) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues newBest = new ContentValues();
            newBest.put(LlamaDbContracts.Levels.SCORE, score);

            String selection = LlamaDbContracts.Levels.USER + " LIKE ? AND " + LlamaDbContracts.Levels.LEVEL + " = ?";
            String[] selectionArgs = { user, String.valueOf(level) };

            int count = db.update(
                    LlamaDbContracts.Levels.TABLE_NAME,
                    newBest, selection, selectionArgs);

            if (count != 1) {
                throw new SQLException("Affected " + count + " rows instead of 1.");
            }
            return true;
        }

        return false;
    }

    /**
     * Update the user money
     *
     * @param user owner of the money
     * @param money amount of money used to update db, positive or negative
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
                throw new SQLException("Affected " + count + " rows instead of 1.");
            }

            return true;
        }
    }

    /**
     * Set the max level the player can play
     */
    public void updateUserMaxLevel(String user) {
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

}
