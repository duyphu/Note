package com.example.note.activity.fragment;

import android.app.Fragment;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.config.Define;
import com.example.note.custom.adapter.ImageListAdapter;
import com.example.note.db.table.NoteTable;
import com.example.note.model.NoteItem;
import com.example.note.utils.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by phund on 2/29/2016.
 */
public class NotePageFragment extends Fragment {

    public static final String ARG_ID = "noteId";
    private int mNoteId;
    private NoteItem mNoteItem;
    protected TextView tvAlarm, tvCreatTime;
    protected EditText etDate ,etTime, etTitle, etNote;
    protected LinearLayout llSetAlarm, llMainNew, llContent;
    protected GridView gvInsertPicture;

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
        etDate = (EditText)rootView.findViewById(R.id.et_date);
        etTime = (EditText)rootView.findViewById(R.id.et_time);
        etNote = (EditText)rootView.findViewById(R.id.et_note);
        etTitle = (EditText)rootView.findViewById(R.id.et_title);

        String createTime = DateUtil.convertStandarToVnDate(mNoteItem.getCreateTime());
        try {
            tvCreatTime.setText(createTime.substring(0, createTime.lastIndexOf(":")));
            String alarmTime = mNoteItem.getAlarmTime();
            if (!alarmTime.equals("")) {
                tvAlarm.setVisibility(View.GONE);
                llSetAlarm.setVisibility(View.VISIBLE);
                alarmTime = DateUtil.convertStandarToVnDate(alarmTime);
                String[] list = alarmTime.split(" ");
                etDate.setText(list[0]);
                etTime.setText(list[1].substring(0, list[1].lastIndexOf(":")));
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

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Fragment", "onPause");
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

    public void initAlarm(){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        etDate.setText(date);
        etTime.setText(Define.DEFAULT_TIME);
    }

    public void saveData(){
        String title = etTitle.getText().toString();
        String note = etNote.getText().toString();
        if(title.equals("")){
            if(!note.equals("")) title = note;
        }
        mNoteItem.setTitle(title);
        mNoteItem.setNote(note);
        String alarmTime = "";
        if(tvAlarm.getVisibility() != View.VISIBLE){
            alarmTime = etDate.getText() + " " + etTime.getText() + ":00";
            alarmTime = DateUtil.convertVnToStandarDate(alarmTime);
        }
        mNoteItem.setAlarmTime(alarmTime);
        mNoteItem.update(getActivity());
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