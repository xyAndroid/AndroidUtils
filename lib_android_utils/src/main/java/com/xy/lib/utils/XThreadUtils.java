package com.xy.lib.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author XieYan
 * @date 2020/7/27 14:27
 */
public class XThreadUtils {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainHandler() {
        return HANDLER;
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    //TODO 线程池相关工具
}
