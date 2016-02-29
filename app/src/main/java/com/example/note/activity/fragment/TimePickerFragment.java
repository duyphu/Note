package com.example.note.activity.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.note.R;

import java.util.Calendar;

/**
 * Created by phund on 2/24/2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private int mHour;
    private int mMinute;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("time >>>>>>>>>>>", view.getParent().toString());
        ViewGroup viewGroup = (ViewGroup)view.getParent().getParent();
        EditText editText = (EditText)viewGroup.findViewById(R.id.et_time);
        String time = String.format("%2d:%2d", hourOfDay, minute);
        editText.setText(time);
    }
}