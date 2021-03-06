package com.example.note.db.table;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.note.config.Define;
import com.example.note.db.DatabaseManager;
import com.example.note.model.NoteItem;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void insert(NoteItem item){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Define.COLUMN_ALARM_TIME, item.getAlarmTime());
        contentValues.put(Define.COLUMN_CREATE_TIME, item.getCreateTime());
        contentValues.put(Define.COLUMN_COLOR, item.getColor());
        contentValues.put(Define.COLUMN_PICTURES, item.getPictures()
                .toString().replaceAll("\\s+|\\[|\\]", ""));
        contentValues.put(Define.COLUMN_NOTE, item.getNote());
        contentValues.put(Define.COLUMN_TITLE, item.getTitle());
        db.insert(Define.TABLE_NAME, null, contentValues);
        db.close();

    }

    public void update(NoteItem item){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Define.COLUMN_ALARM_TIME, item.getAlarmTime());
        contentValues.put(Define.COLUMN_CREATE_TIME, item.getCreateTime());
        contentValues.put(Define.COLUMN_COLOR, item.getColor());
        contentValues.put(Define.COLUMN_PICTURES, item.getPictures()
                .toString().replaceAll("\\s+|\\[|\\]", ""));
        contentValues.put(Define.COLUMN_NOTE, item.getNote());
        contentValues.put(Define.COLUMN_TITLE, item.getTitle());
        db.update(Define.TABLE_NAME, contentValues, Define.COLUMN_ID + "=" + item.getId(), null);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = mDatabaseManager.getWritableDatabase();
        db.delete(Define.TABLE_NAME, Define.COLUMN_ID + "=" + id, null);
    }

    public String getOneColumn(String column, int id){
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        String value = "";
        String query = "SELECT "+column+" FROM "+Define.TABLE_NAME+" WHERE "
                +Define.COLUMN_ID+" = "+id;
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(column));
            }
        }catch (NullPointerException npe){
            npe.printStackTrace();
        } finally {
            db.close();
        }
        return value;
    }

    public int getLastId(){
        int lastId = 0;
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        String query = "SELECT "+Define.COLUMN_ID+" FROM "+Define.TABLE_NAME
                +" ORDER BY "+Define.COLUMN_ID+" DESC LIMIT 1";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                lastId = cursor.getInt(cursor.getColumnIndexOrThrow(Define.COLUMN_ID));
            }
            cursor.close();
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
        return lastId;
    }
    public NoteItem getOne(int id){
        SQLiteDatabase db = mDatabaseManager.getReadableDatabase();
        NoteItem noteItem = new NoteItem();
        String query  = "SELECT * FROM "+Define.TABLE_NAME+" WHERE "
                +Define.COLUMN_ID+" = "+id;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                noteItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Define.COLUMN_ID)));
                noteItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_TITLE)));
                noteItem.setCreateTime(
                        cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_CREATE_TIME)));
                noteItem.setNote(cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_NOTE)));
                noteItem.setAlarmTime(
                        cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_ALARM_TIME)));
                noteItem.setColor(cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_COLOR)));
                String pictures =
                        cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_PICTURES));
                if (!pictures.equals("")){
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(pictures.split(",")));
                    noteItem.setPictures(list);
                }
            }
        } catch(NullPointerException npe){
            npe.printStackTrace();
        } finally {
            db.close();
        }
        return noteItem;
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
                            cursor.getString(cursor.getColumnIndexOrThrow(Define.COLUMN_COLOR)),
                            null
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
