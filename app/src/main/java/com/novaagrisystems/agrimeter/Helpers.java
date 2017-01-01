package com.novaagrisystems.agrimeter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by peter on 1/1/17.
 */

public class Helpers {

    public static String getDate(long time) {
        //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

}
