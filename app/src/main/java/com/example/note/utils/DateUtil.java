package com.example.note.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String convertStringToDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        if(date.equals("Today")){
            return sdf.format(d);
        } else if(date.equals("Tomorrow")){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);
            return sdf.format(c.getTime());
        } else if(date.contains("Next")){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 7);
            return sdf.format(c.getTime());
        }
        return date;
    }
}
