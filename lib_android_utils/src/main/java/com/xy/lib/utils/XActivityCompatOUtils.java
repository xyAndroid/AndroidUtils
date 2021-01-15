package com.xy.lib.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 针对 Android 28 的情况进行处理
 * 横竖屏设置了方向会崩溃的问题
 * <p>
 * https://www.cnblogs.com/liyiran/p/10362881.html
 *
 * 使用方式：
 * 1.在基类Activity中调用fixOrientationBug（）
 * 2.在基类Activity中调用isTranslucentOrFloatingCompatO（）
 * @author xieyan
 * @date 2020/5/8
 */
public class XActivityCompatOUtils {

    public XActivityCompatOUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 修复横竖屏 crash 的问题
     *
     * @return
     */
    private static boolean fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 一般用于基类Activity的onCreate()方法中，在super.onCreate()之前调用
     *
     * @param activity 基类Activity
     */
    public static void fixOrientationBug(Activity activity) {
        try {
            if (isTranslucentOrFloatingCompatO(activity)) {
                fixOrientation(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断设备是否是Android O系统且Activity是透明主题
     * 一般用于基类Activity的setRequestedOrientation()中
     * 示例：
     * <p>
     * public void setRequestedOrientation(int requestedOrientation){
     * if（isTranslucentOrFloatingCompatO（this））{
     * return;
     * }
     * super.setRequestedOrientation(requestedOrientation)
     * }
     * </>
     *
     * @param activity 基类的Activity
     */
    public static boolean isTranslucentOrFloatingCompatO(Activity activity) {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating(activity);
    }
}
