package com.xy.lib.utils.badge;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;


/**
 * 华为角标
 *
 * @author wangjing
 * @date 2019/5/7
 */
class BadgeManagerHuaWei implements BadgeManager {
    @Override
    public void setNumber(Context context, int number) {
        try {
            String launchClassName = BadgeHelper.getLauncherClassName(context);
            if (TextUtils.isEmpty(launchClassName)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", number);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher" +
                    ".settings/badge/"), "change_badge", null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear(Context context) {
        setNumber(context, 0);
    }

    @Override
    public String init(Context context) {
        return "";
    }

}
