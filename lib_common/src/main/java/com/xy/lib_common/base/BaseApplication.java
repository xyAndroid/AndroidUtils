package com.xy.lib_common.base;

import android.app.Application;

import com.xy.simplerouter.SimpleRouter;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SimpleRouter.getInstance().init(this);
    }
}
