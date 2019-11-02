package com.xy.lib.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

public class KeyboardUtils {
    /**
     * 软键盘高度变化监听
     * @param activity
     * @param keyboardHeightObserver
     */
    public static void registerKeyboardChangeListener(Activity activity, @NonNull KeyboardHeightProvider.KeyboardHeightObserver keyboardHeightObserver){
        KeyboardHeightProvider keyboardHeightProvider = new KeyboardHeightProvider(activity);
        keyboardHeightProvider.setKeyboardHeightObserver(keyboardHeightObserver);
        keyboardHeightProvider.start();
    }
}
