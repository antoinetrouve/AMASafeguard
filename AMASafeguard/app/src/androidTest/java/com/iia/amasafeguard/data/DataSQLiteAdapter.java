package com.iia.amasafeguard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iia.amasafeguard.entity.Data;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by alexis on 19/01/2016.
 */
public class DataSQLiteAdapter {

    public static final String TABLE_DATA = "data";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PATH = "path";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    private SQLiteDatabase db;
    private AmasafeguardSQLiteOpenHelper helper;

    /**
     * Helper Object to access db
     * @param context
     */
    public DataSQLiteAdapter(Context context){
        helper = new AmasafeguardSQLiteOpenHelper(context, AmasafeguardSQLiteOpenHelper.DB_NAME, null, 1);
    }

    /**
     * Script SQL for Create Table Data
     * @return String
     */
    public static String getSchema(){
        return "CREATE TABLE " + TABLE_DATA + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PATH + " TEXT NOT NULL, "
                + COL_CREATED_AT + " INTEGER NOT NULL, "
                + COL_UPDATED_AT + " INTERGER NOT NULL);";
    }

    public void open(){
        this.db = this.helper.getWritableDatabase();
    }

    public void close(){
        this.db.close();
    }

    /**
     * Insert Data in DB
     * @param data
     * @return line result
     */
    public long insert(Data data){
        return db.insert(TABLE_DATA, null, this.dataToContenValues(data));
    }

    /**
     * Update Data in DB
     * @param data
     * @return line result
     */
    public long update(Data data){
        ContentValues valuesUpdate = this.dataToContenValues(data);
        String whereClausesUpdate = COL_ID + "= ?";
        String[] whereArgsUpdate =  {String.valueOf(data.getId())};

        return db.update(TABLE_DATA, valuesUpdate, whereClausesUpdate, whereArgsUpdate);
    }

    /**
     * Select a Data with his Id.
     * @param id
     * @return Data
     */
    public Data getData(long id){

        String[] cols = {COL_ID, COL_NAME, COL_PATH, COL_CREATED_AT, COL_UPDATED_AT};
        String whereClausesSelect = COL_ID + "= ?";
        String[] whereArgsSelect = {String.valueOf(id)};

        Cursor c = db.query(TABLE_DATA, cols, whereClausesSelect, whereArgsSelect, null, null, null);

        Data result = null;

        if (c.getCount() > 0){
            c.moveToFirst();
            result = cursorToItem(c);
        }

        return result;
    }

    /**
     * Get all Data
     * @return ArrayList<>
     */
    public ArrayList<Data> getAllData(){
        ArrayList<Data> result = null;
        Cursor c = getAllCursor();

        if (c.moveToFirst()){
            result = new ArrayList<Data>();
            do {
                result.add(this.cursorToItem(c));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    /**
     * Delete Data with data object
     * @param data
     * @return long
     */
    public long delete(Data data){
        String whereClausesDelete = COL_ID + "= ?";
        String[] whereArgsDelete = {String.valueOf(data.getId())};

        return this.db.delete(TABLE_DATA, whereClausesDelete, whereArgsDelete);
    }

    /**
     * Convert data to ContentValues
     * @param data
     * @return ContentValue
     */
    private ContentValues dataToContenValues(Data data){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, data.getName());
        values.put(COL_PATH, data.getPath());
        values.put(COL_CREATED_AT, data.getCreated_at().toString());
        values.put(COL_UPDATED_AT, data.getUpdated_at().toString());

        return values;
    }

    /**
     * Cursor convert to Data
     * @param c
     * @return Data
     */
    public Data cursorToItem(Cursor c){
        Data result = new Data();
        result.setId(c.getLong(c.getColumnIndex(COL_ID)));
        result.setName(c.getString(c.getColumnIndex(COL_NAME)));
        result.setPath(c.getString(c.getColumnIndex(COL_NAME)));
        result.setCreated_at(c.getInt(c.getColumnIndex(COL_CREATED_AT)));
        result.setUpdated_at(c.getInt(c.getColumnIndex(COL_UPDATED_AT)));

        return result;
    }

    /**
     * Get all Cursor in Data Table
     * @return Cursor
     */
    public Cursor getAllCursor(){
        String[] cols = {COL_ID, COL_NAME, COL_PATH, COL_CREATED_AT, COL_UPDATED_AT};
        Cursor c = db.query(TABLE_DATA, cols, null, null, null, null, null);
        return c;
    }

}