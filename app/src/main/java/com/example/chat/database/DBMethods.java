package com.example.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBMethods {
    private SQLiteDatabase database;

    public DBMethods(Context context) {
        this.database = new UserBaseHelper(context).getWritableDatabase();
    }

    public void addUser(String phone, String name, String uuid, String token){
        ContentValues values= getContentValues(phone, name, uuid, token);
        database.insert(UserDBSchema.UserTable.NAME, null, values);
    }
    public void updateUser(String phone, String name, String uuidString, String token){
        ContentValues values = getContentValues(phone, name, uuidString, token);
        database.update(UserDBSchema.UserTable.NAME, values, UserDBSchema.Cols.UUID+"=?", new String[]{uuidString});
    }
    private UserCursorWrapper queryUsers(){
        Cursor cursor = database.query(UserDBSchema.UserTable.NAME, null, null, null, null, null, null);
        return new UserCursorWrapper(cursor);
    }

    public String getToken(){
        UserCursorWrapper cursorWrapper = queryUsers();
        cursorWrapper.moveToFirst();
        String token = null;
        try {
            token = cursorWrapper.getToken();
        }finally {
            cursorWrapper.close();
        }
        return token;
    }
    private ContentValues getContentValues(String phone, String name, String uuid, String token){
        ContentValues values = new ContentValues();
        values.put(UserDBSchema.Cols.UUID, uuid);
        values.put(UserDBSchema.Cols.USERNAME, name);
        values.put(UserDBSchema.Cols.TOKEN, token);
        values.put(UserDBSchema.Cols.PHONE, phone);
        return values;
    }
}