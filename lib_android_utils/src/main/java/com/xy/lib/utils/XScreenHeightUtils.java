package com.xy.lib.utils;

import android.app.Activity;
import android.content.Context;

public class XScreenHeightUtils {

    public static int getScreenHeight(Context context) {
        int realHeight = XScreenInfoUtils.getRealHeight(context);

        if (realHeight == XScreenInfoUtils.getScreenHeight(context) + XScreenInfoUtils.getNavigationBarHeight(context)) {
            return checkNavigationBarShow(context) ? XScreenInfoUtils.getScreenHeight(context) : XScreenInfoUtils.getScreenHeight(context) + XScreenInfoUtils.getNavigationBarHeight(context);
        } else if (realHeight == XScreenInfoUtils.getScreenHeight(context) + XScreenInfoUtils.getNavigationBarHeight(context) + XScreenInfoUtils.getStatusBarHeight(context)) {
            return checkNavigationBarShow(context) ? XScreenInfoUtils.getScreenHeight(context) + XScreenInfoUtils.getStatusBarHeight(context) : XScreenInfoUtils.getScreenHeight(context) + XScreenInfoUtils.getNavigationBarHeight(context) + XScreenInfoUtils.getStatusBarHeight(context);
        } else if (realHeight == XScreenInfoUtils.getScreenHeight(context)) {
            return checkNavigationBarShow(context) ? XScreenInfoUtils.getScreenHeight(context) - XScreenInfoUtils.getNavigationBarHeight(context) : XScreenInfoUtils.getScreenHeight(context);
        } else if(realHeight == XScreenInfoUtils.getRealHeightNotContainNavigationBar((Activity) context) + XScreenInfoUtils.getNavigationBarHeight(context)){
            return  checkNavigationBarShow(context) ? XScreenInfoUtils.getRealHeightNotContainNavigationBar((Activity) context) : realHeight;
        } else {
            return realHeight;
        }
    }

    private static boolean checkNavigationBarShow(Context context){
        return XNavigationBarUtils.hasNavigationBarCompat(context);
    }

}
