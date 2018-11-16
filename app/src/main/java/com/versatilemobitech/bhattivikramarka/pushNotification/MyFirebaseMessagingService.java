package com.versatilemobitech.bhattivikramarka.pushNotification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.activities.SplashActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Excentd11 on 7/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
    }

    @Override
    public void zzm(Intent intent) {
        Log.d("title", "" + intent.getStringExtra("gcm.notification.body"));
        Log.d("body", "" + intent.getStringExtra("gcm.notification.title"));
        Log.d("page no", "" + intent.getStringExtra("gcm.notification.page_no"));
        Log.d("new one", "" + intent.getStringExtra("gcm.notification.largeIcon"));

        int color = 0x117B3D;

        Intent splashIntent = new Intent(this, SplashActivity.class);
        splashIntent.putExtra("page_no", intent.getStringExtra("gcm.notification.page_no") + "");
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, splashIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (isAppIsInBackground(getApplicationContext())) {
//            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Bitmap bitmap = getBitmapFromURL(intent.getStringExtra("gcm.notification.largeIcon"));
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.setBigContentTitle(intent.getStringExtra("gcm.notification.title"));
            bigPictureStyle.setSummaryText(Html.fromHtml(intent.getStringExtra("gcm.notification.body")).toString());
            bigPictureStyle.bigPicture(bitmap);

            Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo_push)
                    .setLargeIcon(rawBitmap)
                    .setColor(color)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setStyle(bigPictureStyle)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(intent.getStringExtra("gcm.notification.title"))
                    .setContentText(intent.getStringExtra("gcm.notification.body"))
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } else {

        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}