package com.example.note.config;

import android.os.Environment;

import com.example.note.R;
import com.example.note.model.DialogItem;

/**
 * Created by phund on 2/24/2016.
 */
public class Define {
    public static final String PICTURE_NOTE_FOLDER = Environment.getExternalStorageDirectory()
            + "/Pictures" + "/PictureNote";
    public static final CharSequence[] DIALOG_CHOOSE_COLOR_ITEMS = { "Take Photo", "Choose Photo"};

    public static final String DEFAULT_TITLE = "Untitle";

    // database
    public static final String DATABASE_NAME = "notes.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_NOTE = "NOTE";
    public static final String COLUMN_CREATE_TIME = "CREATE_TIME";
    public static final String COLUMN_ALARM_TIME = "ALARM_TIME";
    public static final String COLUMN_PICTURES = "PICTURES";
    public static final String COLUMN_COLOR = "COLOR";

    public static final String TEXT_TYPE = " TEXT";
    public static final String NUMBER_TYPE = " INTEGER";
    public static final String DATETIME_TYPE = " DATETIME";
    public static final String NOT_NULL_TYPE = " NOT NULL";
    public static final String AUTOINCREMENT_TYPE = " AUTOINCREMENT";
    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String COMMA_STEP = ",";
    public static final String BRACKET_OPEN = "(";
    public static final String BRACKET_CLOSE = ")";

    public static final String SQL_DELETE_TABLE_NOTES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CREATE_TABLE_NOTES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + BRACKET_OPEN +COLUMN_ID + NUMBER_TYPE + NOT_NULL_TYPE + PRIMARY_KEY + AUTOINCREMENT_TYPE + COMMA_STEP
            + COLUMN_TITLE + TEXT_TYPE + NOT_NULL_TYPE + COMMA_STEP
            + COLUMN_NOTE + TEXT_TYPE + NOT_NULL_TYPE + COMMA_STEP
            + COLUMN_CREATE_TIME + DATETIME_TYPE + NOT_NULL_TYPE + COMMA_STEP
            + COLUMN_COLOR + TEXT_TYPE + NOT_NULL_TYPE + COMMA_STEP
            + COLUMN_PICTURES + TEXT_TYPE + NOT_NULL_TYPE + COMMA_STEP
            + COLUMN_ALARM_TIME + DATETIME_TYPE + NOT_NULL_TYPE + BRACKET_CLOSE;


    public static int REQUEST_CAMERA = 1;
    public static int SELECT_FILE = 2;

    public static String NOTIFICATION_TITLE = "Note notification";
    public static final DialogItem[] ITEMS_INSERT_PIC_DIALOG = {
            new DialogItem("Take Photo", R.drawable.ic_camera_gray),
            new DialogItem("Choose Photo", R.drawable.ic_image)
    };
}
