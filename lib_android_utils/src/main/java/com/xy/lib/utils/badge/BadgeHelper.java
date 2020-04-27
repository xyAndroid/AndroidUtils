package com.xy.lib.utils.badge;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;


/**
 * 应用角标
 *
 * @author yaNing
 * @date 2019/7/12
 */
class BadgeHelper {

    private static final String CHANNEL_NAME = "普通消息";
    private static final String CHANNEL_ID = "defaultChannel";

    static String getLauncherClassName(Context context) {
        ComponentName launchComponent = getLauncherComponentName(context);
        if (launchComponent == null) {
            return "";
        } else {
            return launchComponent.getClassName();
        }
    }

    private static ComponentName getLauncherComponentName(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context
                .getPackageName());
        if (launchIntent != null) {
            return launchIntent.getComponent();
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static String initNotificationChannel(Context context) {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        NotificationManager manager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
        return CHANNEL_NAME;
    }

    static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
