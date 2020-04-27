package com.xy.lib.utils.badge;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author yaNing
 * @date 2019/7/12
 */
interface BadgeManager {

    /**
     * 设置数量
     *
     * @param context Context
     * @param number  角标数
     */
    void setNumber(Context context, int number);

    /**
     * 清除角标
     *
     * @param context Context
     */
    void clear(Context context);

    /**
     * 初始化
     *
     * @param context Context
     * @return 初始化信息
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    String init(Context context);
}
