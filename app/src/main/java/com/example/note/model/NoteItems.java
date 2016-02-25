package com.example.note.model;

/**
 * Created by phund on 2/25/2016.
 */
public class NoteItems {
    private String mCreateTime;
    private String mAlarmTime;
    private String mTitle;
    private String mNote;
    private String mColor;
    private String mPictures;

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

    public String getPictures() {
        return mPictures;
    }

    public void setPictures(String mPictures) {
        this.mPictures = mPictures;
    }
}
