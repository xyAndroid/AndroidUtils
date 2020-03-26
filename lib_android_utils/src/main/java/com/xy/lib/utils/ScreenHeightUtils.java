package com.xy.lib.utils;

import android.app.Activity;
import android.content.Context;

public class ScreenHeightUtils {

    public static int getScreenHeight(Context context) {
        int realHeight = ScreenInfoUtils.getRealHeight(context);

        if (realHeight == ScreenInfoUtils.getScreenHeight(context) + ScreenInfoUtils.getNavigationBarHeight(context)) {
            return checkNavigationBarShow(context) ? ScreenInfoUtils.getScreenHeight(context) : ScreenInfoUtils.getScreenHeight(context) + ScreenInfoUtils.getNavigationBarHeight(context);
        } else if (realHeight == ScreenInfoUtils.getScreenHeight(context) + ScreenInfoUtils.getNavigationBarHeight(context) + ScreenInfoUtils.getStatusBarHeight(context)) {
            return checkNavigationBarShow(context) ? ScreenInfoUtils.getScreenHeight(context) + ScreenInfoUtils.getStatusBarHeight(context) : ScreenInfoUtils.getScreenHeight(context) + ScreenInfoUtils.getNavigationBarHeight(context) + ScreenInfoUtils.getStatusBarHeight(context);
        } else if (realHeight == ScreenInfoUtils.getScreenHeight(context)) {
            return checkNavigationBarShow(context) ? ScreenInfoUtils.getScreenHeight(context) - ScreenInfoUtils.getNavigationBarHeight(context) : ScreenInfoUtils.getScreenHeight(context);
        } else if(realHeight == ScreenInfoUtils.getRealHeightNotContainNavigationBar((Activity) context) + ScreenInfoUtils.getNavigationBarHeight(context)){
            return  checkNavigationBarShow(context) ? ScreenInfoUtils.getRealHeightNotContainNavigationBar((Activity) context) : realHeight;
        } else {
            return realHeight;
        }
    }

    private static boolean checkNavigationBarShow(Context context){
        return NavigationBarUtils.hasNavigationBarCompat(context);
    }

}
