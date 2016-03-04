package com.example.note.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.note.model.DialogItem;

/**
 * Created by phund on 3/4/2016.
 */
public class ItemDialogAdapter extends ArrayAdapter<DialogItem> {
    Context mContext;
    DialogItem[] mDialogItems;
    public ItemDialogAdapter(Context context, int resource, DialogItem[] dialogItems) {
        super(context, resource, dialogItems);
        mContext = context;
        mDialogItems = dialogItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setCompoundDrawablesWithIntrinsicBounds(mDialogItems[position].icon, 0, 0, 0);
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
        return view;
    }
}
