package com.bagushikano.sikedatmobileadmin.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class ChangeDateTimeFormat {
    private ChangeDateTimeFormat() {

    }
    public static String changeDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMMM-yyyy";
        SimpleDateFormat input = new SimpleDateFormat(inputPattern);
        SimpleDateFormat output = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = input.parse(time);
            str = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String changeDateTimeFormat (String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(time);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy, HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            time = dateFormatter.format(value);

        }
        catch (Exception e)
        {
            time = "00-00-0000 00:00";
        }
        Log.d("tanggal", time);
        return time;
    }

    public static String changeDateTimeFormatForCreatedAt (String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(time);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy, HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            time = dateFormatter.format(value);

        }
        catch (Exception e)
        {
            time = "00-00-0000 00:00";
        }
        Log.d("tanggal", time);
        return time;
    }

    public static String changeDateFormatForForm(String time) {
        String inputPattern = "EEE, dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat input = new SimpleDateFormat(inputPattern);
        SimpleDateFormat output = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = input.parse(time);
            str = output.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
