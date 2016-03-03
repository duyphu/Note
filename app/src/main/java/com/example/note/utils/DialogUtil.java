package com.example.note.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.activity.NewNoteActivity;
import com.example.note.activity.OpenNoteActivity;
import com.example.note.activity.fragment.NotePageFragment;
import com.example.note.custom.adapter.ImageListAdapter;
import com.example.note.model.NoteItem;

import java.util.ArrayList;

/**
 * Created by phund on 3/3/2016.
 */
public class DialogUtil {
    public static void showDialogConfirmDeletePic(final Context context, final Object ob1, final Object ob2, final Object ob3){
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Are you sure you want to delete this?");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        View view = (View) ob1;
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        TextView tvPosition = (TextView) viewGroup.findViewById(R.id
                                .tv_position);
                        int position = Integer.parseInt(tvPosition.getText().toString());

                        if (context instanceof OpenNoteActivity) {
                            NotePageFragment fragment = (NotePageFragment) ob2;
                            fragment.deletePic(position);
                            // create note
                        } else if (context instanceof NewNoteActivity) {
                            NoteItem item = (NoteItem) ob2;
                            GridView gridView = (GridView) ob3;
                            ArrayList<String> tmp = item.getPictures();
                            tmp.remove(position);
                            item.setPictures(tmp);
                            gridView.setAdapter(new ImageListAdapter(context, item.getPictures()));
                        }
                        dialog.dismiss();
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
