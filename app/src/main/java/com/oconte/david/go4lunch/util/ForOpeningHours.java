package com.oconte.david.go4lunch.util;

import android.annotation.SuppressLint;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.Geometry;
import com.oconte.david.go4lunch.models.OpeningHours;
import com.oconte.david.go4lunch.models.Result;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public abstract class ForOpeningHours {

    private static String FORMAT_HOURS = "HHmm";



    public static Date converStringInDate(int hour) {
        String hourString = String.valueOf(hour);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(FORMAT_HOURS);
        try {
            return dateFormat.parse(hourString);
        } catch (ParseException e) {
            return null;
        }
    }

   public static int getOpeningTime(OpeningHours openingHours) {
        if(openingHours == null || openingHours.getPeriods() == null) return R.string.no_time;
        if(openingHours.getOpenNow() != null && !openingHours.getOpenNow()){
            return R.string.closed;
        }

        int dayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1;
        if(openingHours.getPeriods().size() >= dayOfTheWeek+1){
            Period periodOfTheDay = openingHours.getPeriods().get(dayOfTheWeek);

            //if(periodOfTheDay.getClose() == null) return R.string.open_24_7;

            //String closureString = periodOfTheDay.getClose().getTime();
            /*int closure = Integer.parseInt(closureString);

            Date todayDate = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(FORMAT_HOURS);
            String todayDateString = dateFormat.format(todayDate);
            int timeNow = Integer.parseInt(todayDateString);
            int timeBeforeClosure = closure - timeNow;
            if(timeBeforeClosure <= 100){
                return R.string.closing_soon;
            } else {
                return closure;
            }*/
        }
        return R.string.no_time;
    }
}
