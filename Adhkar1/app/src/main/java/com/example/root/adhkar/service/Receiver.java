package com.example.root.adhkar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.root.adhkar.R;
import com.example.root.adhkar.activity.Note;

import java.util.Random;

/**
 * Created by root on 02/07/15.
 */
public class Receiver extends BroadcastReceiver {
    private static Receiver sharedReceiver;
    static int i = 0;
    private Context context;
    NotificationManager notificationManager;

    public void setContext(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static Receiver getSharedReceiver() {
        if (sharedReceiver == null) {
            sharedReceiver = new Receiver();
        }
        return sharedReceiver;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //createNotification(intent);

        displayNotificationOne();

    }

    private final void createNotification(Intent activity) {
        //Récupération du notification Manager
        String msg = "ayat";


        //Création de la notification avec spécification de l'icône de la notification et le texte qui apparait à la création de la notification
        final Notification notification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
        notification.vibrate = new long[]{0, 100, 25, 100};
        notification.ledOffMS = 25;
        notification.ledOnMS = 100;
        notification.ledARGB = Color.RED;
        notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;


        activity = new Intent(this.context.getApplicationContext(), Note.class);
        PendingIntent pendingintent = PendingIntent.getActivity(context.getApplicationContext(), 0, activity, Intent.FLAG_ACTIVITY_NEW_TASK);
        notification.contentIntent = pendingintent;


        notificationManager.notify(1, notification);
    }

    private NotificationManager myNotificationManager;
    private int notificationIdOne = 111;
    private int notificationIdTwo = 112;
    private int numMessagesOne = 0;
    private int numMessagesTwo = 0;

    protected void displayNotificationOne() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.context.getApplicationContext())
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Ayet  ")
                        .setContentText("Adhkar w do3aa");
// Creates an explicit intent for an Activity in your app

        mBuilder.setVibrate(new long[]{0, 100, 25, 100});
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);


        String[] tabDoaa = context.getResources().getStringArray(R.array.listadhkarnotification);
        Intent resultIntent = new Intent(this.context.getApplicationContext(), Note.class);
        Random r = new Random();

        int i = r.nextInt(tabDoaa.length - 1);
        resultIntent.putExtra("numd", i + "");
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context.getApplicationContext());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Note.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// mId allows you to update the notification later on.
        mNotificationManager.notify(-1, mBuilder.build());
    }
}