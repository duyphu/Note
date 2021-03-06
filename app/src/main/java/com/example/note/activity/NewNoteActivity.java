package com.example.note.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.note.R;
import com.example.note.activity.base.BaseActivity;
import com.example.note.config.Define;
import com.example.note.custom.adapter.ImageListAdapter;
import com.example.note.custom.adapter.ItemDialogAdapter;
import com.example.note.model.DialogItem;
import com.example.note.model.NoteItem;
import com.example.note.utils.DateUtil;
import com.example.note.utils.DialogUtil;
import com.example.note.utils.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by phund on 2/24/2016.
 */
public class NewNoteActivity extends BaseActivity {
    protected LinearLayout llSetAlarm;
    protected LinearLayout llMainNew, LLParentNew;
    protected TextView tvAlarm, tvCreatTime;
    protected EditText etTitle, etNote;
    protected Spinner sAlarmDate, sAlarmTime;
    protected GridView gvInsertPicture;
    protected Dialog dChooseColor;
    protected NoteItem mNoteItem;
    protected static ArrayList<String> mTimeAlarms, mDateAlams;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.t_new_note);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_toolbar);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        toolbar.setBackgroundDrawable(bitmapDrawable);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setLogo(R.mipmap.notes);
        actionBar.setDisplayUseLogoEnabled(true);

        llSetAlarm = (LinearLayout)findViewById(R.id.ll_set_alarm);
        llMainNew = (LinearLayout)findViewById(R.id.ll_main_new);
        LLParentNew = (LinearLayout)findViewById(R.id.ll_parent_new);
        tvAlarm = (TextView)findViewById(R.id.tv_alarm);
        tvCreatTime = (TextView)findViewById(R.id.tv_create_time);
        etTitle = (EditText)findViewById(R.id.et_title);
        etNote = (EditText)findViewById(R.id.et_note);
        sAlarmDate = (Spinner)findViewById(R.id.s_alarm_date);
        sAlarmTime = (Spinner)findViewById(R.id.s_alarm_time);


        mDateAlams = new ArrayList<String>();
        mDateAlams.add("Today");
        mDateAlams.add("Tomorrow");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        mDateAlams.add("Next "+dayOfTheWeek);
        mDateAlams.add("Other");
        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, mDateAlams);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAlarmDate.setAdapter(dateAdapter);
        sAlarmDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(mDateAlams.get(position).equals("Other")) {
                    DatePickerDialog mDatePicker;
                    Calendar c = Calendar.getInstance();
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    int mMonth = c.get(Calendar.MONTH);
                    int mYear = c.get(Calendar.YEAR);

                    mDatePicker = new DatePickerDialog(NewNoteActivity.this
                            , new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker
                                , int selectedyear, int selectedmonth, int selectedday) {
                            mDateAlams.set(position, String.format("%02d/%02d/%d"
                                    , selectedday, selectedmonth, selectedyear));

                            if (mDateAlams.indexOf("Other") < 0) mDateAlams.add("Other");
                            dateAdapter.notifyDataSetChanged();
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String tmp [] = {"09:00","13:00","17:00","20:00","Other"};
        mTimeAlarms = new ArrayList<String>(Arrays.asList(tmp));
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, mTimeAlarms);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAlarmTime.setAdapter(timeAdapter);
        sAlarmTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(mTimeAlarms.get(position).equals("Other")){
                    TimePickerDialog mTimePicker;
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY)+1;
                    int minute = 0;
                    mTimePicker = new TimePickerDialog(NewNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            mTimeAlarms.set(position, String.format("%02d:%02d", selectedHour,
                                    selectedMinute));
                            if(mTimeAlarms.indexOf("Other") < 0) mTimeAlarms.add("Other");
                            timeAdapter.notifyDataSetChanged();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
        switch (id) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.action_insert_picture:
                insertPicture();
                return true;
            case R.id.action_choose_color:
                chooseColor();
                return true;
            case R.id.action_done:
                getDataToSave();
                mNoteItem.create(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if(requestCode == Define.REQUEST_CAMERA){
                //save file to picture note folder
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                FileUtil.createFolder(Define.PICTURE_NOTE_FOLDER);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

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
            } else if (requestCode == Define.SELECT_FILE){
                String newPath = FileUtil.copyPictureToNoteFolder(data, this);
                if(!newPath.equals("")) {
                    ArrayList<String> tmp = mNoteItem.getPictures();
                    tmp.add(newPath);
                    mNoteItem.setPictures(tmp);
                    gvInsertPicture.setAdapter(new ImageListAdapter(this, mNoteItem.getPictures()));
                }
            }
        }
    }

    public void ivDeletePicOnClick(View v){
        DialogUtil.showDialogConfirmDeletePic(NewNoteActivity.this, v, mNoteItem, gvInsertPicture);
    }

    protected void getDataToSave(){
        mNoteItem.setTitle(etTitle.getText().toString());
        mNoteItem.setNote(etNote.getText().toString());
        if (tvAlarm.getVisibility() != View.VISIBLE) {
            String date = sAlarmDate.getSelectedItem().toString();
            String alarmTime = DateUtil.convertStringToDate(date) + " "
                    + sAlarmTime.getSelectedItem().toString() + ":00";
            mNoteItem.setAlarmTime(DateUtil.convertVnToStandarDate(alarmTime));
        }
        mNoteItem.setCreateTime(DateUtil.convertVnToStandarDate(tvCreatTime.getText() + ":00"));
    }

    protected void insertPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
        builder.setTitle("Insert Picture");
        ListAdapter adapter = new ItemDialogAdapter(NewNoteActivity.this,
                android.R.layout.select_dialog_item, Define.ITEMS_INSERT_PIC_DIALOG);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (Define.DIALOG_CHOOSE_COLOR_ITEMS[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Define.REQUEST_CAMERA);
                } else if (Define.DIALOG_CHOOSE_COLOR_ITEMS[item].equals("Choose Photo")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, Define.SELECT_FILE);
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

    public void tvColorOnClick(View v){
        TextView tv = (TextView)v;
        ColorDrawable colorDrawable = (ColorDrawable) tv.getBackground();
        int intColor = colorDrawable.getColor();
        llMainNew.setBackgroundColor(intColor);
        LLParentNew.setBackgroundColor(intColor);
        // convert color value to color code
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        mNoteItem.setColor(hexColor);
//        Log.i("Color", Color.parseColor("#ffffff")+"");
        dChooseColor.dismiss();
    }
}
