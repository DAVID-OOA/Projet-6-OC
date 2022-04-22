package com.oconte.david.go4lunch.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class ForNetIsAvailable {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
