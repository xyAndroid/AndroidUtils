package com.xy.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {

    /**
     * Get Orientation
     *
     * @param context Context
     * @return Overall orientation of the screen.  May be one of portrait(1),landscape(2).
     */
    public static int getOrientation(Context context) {
        Resources resources = context.getResources();
        return resources.getConfiguration().orientation;
    }

    /**
     * @param activity 窗体
     * @return 获取除了虚拟键盘之后的窗体实际高度，用于PopuWindow的显示
     */
    public static int getRealHeightNotContainNavigationBar(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels - (NavigationBarUtils.hasNavigationBarCompat(activity) ? getNavigationBarHeight(activity) : 0);
    }

    /**
     * Get Screen Real Height
     *
     * @param context Context
     * @return Real Height
     */
    public static int getRealHeight(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * Get Screen Real Width
     *
     * @param context Context
     * @return Real Width
     */
    public static int getRealWidth(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            wm = activity.getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            return wm.getDefaultDisplay();
        }
        return null;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }


    /**
     * 获取导航栏的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context){
        Resources resources = context.getResources();
        int resourceId = 0;
        if (getOrientation(context) == Configuration.ORIENTATION_PORTRAIT) {
            resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        } else if (getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE) {
            resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        }
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * Get ActionBar Height
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getDisplayMetrics(context));
        }
        return 0;
    }

    /**
     * Get DisplayMetrics
     *
     * @param context Context for get Resources
     * @return DisplayMetrics
     */
    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Get Dpi
     */
    private static int getDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    /**
     * Get Density
     */
    private static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    /**
     * Get ScreenInfo
     * Note：Context is Activity Context
     */
    public static String getScreenInfo(Context context) {
        return " \n" +
                "--------ScreenInfo--------" + "\n" +
                "Screen Orientation : " + getOrientation(context) + " --- portrait(1),landscape(2)" + "\n" +
                "Screen Width : " + getScreenWidth(context) + "px\n" +
                "Screen RealWidth :" + getRealWidth(context) + "px\n" +
                "Screen Height: " + getScreenHeight(context) + "px\n" +
                "Screen RealHeight: " + getRealHeight(context) + "px\n" +
                "Screen StatusBar Height: " + getStatusBarHeight(context) + "px\n" +
                "Screen ActionBar Height: " + getActionBarHeight(context) + "px\n" +
                "Screen NavigationBar Height : " + getNavigationBarHeight(context) + "px\n" +
                "Screen Dpi: " + getDpi(context) + "\n" +
                "Screen Density: " + getDensity(context) + "\n" +
                "--------------------------";
    }

}
