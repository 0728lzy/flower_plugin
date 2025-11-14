package com.cslianta.catdog.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DbManager {

    private Context mContext;
    private SQLiteDatabase mDb;
    private static String dbPath = "game.db";
    private static DbManager instancef = null;

    public static DbManager getInstance() {
        if (instancef == null) {
            instancef = new DbManager();
        }
        return instancef;
    }

    private void openDb() {
        mDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    private void query() {

    }

}
