package com.example.note.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.note.config.Define;
import com.example.note.db.DatabaseManager;
import com.example.note.model.NoteItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by phund on 2/26/2016.
 */
public class NoteTable {
    private DatabaseManager mDatabaseManager;

    public NoteTable(Context context){
        mDatabaseManager = new DatabaseManager(context);
    }

    public void insert(HashMap<String, String> hashMap){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        ContentValues contentValues = new ContentValues();
        while (iterator.hasNext()){
            Map.Entry mentry = (Map.Entry) iterator.next();
            contentValues.put(mentry.getKey().toString(), mentry.getValue().toString());
        }
        db.insert(Define.TABLE_NAME, null, contentValues);
        db.close();

    }

    public void update(HashMap<String, String> hashMap, String id){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        Set set = hashMap.entrySet();
        Iterator iterator = set.iterator();
        ContentValues contentValues = new ContentValues();
        while (iterator.hasNext()){
            Map.Entry mentry = (Map.Entry) iterator.next();
            contentValues.put(mentry.getKey().toString(), mentry.getValue().toString());
        }
        db.update(Define.TABLE_NAME, contentValues, Define.COLUMN_ID + "=" + id, null);
        db.close();
    }

    public ArrayList<NoteItem> getAll(){
        // return array list alarm item
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        ArrayList<NoteItem> list = new ArrayList<NoteItem>();
        try {
            String query = "SELECT * FROM " + Define.TABLE_NAME + " ORDER BY "
                    + Define.COLUMN_CREATE_TIME + " DESC";
            Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst()){
                do {
                    NoteItem item = new NoteItem(
                            cursor.getInt(cursor.getColumnIndexOrThrow(Define.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_NOTE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_CREATE_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_ALARM_TIME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_COLOR))
                    );
                    list.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (android.database.SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }
}
