package com.xy.lib.utils.badge;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


/**
 * create by wangjing on 2019/5/8 0008
 * description:
 */
class BadgeManagerSamsung implements BadgeManager {
    @Override
    public void setNumber(Context context, int number) {
        try {
            String launcherClassName = BadgeHelper.getLauncherClassName(context);
            if (TextUtils.isEmpty(launcherClassName)) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", number);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
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
