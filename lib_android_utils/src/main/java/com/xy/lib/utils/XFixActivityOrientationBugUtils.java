package com.xy.lib.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 针对 Android 27 的情况进行处理
 * 横竖屏设置了方向会崩溃的问题
 *
 * https://www.cnblogs.com/liyiran/p/10362881.html
 *
 */
public class XFixActivityOrientationBugUtils {

    private static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 修复横竖屏 crash 的问题
     * @return
     */
    private static boolean fixOrientation(Activity activity){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void fixOrientationBug(Activity activity){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isTranslucentOrFloating(activity)) {
                fixOrientation(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
