package com.iia.amasafeguard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iia.amasafeguard.entity.Data;
import com.iia.amasafeguard.entity.TypeData;

import java.util.ArrayList;

/**
 * Created by alexis on 19/01/2016.
 */
public class TypeDataSQLiteAdapter {

    public static final String TABLE_TYPEDATA = "type_data";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";

    private SQLiteDatabase db;
    private AmasafeguardSQLiteOpenHelper helper;

    /**
     * Helper Object to access db
     * @param context
     */
    public TypeDataSQLiteAdapter(Context context){
        helper = new AmasafeguardSQLiteOpenHelper(context, AmasafeguardSQLiteOpenHelper.DB_NAME, null, 1);
    }

    /**
     * Script SQL for Create Table TypeData
     * @return String
     */
    public static String getSchema(){
        return "CREATE TABLE " + TABLE_TYPEDATA + " ("
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
     * Insert TypeData in DB
     * @param typeData
     * @return line result
     */
    public long insert(TypeData typeData){
        return db.insert(TABLE_TYPEDATA, null, this.typeDataToContenValues(typeData));
    }

    /**
     * Update TypeData in DB
     * @param typeData
     * @return line result
     */
    public long update(TypeData typeData){
        ContentValues valuesUpdate = this.typeDataToContenValues(typeData);
        String whereClausesUpdate = COL_ID + "= ?";
        String[] whereArgsUpdate =  {String.valueOf(typeData.getId())};

        return db.update(TABLE_TYPEDATA, valuesUpdate, whereClausesUpdate, whereArgsUpdate);
    }

    /**
     * Select a TypeData with his Id.
     * @param id
     * @return Data
     */
    public TypeData getTypeData(long id){

        String[] cols = {COL_ID, COL_NAME};
        String whereClausesSelect = COL_ID + "= ?";
        String[] whereArgsSelect = {String.valueOf(id)};

        Cursor c = db.query(TABLE_TYPEDATA, cols, whereClausesSelect, whereArgsSelect, null, null, null);

        TypeData result = null;

        if (c.getCount() > 0){
            c.moveToFirst();
            result = this.cursorToItem(c);
        }

        return result;
    }

    /**
     * Get all Data
     * @return ArrayList<>
     */
    public ArrayList<TypeData> getAllTypeData(){
        ArrayList<TypeData> result = null;
        Cursor c = getAllCursor();

        if (c.moveToFirst()){
            result = new ArrayList<TypeData>();
            do {
                result.add(this.cursorToItem(c));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    /**
     * Delete TypeData with typedata object
     * @param typeData
     * @return long
     */
    public long delete(TypeData typeData){
        String whereClausesDelete = COL_ID + "= ?";
        String[] whereArgsDelete = {String.valueOf(typeData.getId())};

        return this.db.delete(TABLE_TYPEDATA, whereClausesDelete, whereArgsDelete);
    }

    /**
     * Convert TypeData to ContentValues
     * @param typeData
     * @return ContentValue
     */
    private ContentValues typeDataToContenValues(TypeData typeData){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, typeData.getName());

        return values;
    }

    /**
     * Cursor convert to TypeData
     * @param c
     * @return TypeData
     */
    public TypeData cursorToItem(Cursor c){
        TypeData result = new TypeData();
        result.setId(c.getLong(c.getColumnIndex(COL_ID)));
        result.setName(c.getString(c.getColumnIndex(COL_NAME)));

        return result;
    }

    /**
     * Get all Cursor in TypeData Table
     * @return Cursor
     */
    public Cursor getAllCursor(){
        String[] cols = {COL_ID, COL_NAME};
        Cursor c = db.query(TABLE_TYPEDATA, cols, null, null, null, null, null);
        return c;
    }
}
