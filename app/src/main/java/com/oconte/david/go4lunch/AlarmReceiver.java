package com.oconte.david.go4lunch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * It's what doing the AlarmManager
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        getWorkManager(context);
    }

    private void getWorkManager(Context context){
        Data data = new Data.Builder()
                .putString(AlarmWorker.CHANNEL_ID, "Notification.")
                .build();

        //This is the subclass of periodicWorkRequest
        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(AlarmWorker.class,24, TimeUnit.HOURS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
    }
}
