package com.xy.lib.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.xy.lib.callback.XConsumer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author XieYan
 * @date 2020/8/3 13:49
 */
public class XNotificationUtils {

    @IntDef({
            NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, NotificationManagerCompat.IMPORTANCE_NONE,
            NotificationManagerCompat.IMPORTANCE_MIN, NotificationManagerCompat.IMPORTANCE_LOW, NotificationManagerCompat.IMPORTANCE_DEFAULT, NotificationManagerCompat.IMPORTANCE_HIGH
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {}


    /**
     * Return whether the notifications enabled.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean areNotificationsEnabled() {
        return NotificationManagerCompat.from(XAndroidUtils.app()).areNotificationsEnabled();
    }

    /**
     * Post a notification to be shown in the status bar.
     *
     * @param id       An identifier for this notification.
     * @param consumer The consumer of create the builder of notification.
     */
    public static NotificationManagerCompat notify(int id, XConsumer<NotificationCompat.Builder> consumer) {
        return notify(null, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     *
     * @param tag      A string identifier for this notification.  May be {@code null}.
     * @param id       An identifier for this notification.
     * @param consumer The consumer of create the builder of notification.
     */
    public static NotificationManagerCompat notify(String tag, int id, XConsumer<NotificationCompat.Builder> consumer) {
        return notify(tag, id, ChannelConfig.DEFAULT_CHANNEL_CONFIG, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     *
     * @param id            An identifier for this notification.
     * @param channelConfig The notification channel of config.
     * @param consumer      The consumer of create the builder of notification.
     */
    public static NotificationManagerCompat notify(int id, ChannelConfig channelConfig, XConsumer<NotificationCompat.Builder> consumer) {
        return notify(null, id, channelConfig, consumer);
    }

    /**
     * Post a notification to be shown in the status bar.
     *
     * @param tag           A string identifier for this notification.  May be {@code null}.
     * @param id            An identifier for this notification.
     * @param channelConfig The notification channel of config.
     * @param consumer      The consumer of create the builder of notification.
     */
    public static NotificationManagerCompat notify(String tag, int id, ChannelConfig channelConfig, XConsumer<NotificationCompat.Builder> consumer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) XAndroidUtils.app().getSystemService(Context.NOTIFICATION_SERVICE);
            //noinspection ConstantConditions
            nm.createNotificationChannel(channelConfig.getNotificationChannel());
        }

        NotificationManagerCompat nmc = NotificationManagerCompat.from(XAndroidUtils.app());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(XAndroidUtils.app());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelConfig.mNotificationChannel.getId());
        }
        consumer.accept(builder);

        nmc.notify(tag, id, builder.build());

        return nmc;
    }


    /**
     * Cancel The notification.
     *
     * @param tag The tag for the notification will be cancelled.
     * @param id  The identifier for the notification will be cancelled.
     */
    public static void cancel(String tag, final int id) {
        NotificationManagerCompat.from(XAndroidUtils.app()).cancel(tag, id);
    }

    /**
     * Cancel The notification.
     *
     * @param id The identifier for the notification will be cancelled.
     */
    public static void cancel(final int id) {
        NotificationManagerCompat.from(XAndroidUtils.app()).cancel(id);
    }

    /**
     * Cancel all of the notifications.
     */
    public static void cancelAll() {
        NotificationManagerCompat.from(XAndroidUtils.app()).cancelAll();
    }



    public static class ChannelConfig {

        public static final ChannelConfig DEFAULT_CHANNEL_CONFIG = new ChannelConfig(
                XAndroidUtils.app().getPackageName(), XAndroidUtils.app().getPackageName(), NotificationManagerCompat.IMPORTANCE_DEFAULT
        );

        private NotificationChannel mNotificationChannel;

        public ChannelConfig(String id, CharSequence name, @Importance int importance) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel = new NotificationChannel(id, name, importance);
            }
        }

        public NotificationChannel getNotificationChannel() {
            return mNotificationChannel;
        }

        /**
         * Sets whether or not notifications posted to this channel can interrupt the user in
         * {@link NotificationManager.Policy#INTERRUPTION_FILTER_PRIORITY} mode.
         * <p>
         * Only modifiable by the system and notification ranker.
         */
        public ChannelConfig setBypassDnd(boolean bypassDnd) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setBypassDnd(bypassDnd);
            }
            return this;
        }

        /**
         * Sets the user visible description of this channel.
         *
         * <p>The recommended maximum length is 300 characters; the value may be truncated if it is too
         * long.
         */
        public ChannelConfig setDescription(String description) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setDescription(description);
            }
            return this;
        }

        /**
         * Sets what group this channel belongs to.
         * <p>
         * Group information is only used for presentation, not for behavior.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}, unless the
         * channel is not currently part of a group.
         *
         * @param groupId the id of a group created by
         *                {@link NotificationManager#createNotificationChannelGroup)}.
         */
        public ChannelConfig setGroup(String groupId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setGroup(groupId);
            }
            return this;
        }

        /**
         * Sets the level of interruption of this notification channel.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         *
         * @param importance the amount the user should be interrupted by
         *                   notifications from this channel.
         */
        public ChannelConfig setImportance(@Importance int importance) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setImportance(importance);
            }
            return this;
        }

        /**
         * Sets the notification light color for notifications posted to this channel, if lights are
         * {@link NotificationChannel#enableLights(boolean) enabled} on this channel and the device supports that feature.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         */
        public ChannelConfig setLightColor(int argb) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setLightColor(argb);
            }
            return this;
        }

        /**
         * Sets whether notifications posted to this channel appear on the lockscreen or not, and if so,
         * whether they appear in a redacted form. See e.g. {@link Notification#VISIBILITY_SECRET}.
         * <p>
         * Only modifiable by the system and notification ranker.
         */
        public ChannelConfig setLockscreenVisibility(int lockscreenVisibility) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setLockscreenVisibility(lockscreenVisibility);
            }
            return this;
        }

        /**
         * Sets the user visible name of this channel.
         *
         * <p>The recommended maximum length is 40 characters; the value may be truncated if it is too
         * long.
         */
        public ChannelConfig setName(CharSequence name) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setName(name);
            }
            return this;
        }

        /**
         * Sets whether notifications posted to this channel can appear as application icon badges
         * in a Launcher.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         *
         * @param showBadge true if badges should be allowed to be shown.
         */
        public ChannelConfig setShowBadge(boolean showBadge) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setShowBadge(showBadge);
            }
            return this;
        }

        /**
         * Sets the sound that should be played for notifications posted to this channel and its
         * audio attributes. Notification channels with an {@link NotificationChannel#getImportance() importance} of at
         * least {@link NotificationManager#IMPORTANCE_DEFAULT} should have a sound.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         */
        public ChannelConfig setSound(Uri sound, AudioAttributes audioAttributes) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setSound(sound, audioAttributes);
            }
            return this;
        }

        /**
         * Sets the vibration pattern for notifications posted to this channel. If the provided
         * pattern is valid (non-null, non-empty), will {@link NotificationChannel#enableVibration(boolean)} enable
         * vibration} as well. Otherwise, vibration will be disabled.
         * <p>
         * Only modifiable before the channel is submitted to
         * {@link NotificationManager#createNotificationChannel(NotificationChannel)}.
         */
        public ChannelConfig setVibrationPattern(long[] vibrationPattern) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel.setVibrationPattern(vibrationPattern);
            }
            return this;
        }
    }

}
