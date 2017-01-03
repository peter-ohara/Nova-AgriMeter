package com.novaagrisystems.agrimeter;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by peter on 1/1/17.
 */

public class Helpers {

    public static String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        String dateString = formatter.format(new Date(time * 1000L));
        return dateString;
    }

    public static String getTimeRelativeToNow(Context context, long time) {
        return (String) DateUtils.getRelativeDateTimeString(context,
                time * 1000,
                DateUtils.DAY_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL);
    }



    public static String getHumiditySummary(Context context, Float humidity) {
        if (humidity < 40) {
            return context.getString(R.string.humidity_low_summary);
        } else if (humidity > 90) {
            return context.getString(R.string.humidity_high_summary);
        } else {
            return context.getString(R.string.humidity_ok_summary);
        }
    }

    public static String getHumidityMessage(Context context, Float humidity) {
        if (humidity < 40) {
            return context.getString(R.string.humidity_low_message);
        } else if (humidity > 90) {
            return context.getString(R.string.humidity_high_message);
        } else {
            return context.getString(R.string.humidity_ok_message);
        }
    }



    public static String getTemperatureSummary(Context context, Float temperature) {
        if (temperature < 15) {
            return context.getString(R.string.temperature_low_summary);
        } else if (temperature > 35) {
            return context.getString(R.string.temperature_high_summary);
        } else {
            return context.getString(R.string.temperature_ok_summary);
        }
    }

    public static String getTemperatureMessage(Context context, Float temperature) {
        if (temperature < 15) {
            return context.getString(R.string.temperature_low_message);
        } else if (temperature > 35) {
            return context.getString(R.string.temperature_high_message);
        } else {
            return context.getString(R.string.temperature_ok_message);
        }
    }



    public static String getMoistureSummary(Context context, Float moisture) {
        if (moisture < 20) {
            return context.getString(R.string.moisture_low_summary);
        } else if (moisture > 90) {
            return context.getString(R.string.moisture_high_summary);
        } else {
            return context.getString(R.string.moisture_ok_summary);
        }
    }

    public static String getMoistureMessage(Context context, Float moisture) {
        if (moisture < 20) {
            return context.getString(R.string.moisture_low_message);
        } else if (moisture > 90) {
            return context.getString(R.string.moisture_high_message);
        } else {
            return context.getString(R.string.moisture_ok_message);
        }
    }



    public static String getLightSummary(Context context, Float light) {
        if (light < 30) {
            return context.getString(R.string.light_low_summary);
        } else if (light > 90) {
            return context.getString(R.string.light_high_summary);
        } else {
            return context.getString(R.string.light_ok_summary);
        }
    }

    public static String getLightMessage(Context context, Float light) {
        if (light < 30) {
            return context.getString(R.string.light_low_message);
        } else if (light > 90) {
            return context.getString(R.string.light_high_message);
        } else {
            return context.getString(R.string.light_ok_message);
        }
    }



}
