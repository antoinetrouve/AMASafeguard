package com.iia.amasafeguard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iia.amasafeguard.entity.User;

import java.util.ArrayList;

/**
 * Created by antoi on 19/01/2016.
 */
public class UserSQLiteAdapter {

    protected static final String TABLE_USER = "user";
    protected static final String COL_ID = "id";
    protected static final String COL_LOGIN = "login";
    protected static final String COL_MDP = "mdp";
    protected static final String COL_UUID = "uuid";
    protected static final String COL_ISCONNECTED = "isconnected";
    protected static final String COL_CREATED_AT = "created_at";

    private SQLiteDatabase db;
    private AmasafeguardSQLiteOpenHelper helper;

    /**
     * Helper object to create access db
     * @param context
     */
    public UserSQLiteAdapter(Context context){
        helper = new AmasafeguardSQLiteOpenHelper(context,AmasafeguardSQLiteOpenHelper.DB_NAME,null,1);
    }

    /**
     * Create database
     * @return String
     */
    public static String getSchema(){
        return "CREATE TABLE " + TABLE_USER + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_LOGIN + " TEXT NOT NULL, "
                + COL_MDP + " TEXT NOT NULL, "
                + COL_ISCONNECTED+ " INTEGER NOT NULL, "
                + COL_UUID+ " INTEGER NOT NULL, "
                + COL_CREATED_AT + " INTEGER NOT NULL);";
    }

    public void open(){
        this.db = this.helper.getWritableDatabase();
    }

    public void close(){
        this.db.close();
    }

    /**
     * Insert user into DB
     * @param user
     * @return line result
     */
    public long insert(User user){
        return db.insert(TABLE_USER, null, this.userToContentValues(user));
    }

    /**
     * Delete User with user object
     * @param user
     * @return line result
     */
    public long delete(User user){
        String whereClausesDelete = COL_ID + "= ?";
        String[] whereArgsDelete = {String.valueOf(user.getId())};

        return this.db.delete(TABLE_USER, whereClausesDelete, whereArgsDelete);
    }

    /**
     * Update User in DB
     * @param user
     * @return line result
     */
    public long update(User user){
        ContentValues valuesUpdate = this.userToContentValues(user);
        String whereClausesUpdate = COL_ID + "= ?";
        String[] whereArgsUpdate =  {String.valueOf(user.getId())};

        return db.update(TABLE_USER, valuesUpdate, whereClausesUpdate, whereArgsUpdate);
    }

    /**
     * Select a User with his Id.
     * @param id
     * @return User
     */
    public User getData(long id){

        String[] cols = {COL_ID, COL_LOGIN, COL_MDP, COL_UUID, COL_ISCONNECTED, COL_CREATED_AT};
        String whereClausesSelect = COL_ID + "= ?";
        String[] whereArgsSelect = {String.valueOf(id)};

        // create SQL request
        Cursor cursor = db.query(TABLE_USER, cols, whereClausesSelect, whereArgsSelect, null, null, null);

        User result = null;

        // if SQL request return a result
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursorToItem(cursor);
        }

        return result;
    }

    /**
     * Get all User
     * @return ArrayList<>
     */
    public ArrayList<User> getAllUser(){
        ArrayList<User> result = null;
        Cursor cursor = getAllCursor();

        // if cursor contains result
        if (cursor.moveToFirst()){
            result = new ArrayList<User>();
            // add user into list
            do {
                result.add(this.cursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    /**
     * Convert data to ContentValues
     * @param user
     * @return ContentValue
     */
    private ContentValues userToContentValues(User user){
        ContentValues values = new ContentValues();
        values.put(COL_LOGIN, user.getLogin());
        values.put(COL_MDP, user.getMdp());
        values.put(COL_UUID, user.getUuid());
        values.put(COL_ISCONNECTED, user.getIs_connected());
        values.put(COL_CREATED_AT, user.getCreated_at());

        return values;
    }

    /**
     * Cursor convert to User
     * @param cursor
     * @return User
     */
    public User cursorToItem(Cursor cursor){
        User result = new User();
        result.setId(cursor.getLong(cursor.getColumnIndex(COL_ID)));
        result.setLogin(cursor.getString(cursor.getColumnIndex(COL_LOGIN)));
        result.setMdp(cursor.getString(cursor.getColumnIndex(COL_MDP)));
        result.setUuid(cursor.getString(cursor.getColumnIndex(COL_UUID)));
        result.setIs_connected(cursor.getInt(cursor.getColumnIndex(COL_ISCONNECTED)));
        result.setCreated_at(cursor.getInt(cursor.getColumnIndex(COL_CREATED_AT)));

        return result;
    }

    /**
     * Get all Cursor in User Table
     * @return Cursor
     */
    public Cursor getAllCursor(){
        String[] cols = {COL_ID, COL_LOGIN, COL_MDP, COL_UUID, COL_ISCONNECTED, COL_CREATED_AT};
        Cursor cursor = db.query(TABLE_USER, cols, null, null, null, null, null);
        return cursor;
    }
}
