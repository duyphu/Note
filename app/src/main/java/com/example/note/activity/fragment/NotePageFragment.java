package com.example.note.activity.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.note.config.Define;
import com.example.note.custom.adapter.ImageListAdapter;
import com.example.note.db.table.NoteTable;
import com.example.note.model.NoteItem;
import com.example.note.utils.DateUtil;

import java.security.acl.LastOwnerException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by phund on 2/29/2016.
 */
public class NotePageFragment extends Fragment {

    public static final String ARG_ID = "noteId";
    private int mNoteId;
    private NoteItem mNoteItem;
    protected TextView tvAlarm, tvCreatTime;
    protected EditText etTitle, etNote;
    protected Spinner sAlarmDate, sAlarmTime;
    protected LinearLayout llSetAlarm, llMainNew, llContent;
    protected GridView gvInsertPicture;
    protected ArrayList<String> mDateAlams,mTimeAlarms;

    public static NotePageFragment create(int position, ArrayList<Integer> ids) {
        NotePageFragment fragment = new NotePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, ids.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    public NotePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteId = getArguments().getInt(ARG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        View rootView = inflater
                .inflate(R.layout.fragment_open_note_page, container, false);
        NoteTable noteTable = new NoteTable(getActivity());
        mNoteItem = noteTable.getOne(mNoteId);

        llSetAlarm = (LinearLayout)rootView.findViewById(R.id.ll_set_alarm);
        llMainNew = (LinearLayout)rootView.findViewById(R.id.ll_main_new);
        llContent = (LinearLayout)rootView.findViewById(R.id.ll_content);
        tvCreatTime = (TextView)rootView.findViewById(R.id.tv_create_time);
        tvAlarm = (TextView)rootView.findViewById(R.id.tv_alarm);
        sAlarmDate = (Spinner)rootView.findViewById(R.id.s_alarm_date);
        sAlarmTime = (Spinner)rootView.findViewById(R.id.s_alarm_time);
        etNote = (EditText)rootView.findViewById(R.id.et_note);
        etTitle = (EditText)rootView.findViewById(R.id.et_title);

        //spinner date and time
        mDateAlams = new ArrayList<String>();
        mDateAlams.add("Today");
        mDateAlams.add("Tomorrow");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        mDateAlams.add("Next "+dayOfTheWeek);
        mDateAlams.add("Other");
        final ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(getActivity()
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

                    mDatePicker = new DatePickerDialog(getActivity()
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
        final ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getActivity()
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
                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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

        String createTime = DateUtil.convertStandarToVnDate(mNoteItem.getCreateTime());
        try {
            tvCreatTime.setText(createTime.substring(0, createTime.lastIndexOf(":")));
            String alarmTime = mNoteItem.getAlarmTime();
            if (!alarmTime.equals("")) {
                tvAlarm.setVisibility(View.GONE);
                llSetAlarm.setVisibility(View.VISIBLE);
                alarmTime = DateUtil.convertStandarToVnDate(alarmTime);
                String[] list = alarmTime.split(" ");
                // set value for spinner
                int indexDate = mDateAlams.size() - 1;
                sAlarmDate.setSelection(indexDate);
                mDateAlams.set(indexDate, list[0]);
                mDateAlams.add("Other");

                int indexTime = mTimeAlarms.size() - 1;
                sAlarmTime.setSelection(indexTime);
                mTimeAlarms.set(indexTime, list[1].substring(0, list[1].lastIndexOf(":")));
                mTimeAlarms.add("Other");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        etTitle.setText(mNoteItem.getTitle());
        etNote.setText(mNoteItem.getNote());

        llMainNew.setBackgroundColor(Color.parseColor(mNoteItem.getColor()));
        llContent.setBackgroundColor(Color.parseColor(mNoteItem.getColor()));
        gvInsertPicture = (GridView)rootView.findViewById(R.id.gv_insert_picture);
        new GridLoadAsynTack().execute();
        gvInsertPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mNoteItem.getPictures().get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + path), "image/*");
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void deletePic(int position){
        ImageListAdapter adapter = (ImageListAdapter)gvInsertPicture.getAdapter();
        ArrayList<String> list = adapter.getData();
        list.remove(position);
        adapter.notifyDataSetChanged();
        mNoteItem.setPictures(list);
    }

    public void changeColor(String color){
        llMainNew.setBackgroundColor(Color.parseColor(color));
        llContent.setBackgroundColor(Color.parseColor(color));
        mNoteItem.setColor(color);
    }

    public void insertPicture(String path){
        //TODO: update gridview
        ArrayList<String> list = mNoteItem.getPictures();
        list.add(path);
        mNoteItem.setPictures(list);
        gvInsertPicture.setAdapter(new ImageListAdapter(getActivity(), mNoteItem.getPictures()));
    }

    public NoteItem getNoteItem(){
        return  mNoteItem;
    }

    public int getVisibilityTVAlarm(){
        return tvAlarm.getVisibility();
    }

    public void setVisibilityTVAlarm(int visibility){
        tvAlarm.setVisibility(visibility);
    }

    public int getVisibilityLLSetAlarm(){
        return llSetAlarm.getVisibility();
    }

    public void setVisibilityLLSetAlarm(int visibility){
        llSetAlarm.setVisibility(visibility);
    }

    public void saveData(NoteItem lastNoteItem){
        String title = etTitle.getText().toString();
        String note = etNote.getText().toString();
        if(title.equals("")){
            if(!note.equals("")) title = note;
        }
        mNoteItem.setAlarmTime(getAlarmTime());
        mNoteItem.setTitle(title);
        mNoteItem.setNote(note);

        // kiem tra co thay doi
        Log.i("NoteItem 1", lastNoteItem.toString());
        Log.i("NoteItem 2", mNoteItem.toString());
        if(!lastNoteItem.toString().equals(mNoteItem.toString())) {
            // update create time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mNoteItem.setCreateTime(dateFormat.format(Calendar.getInstance().getTime()));
            mNoteItem.update(getActivity());
        }
    }

    public String getAlarmTime(){
        String alarmTime = "";
        if(tvAlarm.getVisibility() != View.VISIBLE){
            String date = sAlarmDate.getSelectedItem().toString();
            alarmTime = DateUtil.convertStringToDate(date) + " "
                    + sAlarmTime.getSelectedItem().toString() + ":00";
            alarmTime = DateUtil.convertVnToStandarDate(alarmTime);
        }
        return alarmTime;
    }

    public String getTitle(){
        return etTitle.getText().toString();
    }

    public String getNote(){
        return etNote.getText().toString();
    }

    private class GridLoadAsynTack extends AsyncTask<Void, Void, ImageListAdapter>{

        @Override
        protected ImageListAdapter doInBackground(Void... params) {
            new ImageListAdapter(getActivity(), mNoteItem.getPictures());
            return new ImageListAdapter(getActivity(), mNoteItem.getPictures());
        }

        @Override
        protected void onPostExecute(ImageListAdapter imageListAdapter) {
            super.onPostExecute(imageListAdapter);
            gvInsertPicture.setAdapter(imageListAdapter);
        }
    }
}