package com.xy.lib.utils;

import android.util.Log;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author XieYan
 * @date 2020/7/25 16:31
 */
public class XLog {
    private static String TAG = "STL";
    private static @Level int level;

    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数

    @IntDef({Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Level {

    }

    public static void init(String TAG) {
        XLog.TAG = TAG;
    }

    public static void init(@Level int level, String TAG) {
        XLog.TAG = TAG;
        XLog.level = level;
    }


    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")]:  ");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement sElements) {
        className = sElements.getFileName();
        methodName = sElements.getMethodName();
        lineNumber = sElements.getLineNumber();
    }


    public static void i(String msg) {
        log(Log.INFO, msg);
    }

    public static void i(String tag, String msg) {
        log(Log.INFO, tag, msg);
    }

    public static void e(String msg) {
        log(Log.ERROR, msg);
    }

    public static void e(String tag, String msg) {
        log(Log.ERROR, tag, msg);
    }

    public static void d(String msg) {
        log(Log.DEBUG, msg);
    }

    public static void d(String tag, String msg) {
        log(Log.DEBUG, tag, msg);
    }

    public static void w(String msg) {
        log(Log.WARN, msg);
    }

    public static void w(String tag, String msg) {
        log(Log.WARN, tag, msg);
    }


    protected static void log(int priority, String msg) {
        log(priority, TAG, msg, 3);
    }

    protected static void log(int priority, String tag, String msg) {
        log(priority, tag, msg, 3);
    }

    protected static void log(int priority, String tag, String msg, int index) {
        if (!isEnable(priority)) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace()[index]);
        Log.println(priority, tag, createLog(msg));
    }


    public static boolean isEnable(int priority) {
        return level <= priority;
    }

}
