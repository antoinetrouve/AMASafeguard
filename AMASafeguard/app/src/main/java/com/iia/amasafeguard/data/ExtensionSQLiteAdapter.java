package com.iia.amasafeguard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iia.amasafeguard.entity.Extension;

import java.util.ArrayList;

/**
 * Created by alexis on 20/01/2016.
 */
public class ExtensionSQLiteAdapter {

    public static final String TABLE_EXTENSION = "extension";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";

    private SQLiteDatabase db;
    private AmasafeguardSQLiteOpenHelper helper;

    /**
     * Helper Object to access db
     * @param context
     */
    public ExtensionSQLiteAdapter(Context context){
        helper = new AmasafeguardSQLiteOpenHelper(context, AmasafeguardSQLiteOpenHelper.DB_NAME, null, 1);
    }

    /**
     * Script SQL for Create Table Extension
     * @return String
     */
    public static String getSchema(){
        return "CREATE TABLE " + TABLE_EXTENSION + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL;";
    }

    public void open(){
        this.db = this.helper.getWritableDatabase();
    }

    public void close(){
        this.db.close();
    }

    /**
     * Insert Extension in DB
     * @param extension
     * @return line result
     */
    public long insert(Extension extension){
        return db.insert(TABLE_EXTENSION, null, this.typeDataToContentValues(extension));
    }

    /**
     * Update Extension in DB
     * @param typeData
     * @return line result
     */
    public long update(Extension typeData){
        ContentValues valuesUpdate = this.typeDataToContentValues(typeData);
        String whereClausesUpdate = COL_ID + "= ?";
        String[] whereArgsUpdate =  {String.valueOf(typeData.getId())};

        return db.update(TABLE_EXTENSION, valuesUpdate, whereClausesUpdate, whereArgsUpdate);
    }

    /**
     * Select a Extension with his Id.
     * @param id
     * @return Extension
     */
    public Extension getExtension(long id){

        String[] cols = {COL_ID, COL_NAME};
        String whereClausesSelect = COL_ID + "= ?";
        String[] whereArgsSelect = {String.valueOf(id)};

        Cursor c = db.query(TABLE_EXTENSION, cols, whereClausesSelect, whereArgsSelect, null, null, null);

        Extension result = null;

        if (c.getCount() > 0){
            c.moveToFirst();
            result = this.cursorToItem(c);
        }
        return result;
    }

    /**
     * Get all Extension
     * @return ArrayList<>
     */
    public ArrayList<Extension> getAllExtension(){
        ArrayList<Extension> result = null;
        Cursor c = getAllCursor();

        if (c.moveToFirst()){
            result = new ArrayList<Extension>();
            do {
                result.add(this.cursorToItem(c));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    /**
     * Delete Extension with extension object
     * @param extension
     * @return long
     */
    public long delete(Extension extension){
        String whereClausesDelete = COL_ID + "= ?";
        String[] whereArgsDelete = {String.valueOf(extension.getId())};

        return this.db.delete(TABLE_EXTENSION, whereClausesDelete, whereArgsDelete);
    }

    /**
     * Convert Extension to ContentValues
     * @param extension
     * @return ContentValue
     */
    private ContentValues typeDataToContentValues(Extension extension){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, extension.getName());

        return values;
    }

    /**
     * Cursor convert to Extension
     * @param c
     * @return Extension
     */
    public Extension cursorToItem(Cursor c){
        Extension result = new Extension();
        result.setId(c.getLong(c.getColumnIndex(COL_ID)));
        result.setName(c.getString(c.getColumnIndex(COL_NAME)));

        return result;
    }

    /**
     * Get all Cursor in Extension Table
     * @return Cursor
     */
    public Cursor getAllCursor(){
        String[] cols = {COL_ID, COL_NAME};
        Cursor c = db.query(TABLE_EXTENSION, cols, null, null, null, null, null);
        return c;
    }
}
