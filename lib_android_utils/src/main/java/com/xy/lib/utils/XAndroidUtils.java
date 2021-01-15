package com.xy.lib.utils;

import android.app.Application;

/**
 * 全局的Application
 *
 * @author yh
 * @date 2019/12/13
 */
public class XAndroidUtils {

    private XAndroidUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static Application mApplication;


    public static void init(Application application) {
        mApplication = application;
    }

    public static Application app() {
        if (mApplication == null) {
            throw new NullPointerException("XAndroidUtils Not Initialized");
        }
        return mApplication;
    }
}
