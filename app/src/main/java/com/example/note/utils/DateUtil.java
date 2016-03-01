package com.example.note.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by phund on 3/1/2016.
 */
public class DateUtil {
    public static String convertVnToStandarDate(String date){
        String newDate = "";
        DateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = fromFormat.parse(date);
            newDate = toFormat.format(mDate);
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        return newDate;
    }

    public static String convertStandarToVnDate(String date){
        String newDate = "";
        DateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = fromFormat.parse(date);
            newDate = toFormat.format(mDate);
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        return newDate;
    }
}
