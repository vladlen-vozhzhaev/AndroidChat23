package com.example.chat.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public String getToken(){
        String token;
        try {
           token = getString(getColumnIndex(UserDBSchema.Cols.TOKEN));
        }catch (Exception e){
            token = null;
        }

        return token;
    }
}
