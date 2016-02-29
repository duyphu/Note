package com.example.note.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.note.R;
import com.example.note.activity.base.BaseActivity;
import com.example.note.config.Define;
import com.example.note.custom.adapter.ImageListAdapter;
import com.example.note.model.NoteItem;
import com.example.note.utils.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by phund on 2/24/2016.
 */
public class NewNoteActivity extends BaseActivity {
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 2;
    private LinearLayout llSetAlarm;
    private LinearLayout llMainMew;
    private TextView tvAlarm, tvCreatTime;
    private EditText etDate ,etTime, etTitle, etNote;
    private GridView gvInsertPicture;
    private Dialog dChooseColor;
    private NoteItem mNoteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.t_new_note);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.mipmap.notes);
        actionBar.setDisplayUseLogoEnabled(true);

        llSetAlarm = (LinearLayout)findViewById(R.id.ll_set_alarm);
        llMainMew = (LinearLayout)findViewById(R.id.ll_main_new);
        tvAlarm = (TextView)findViewById(R.id.tv_alarm);
        tvCreatTime = (TextView)findViewById(R.id.tv_create_time);
        etTitle = (EditText)findViewById(R.id.et_title);
        etNote = (EditText)findViewById(R.id.et_note);
        etDate = (EditText)findViewById(R.id.et_date);
        etTime = (EditText)findViewById(R.id.et_time);
        etDate.setKeyListener(null);
        etTime.setKeyListener(null);

        // set default date and time
        etTime.setText(Define.DEFAULT_TIME);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d/MM/y");
        Date curDate = calendar.getTime();
        etDate.setText(dateFormat.format(curDate));
        dateFormat = new SimpleDateFormat("d/MM/y H:m");
        tvCreatTime.setText(dateFormat.format(curDate));

        mNoteItem = new NoteItem();
        gvInsertPicture = (GridView)findViewById(R.id.gv_insert_picture);
        gvInsertPicture.setAdapter(new ImageListAdapter(this, mNoteItem.getPictures()));

        gvInsertPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mNoteItem.getPictures().get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if(requestCode == REQUEST_CAMERA){
                //save file to picture note folder
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                FileUtil.createFolder(Define.PICTURE_NOTE_FOLDER);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                Calendar calendar = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yMMd_Hms");
                String fileName = Define.PICTURE_NOTE_FOLDER+"/IMG_"
                        + dateFormat.format(calendar.getTime()) + "_"
                        + System.currentTimeMillis() + ".jpg";
                File destination = new File(fileName);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<String> tmp = mNoteItem.getPictures();
                tmp.add(fileName);
                mNoteItem.setPictures(tmp);
                gvInsertPicture.setAdapter(new ImageListAdapter(this, mNoteItem.getPictures()));
            } else if (requestCode == SELECT_FILE){
                try {
                    FileUtil.createFolder(Define.PICTURE_NOTE_FOLDER);

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    // copy file to picture note folder
                    String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                    String newPath = Define.PICTURE_NOTE_FOLDER + "/" + fileName;
                    FileUtil.copy(new File(picturePath), new File(newPath));

                    ArrayList<String> tmp = mNoteItem.getPictures();
                    tmp.add(newPath);
                    mNoteItem.setPictures(tmp);
                    gvInsertPicture.setAdapter(new ImageListAdapter(this, mNoteItem.getPictures()));
                    cursor.close();
                } catch (NullPointerException ne){
                    ne.printStackTrace();
                }
            }
        }
    }

    private void getDataToSave(){
        mNoteItem.setTitle(etTitle.getText().toString());
        mNoteItem.setNote(etNote.getText().toString());
        if (tvAlarm.getVisibility() != View.VISIBLE) {
            String alarmTime = etDate.getText() + " " + etTime.getText() + ":00";
            mNoteItem.setAlarmTime(alarmTime);
        }
        mNoteItem.setCreateTime(tvCreatTime.getText() + ":00");
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(mNoteItem.getId() != 0) {
            getDataToSave();
            mNoteItem.update(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_note, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.action_new).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_insert_picture) {
            insertPicture();
        } else if(id == R.id.action_choose_color){
            chooseColor();
        } else if(id == R.id.action_done){
            getDataToSave();
            mNoteItem.create(this);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
        builder.setTitle("Insert Picture");
        builder.setItems(Define.DIALOG_CHOOSE_COLOR_ITEMS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (Define.DIALOG_CHOOSE_COLOR_ITEMS[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (Define.DIALOG_CHOOSE_COLOR_ITEMS[item].equals("Choose Photo")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);
                }
            }
        });
        builder.show();
    }

    public void chooseColor(){
        dChooseColor = new Dialog(NewNoteActivity.this);
        dChooseColor.setTitle("Choose Color");
        dChooseColor.setContentView(R.layout.dialog_choose_color);
        dChooseColor.show();
//        builder.s
    }

    public void tvAlarmOnClick(View v){
        if(llSetAlarm.getVisibility() != View.VISIBLE){
            llSetAlarm.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        }
    }

    public void ivCloseOnClick(View v){
        llSetAlarm.setVisibility(View.GONE);
        tvAlarm.setVisibility(View.VISIBLE);
    }

    public void etTimeOnClick(View v){
        TimePickerDialog mTimePicker;
        final EditText editTextTime = (EditText)v;
        String values[] = editTextTime.getText().toString().split(":");
        int hour = Integer.parseInt(values[0]);
        int minute = Integer.parseInt(values[1]);
        mTimePicker = new TimePickerDialog(NewNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editTextTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void etDateOnClick(View v){
        DatePickerDialog mDatePicker;
        final EditText editTextDate = (EditText)v;
        String values[] = editTextDate.getText().toString().split("/");
        int mDay = Integer.parseInt(values[0]);
        int mMonth = Integer.parseInt(values[1]);
        int mYear = Integer.parseInt(values[2]);

        mDatePicker = new DatePickerDialog(NewNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                editTextDate.setText(String.format("%02d/%02d/%d", selectedday, selectedmonth,selectedyear));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    public void tvColorOnClick(View v){
        TextView tv = (TextView)v;
        ColorDrawable colorDrawable = (ColorDrawable) tv.getBackground();
        int intColor = colorDrawable.getColor();
        llMainMew.setBackgroundColor(intColor);
        // convert color value to color code
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        mNoteItem.setColor(hexColor);
//        Log.i("Color", Color.parseColor("#ffffff")+"");
        dChooseColor.dismiss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
