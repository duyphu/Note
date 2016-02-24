package com.example.note.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.note.R;
import com.example.note.config.Define;
import com.example.note.fragment.TimePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by phund on 2/24/2016.
 */
public class NewNoteActivity extends AppCompatActivity {
    private LinearLayout mLLSetAlarm;
    private TextView mTVAlarm, mTVCreatDate;
    private EditText mEDDate;
    private EditText mEDTime;
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.t_new_note);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mLLSetAlarm = (LinearLayout)findViewById(R.id.ll_set_alarm);
        mTVAlarm = (TextView)findViewById(R.id.tv_alarm);
        mTVCreatDate = (TextView)findViewById(R.id.tv_create_time);
        mEDDate = (EditText)findViewById(R.id.et_date);
        mEDTime = (EditText)findViewById(R.id.et_time);
        mEDDate.setKeyListener(null);
        mEDTime.setKeyListener(null);

        // set default date and time
        mEDTime.setText(Define.DEFAULT_TIME);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d/MM/y");
        Date curDate = calendar.getTime();
        mEDDate.setText(dateFormat.format(curDate));
        dateFormat = new SimpleDateFormat("d/MM/y H:m");
        mTVCreatDate.setText(dateFormat.format(curDate));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            Log.i("Picture path",picturePath);
//            cursor.close();
//
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camera) {
            selectImage();
        } else if(id == R.id.action_change_background_color){

        } else if(id == R.id.action_save){

        }

        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
        builder.setTitle("Insert Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void tvAlarmOnClick(View v){
        if(mLLSetAlarm.getVisibility() != View.VISIBLE){
            mLLSetAlarm.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        }
    }

    public void ivCloseOnClick(View v){
        mLLSetAlarm.setVisibility(View.GONE);
        mTVAlarm.setVisibility(View.VISIBLE);
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
}
