package com.xy.lib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 屏幕工具类
 *
 * @author yh
 * @date 2019/03/15
 */
public class XDisplayUtils {

    private XDisplayUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static int dip2px(float dipFloat) {
        return dip2px(XAndroidUtils.app(), dipFloat);
    }

    public static int dip2px(Context context, float dipFloat) {
        float f = getDisplayMetrics(context).density;
        return (int) (dipFloat * f + 0.5F);
    }

    public static int px2dip(float pxFloat) {
        return px2dip(XAndroidUtils.app(), pxFloat);
    }

    public static int px2dip(Context context, float pxFloat) {
        float f = getDisplayMetrics(context).density;
        return (int) (pxFloat / f + 0.5F);
    }

    public static int px2sp(float pxValue) {
        return px2sp(XAndroidUtils.app(), pxValue);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = getDisplayMetrics(context).scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        return sp2px(XAndroidUtils.app(), spValue);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = getDisplayMetrics(context).scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
