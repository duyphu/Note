package com.example.note.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.model.NoteItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by phund on 2/26/2016.
 */
public class NoteListAdapter extends ArrayAdapter<NoteItem> {
    private Context mContext;
    private int mLayoutId;
    private ArrayList<NoteItem> mLists;

    public NoteListAdapter(Context context, int resource, ArrayList<NoteItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutId = resource;
        mLists = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mLayoutId, null);
        }
        NoteItem noteItem = mLists.get(position);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
        TextView tvNote = (TextView)convertView.findViewById(R.id.tv_note);
        TextView tvCreateTime = (TextView)convertView.findViewById(R.id.tv_create_time);

        tvTitle.setText(noteItem.getTitle());
        tvNote.setText(noteItem.getNote());
        try {
            DateFormat fromFormat = new SimpleDateFormat("d/MM/y H:m:s");
            DateFormat toFormat = new SimpleDateFormat("d/MM H:m");
            Date date = fromFormat.parse(noteItem.getCreateTime());
            tvCreateTime.setText(toFormat.format(date));
        } catch (ParseException pe){
            pe.printStackTrace();
        }


        if(!noteItem.getAlarmTime().equals("")){
            ImageView ivAlarm = (ImageView)convertView.findViewById(R.id.iv_alarm);
            ivAlarm.setImageResource(R.drawable.ic_alarm_medium);
        }
        LinearLayout llContentNote = (LinearLayout)convertView.findViewById(R.id.ll_content_note);
        llContentNote.setBackgroundColor(Color.parseColor(noteItem.getColor()));
        return convertView;
    }
}
