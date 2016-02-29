package com.example.note.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.db.table.NoteTable;
import com.example.note.model.NoteItem;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phund on 2/29/2016.
 */
public class ScreenSlidePageFragment extends Fragment {

    public static final String ARG_ID = "noteId";
    private int mNoteId;

    public static ScreenSlidePageFragment create(int position, ArrayList<Integer> ids) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, ids.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
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
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
        Activity activity = getActivity();
        NoteTable noteTable = new NoteTable(activity);
        NoteItem noteItem = noteTable.getOne(mNoteId);

        String title = noteItem.getTitle();
        Log.i("title>>>>>>", title);
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(title);
        activity.setTitle(title);

        return rootView;
    }
}