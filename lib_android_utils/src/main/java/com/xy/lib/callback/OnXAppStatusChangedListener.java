package com.xy.lib.callback;

import android.app.Activity;

/**
 * @author XieYan
 * @date 2020/7/27 14:21
 */
public interface OnXAppStatusChangedListener {
    void onForeground(Activity activity);

    void onBackground(Activity activity);
}
