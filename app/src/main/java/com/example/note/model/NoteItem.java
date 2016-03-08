package com.example.note.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.note.activity.MainActivity;
import com.example.note.config.Define;
import com.example.note.db.table.NoteTable;
import com.example.note.receiver.AlarmReceiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by phund on 2/25/2016.
 */
public class NoteItem {
    private int mId;
    private String mCreateTime;
    private String mAlarmTime;
    private String mTitle;
    private String mNote;
    private String mColor;
    private ArrayList<String> mPictures;

    public NoteItem(){
        mId = 0;
        mColor = "#FFFFFF";
        mCreateTime = "";
        mTitle = "";
        mNote = "";
        mAlarmTime = "";
        mPictures = new ArrayList<String>();
    }

    public NoteItem(int id, String title, String note, String createTime,  String alarmTime, String color, ArrayList<String> list){
        mId = id;
        mTitle = title;
        mNote = note;
        mCreateTime = createTime;
        mAlarmTime = alarmTime;
        mColor = color;
        mPictures = list;
    }

    //contructor copy
    public NoteItem(NoteItem noteItem){
        mId = noteItem.mId;
        mTitle = noteItem.mTitle;
        mNote = noteItem.mNote;
        mCreateTime = noteItem.mCreateTime;
        mAlarmTime = noteItem.mAlarmTime;
        mColor = noteItem.mColor;
        mPictures = new ArrayList<String>(noteItem.getPictures());
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public String getAlarmTime() {
        return mAlarmTime;
    }

    public void setAlarmTime(String mAlarmTime) {
        this.mAlarmTime = mAlarmTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
    }

    public ArrayList<String> getPictures() {
        return mPictures;
    }

    public void setPictures(ArrayList<String> mPictures) {
        this.mPictures = mPictures;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    private boolean isCreate(){
        if(mId == 0 && (!mPictures.isEmpty() || mAlarmTime != "" || !mNote.equals("") || !mTitle.equals(""))){
            return true;
        } else return false;
    }

    // create new row in DB
    public void create(Context context){
        if (isCreate()) {
            NoteTable noteTable = new NoteTable(context);
            mTitle = mTitle.equals("")
                    ? ((mNote.equals("") ? Define.DEFAULT_TITLE : mNote)) : mTitle;
            noteTable.insert(this);

            // tao thong bao
            int lastId = noteTable.getLastId();
            createNotification(context, lastId);
        }
    }

    public void update(Context context){
        NoteTable noteTable = new NoteTable(context);
        mTitle = mTitle.equals("")
                ? ((mNote.equals("") ? Define.DEFAULT_TITLE : mNote)) : mTitle;
        noteTable.update(this);
        createNotification(context, mId);
    }

    public void createNotification(Context context, int id){
        Log.i("Note id", id+"");
        if(!mAlarmTime.equals("")){
            //kiem tra alarmtime lon hon hien tai
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(mAlarmTime);
                if(date.getTime() > System.currentTimeMillis()){
                    //TODO: tao notification
                    AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("note_id", id);
                    intent.putExtra("note_title", mTitle);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
                    manager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                }
            } catch (ParseException pe){
                pe.printStackTrace();
            }
        }
    }

    public String toString(){
        return "mId:"+mId+","
                +"mCreateTime:"+mCreateTime+","
                +"mAlarmTime:"+mAlarmTime+","
                +"mTitle:"+mTitle+","
                +"mNote:"+mNote+","
                +"mColor:"+mColor+","
                +"mPicture:"+mPictures.toString();
    }
}
