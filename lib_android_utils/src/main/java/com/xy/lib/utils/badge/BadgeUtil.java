package com.xy.lib.utils.badge;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xy.lib.utils.ManufacturerInfoUtils;


/**
 * @author wangjing on 2019/5/7 0007
 * description:
 */
public class BadgeUtil {

    private static final BadgeManager BADGE_MANGER;

    static {
        if (ManufacturerInfoUtils.isHuawei()) {
            BADGE_MANGER = new BadgeManagerHuaWei();
        } else if (ManufacturerInfoUtils.isXiaomi()) {
            BADGE_MANGER = new BadgeManagerXiaoMi();
        } else if (ManufacturerInfoUtils.isSamsung()) {
            BADGE_MANGER = new BadgeManagerSamsung();
        } else {
            BADGE_MANGER = new BadgeManagerDefault();
        }
    }

    public static void setNumber(Context context, int number) {
        BADGE_MANGER.setNumber(context.getApplicationContext(), number);
    }

    public static void clear(Context context) {
        BADGE_MANGER.clear(context.getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String init(Context context) {
        return BADGE_MANGER.init(context.getApplicationContext());
    }
}
