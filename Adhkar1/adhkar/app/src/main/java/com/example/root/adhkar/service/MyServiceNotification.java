package com.example.root.adhkar.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by root on 03/07/15.
 */
public class MyServiceNotification extends Service {
    static AlarmManager alarms;
    static PendingIntent operation;
    int i = 0;
    private Receiver receiver;
    int period = 10;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadSavedPreferences();
        if (i != 0) {
            Log.e("TAG", "aaaaaaaaaaaaaaaaaa");
            alarms.cancel(operation);
            operation.cancel();
        }


        //TODO do something useful
        if (intent != null) {
            Bundle b = intent.getExtras();

            if ((b != null) && (b.get("key") != null)) {
                Log.e("TAG", "bbbbbbbbbbbbbbbb");
                period = (int) b.get("key");

            }

        }
        Log.e("TAG", "" + i);

        i++;

        receiver = Receiver.getSharedReceiver();
        receiver.setContext(this);
        IntentFilter filter = new IntentFilter("ALARM_ACTION");


        alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        intent = new Intent("ALARM_ACTION");
        intent.putExtra("param", "do3a2");
        operation = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), period, operation);

        this.registerReceiver(receiver, filter);


        Log.e("TAG", "àààà");


        return Service.START_STICKY;


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        if (i != 0) {
            alarms.cancel(operation);
            operation.cancel();
            this.unregisterReceiver(receiver);
        }
        Log.e("TAG", "=++++++++++++++++++++++++++");
        super.onDestroy();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        int heur = sharedPreferences.getInt("heur", 1);
        int min = sharedPreferences.getInt("minute", 0);
        period = heur * 60 * 60 * 1000 + min * 60 * 1000;
    }
}
