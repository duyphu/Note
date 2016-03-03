package com.example.note.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.note.R;
import com.example.note.activity.base.BaseActivity;
import com.example.note.activity.fragment.NotePageFragment;
import com.example.note.config.Define;
import com.example.note.db.table.NoteTable;
import com.example.note.custom.adapter.NotePagerAdapter;
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
import java.util.Calendar;

/**
 * Created by phund on 2/29/2016.
 */
public class OpenNoteActivity extends BaseActivity {
    private ViewPager mPager;
    private NotePagerAdapter mPagerAdapter;
    private Toolbar mToolbarBottom;
    private int mPosition;
    private ArrayList<Integer> mIds;
    protected Dialog dChooseColor;
    protected NotePageFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_note);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.t_top_note);
        setSupportActionBar(toolbarTop);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.notes);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mToolbarBottom = (Toolbar) findViewById(R.id.t_bottom_note);

        int noteId = getIntent().getIntExtra("noteId", 0);
        mIds = getIntent().getIntegerArrayListExtra("listId");
        mPosition  = mIds.indexOf(noteId);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new NotePagerAdapter(getFragmentManager(), mIds);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mPosition);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mFragment.saveData();
                mPosition = position;
                invalidateOptionsMenu();
            }
        });
        // not show keyboard when activity create
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

                mFragment.insertPicture(fileName);
            } else if (requestCode == Define.SELECT_FILE){
                String newPath = FileUtil.copyPictureToNoteFolder(data, this);
                if(!newPath.equals("")) {
                    mFragment.insertPicture(newPath);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFragment.saveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_top_note, menu);
        int resId;
        if(mPager.getCurrentItem() == mPagerAdapter.getCount() - 1){
            resId = R.drawable.ic_navigate_next_disable;
        } else {
            resId = R.drawable.ic_navigate_next;
        }
        ImageButton ibNext = (ImageButton)mToolbarBottom.findViewById(R.id.ib_next);
        ibNext.setImageResource(resId);

        if(mPager.getCurrentItem() == 0){
            resId = R.drawable.ic_navigate_previous_disable;
        } else {
            resId = R.drawable.ic_navigate_previous;
        }
        ImageButton ibPrev = (ImageButton)mToolbarBottom.findViewById(R.id.ib_prev);
        ibPrev.setImageResource(resId);
        setTitle(new NoteTable(OpenNoteActivity.this)
                .getOneColumn(Define.COLUMN_TITLE, mIds.get(mPosition)));
        mFragment = (NotePageFragment)mPagerAdapter.getRegisteredFragment(mPosition);
//        fragment.setTextView("ádasda");
//        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
//
//        // Add either a "next" or "finish" button to the action bar, depending on which page
//        // is currently selected.
//        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE, "Next");
////                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
////                        ? R.string.action_finish
////                        : R.string.action_next);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        item.setEnabled(mPager.getCurrentItem() < mPagerAdapter.getCount() - 1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.action_new:
                startActivity(new Intent(this, NewNoteActivity.class));
                finish();
                return true;
            case R.id.action_choose_color:
                chooseColor();
                return true;
            case  R.id.action_insert_picture:
                insertPicture();
                return true;
            case R.id.action_done:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    protected void insertPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OpenNoteActivity.this);
        builder.setTitle("Insert Picture");
        builder.setItems(Define.DIALOG_CHOOSE_COLOR_ITEMS, new DialogInterface.OnClickListener() {
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

    public void ivDeletePicOnClick(View v){
        DialogUtil.showDialogConfirmDeletePic(OpenNoteActivity.this, v, mFragment,null);
    }

    public void chooseColor(){
        dChooseColor = new Dialog(OpenNoteActivity.this);
        dChooseColor.setTitle("Choose Color");
        dChooseColor.setContentView(R.layout.dialog_choose_color);
        dChooseColor.show();
    }

    public void tvColorOnClick(View v){
        dChooseColor.dismiss();
        TextView tv = (TextView)v;
        ColorDrawable colorDrawable = (ColorDrawable) tv.getBackground();
        int intColor = colorDrawable.getColor();
        // convert color value to color code
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        mFragment.changeColor(hexColor);
    }

    public void tvAlarmOnClick(View v){
        if(mFragment.getVisibilityLLSetAlarm() != View.VISIBLE){
            //TODO: Khoi tao gia tri cho alarm
            mFragment.initAlarm();
            mFragment.setVisibilityLLSetAlarm(View.VISIBLE);
            v.setVisibility(View.GONE);
        }
    }

    public void ivCloseOnClick(View v){
        mFragment.setVisibilityTVAlarm(View.VISIBLE);
        mFragment.setVisibilityLLSetAlarm(View.GONE);
    }

    public void etTimeOnClick(View v){
        TimePickerDialog mTimePicker;
        final EditText editTextTime = (EditText)v;
        String values[] = editTextTime.getText().toString().split(":");
        int hour = Integer.parseInt(values[0]);
        int minute = Integer.parseInt(values[1]);
        mTimePicker = new TimePickerDialog(OpenNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        mDatePicker = new DatePickerDialog(OpenNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                editTextDate.setText(String.format("%02d/%02d/%d", selectedday, selectedmonth,
                        selectedyear));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }

    public void ibNextOnClick(View v){
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    public void ibPreviousOnClick(View v){
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void ibShareOnClick(View v){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String content = mFragment.getTitle()+"\n"+mFragment.getNote();
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(sharingIntent, "Share with"));
    }

    public void ibDeleteOnClick(View v){
        AlertDialog dialog = new AlertDialog.Builder(OpenNoteActivity.this).create();
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Are you sure you want to delete this?");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        NoteTable noteTable = new NoteTable(OpenNoteActivity.this);
                        noteTable.delete(mIds.get(mPosition));
                        dialog.dismiss();
                        startActivity(new Intent(OpenNoteActivity.this, MainActivity.class));
                        finish();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
