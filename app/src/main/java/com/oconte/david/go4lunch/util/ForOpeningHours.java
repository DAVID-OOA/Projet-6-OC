package com.oconte.david.go4lunch.util;

import com.oconte.david.go4lunch.R;
import com.oconte.david.go4lunch.models.OpeningHours;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ForOpeningHours {

    private static String FORMAT_HOURS = "HHmm";



    public static Date converStringInDate(int hour) {
        String hourString = String.valueOf(hour);
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_HOURS);
        try {
            return dateFormat.parse(hourString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getOpeningTime(OpeningHours openingHours) {
        return R.string.no_time;
    }
}
