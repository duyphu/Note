package com.example.note.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.note.config.Define;

/**
 * Created by phund on 2/26/2016.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(Context context){
        super(context, Define.DATABASE_NAME, null, Define.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Define.SQL_CREATE_TABLE_NOTES);
        }catch (SQLException se){
            se.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Define.SQL_DELETE_TABLE_NOTES);
        onCreate(db);
    }
}