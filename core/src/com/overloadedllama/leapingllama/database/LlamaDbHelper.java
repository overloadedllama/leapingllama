package com.overloadedllama.leapingllama.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LlamaDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Llama.db";


    private static LlamaDbHelper sInstance;

    // to instantiate a new LlamaDbHelper must be used getInstance() method,
    // due to this the basic constructor is private
    private LlamaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LlamaDbContracts.SQL_PLAYER_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // this method prevents having multiple LlamaDbHelper
    public static synchronized LlamaDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LlamaDbHelper(context.getApplicationContext());
        }

        return sInstance;
    }


}
