package com.androks.contactstest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by androks on 16.07.17.
 */

public class DateTimeUtils {

    private static final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public static String dateToString(Date date){
        return dateFormat.format(date);
    }

    public static Date stringToDate(String someDate){
        try{
            return dateFormat.parse(someDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
