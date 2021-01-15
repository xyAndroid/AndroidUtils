package com.xy.lib.utils.badge;


import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xy.lib.utils.XManufacturerInfoUtils;


/**
 * @author wangjing on 2019/5/7 0007
 * description:
 */
public class XBadgeUtil {

    private static final BadgeManager BADGE_MANGER;

    static {
        if (XManufacturerInfoUtils.isHuawei()) {
            BADGE_MANGER = new BadgeManagerHuaWei();
        } else if (XManufacturerInfoUtils.isXiaomi()) {
            BADGE_MANGER = new BadgeManagerXiaoMi();
        } else if (XManufacturerInfoUtils.isSamsung()) {
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
