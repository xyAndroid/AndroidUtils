package com.xy.lib.utils.badge;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**   
 * 
 *@author yaNing
 *@date 2019/7/12
 */
class BadgeManagerDefault implements BadgeManager{
    @Override
    public void setNumber(Context context, int number) {
//       默认不支持
    }

    @Override
    public void clear(Context context) {
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String init(Context context) {
        return BadgeHelper.initNotificationChannel(context);
    }
}
