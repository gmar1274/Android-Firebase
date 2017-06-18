package app.reservation.acbasoftare.com.reservation.App_Services;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.CustomFBProfile;
import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2016-11-12.
 */
public class Notification extends BroadcastReceiver {
    private  Activity a;
    private CustomFBProfile prof;

    public static void createNotification(MainActivity a, CustomFBProfile profile, IMessagingMetaData meta, HashMap<String, String> loc) {
        // SimpleDateFormat sdf = new SimpleDateFormat("H:mm a");
        //  String time = sdf.format(new Date());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(a)
                        .setSmallIcon(R.drawable.acba)
                        .setContentTitle("New Message")
                        .setContentText(meta.getName().toUpperCase()).setTicker("New Message!!");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        mBuilder.setStyle(inboxStyle);
        mBuilder.setVibrate(new long[]{2000, 2000, 1000});
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = a.startMessagingActivityFromAnywhereInApp(profile, meta, loc);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(a);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) a.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    public Notification(Activity a, CustomFBProfile prof) {
        this.prof = prof;
        this.a = a;
    }

    private static void createNotification(Activity a, CustomFBProfile prof) {
        // SimpleDateFormat sdf = new SimpleDateFormat("H:mm a");
        //  String time = sdf.format(new Date());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(a)
                        .setSmallIcon(R.drawable.acba)
                        .setContentTitle("New Message")
                        .setTicker("New Message!!");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        mBuilder.setStyle(inboxStyle);
        mBuilder.setVibrate(new long[]{2000, 500, 500, 250});
        mBuilder.setLights(Color.YELLOW, 3000, 3000);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(a, LoginActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(a);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LoginActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) a.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void begin(int sec) {
        Intent i = new Intent(a,Notification.class);
        PendingIntent pi = PendingIntent.getBroadcast(a,0,i,0);
        AlarmManager am = (AlarmManager)a.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1000*sec,pi);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyService");
        wl.acquire();

        Toast.makeText(context,"In my CUSTOM service",Toast.LENGTH_LONG).show();
        wl.release();

    }
    public Notification(){

    }
}