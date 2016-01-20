package com.iia.amasafeguard.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexis on 19/01/2016.
 */
public class AmasafeguardSQLiteOpenHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "amasafeguard.sqlite";

    public AmasafeguardSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AmasafeguardSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataSQLiteAdapter.getSchema());
        db.execSQL(ExtensionSQLiteAdapter.getSchema());
        db.execSQL(TypeDataSQLiteAdapter.getSchema());
        db.execSQL(UserSQLiteAdapter.getSchema());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}