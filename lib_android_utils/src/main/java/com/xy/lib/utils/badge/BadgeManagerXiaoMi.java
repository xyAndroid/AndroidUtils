package com.xy.lib.utils.badge;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;


/**
 * 小米角标管理者
 *
 * @author wangjing
 * @date 2019/5/7 0007
 */
class BadgeManagerXiaoMi implements BadgeManager {
    @Override
    public void setNumber(Context context, int number) {
//        小米桌面badge跟随通知走
    }

    @Override
    public void clear(Context context) {
        BadgeHelper.clearAllNotification(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String init(Context context) {
        return BadgeHelper.initNotificationChannel(context);
    }

}
