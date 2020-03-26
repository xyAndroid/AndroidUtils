package com.xy.lib.utils;

import android.os.Build;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 手机制造商信息工具类
 *
 * @author yh
 * @date 2018/12/15
 */
public class ManufacturerInfoUtils {

    private static final String TAG = ManufacturerInfoUtils.class.getSimpleName();

    /**
     * 华为手机
     */
    private static final String HUAWEI = "huawei";
    /**
     * VIVO手机
     */
    private static final String VIVO = "vivo";
    /**
     * 小米手机
     */
    private static final String XIAOMI = "xiaomi";
    /**
     * OPPO手机
     */
    private static final String OPPO = "oppo";
    /**
     * 魅族手机
     */
    private static final String MEIZU = "meizu";
    /**
     * 三星手机
     */
    private static final String SAMSUNG = "samsung";
    /**
     * 其他手机
     */
    private static final String OTHER = "other";

    @StringDef({HUAWEI, VIVO, XIAOMI, OPPO, MEIZU, SAMSUNG, OTHER})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Manufacturer {

    }

    /**
     * 制造商为huawei的标识
     */
    private static final String MANUFACTURER_HUAWEI = "HUAWEI";
    /**
     * 制造商为XIAOMI的标识
     */
    private static final String MANUFACTURER_XIAOMI = "XIAOMI";
    /**
     * 制造商为VIVO的标识
     */
    private static final String MANUFACTURER_VIVO = "VIVO";
    /**
     * 制造商为OPPO的标识
     */
    private static final String MANUFACTURER_OPPO = "OPPO";
    /**
     * 制造商为魅族的标识
     */
    private static final String MANUFACTURER_MEIZU = "Meizu";
    /**
     * 制造商为三星的标识
     */
    private static final String MANUFACTURER_SAMSUNG = "samsung";

    @Manufacturer
    public static String getCurrentDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase(MANUFACTURER_HUAWEI)) {
            return HUAWEI;
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_XIAOMI)) {
            return XIAOMI;
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_VIVO)) {
            return VIVO;
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_OPPO)) {
            return OPPO;
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_MEIZU)) {
            return MEIZU;
        } else if (manufacturer.equalsIgnoreCase(MANUFACTURER_SAMSUNG)) {
            return SAMSUNG;
        } else {
            return OTHER;
        }
    }

    /**
     * 判断是否为Vivo手机
     */
    public static boolean isVivo() {
        return VIVO.equals(getCurrentDeviceManufacturer());
    }

    /**
     * 判断是否为OPPO手机
     */
    public static boolean isOppo() {
        return OPPO.equalsIgnoreCase(getCurrentDeviceManufacturer());
    }

    /**
     * 判断是否为小米手机
     */
    public static boolean isXiaomi() {
        return XIAOMI.equalsIgnoreCase(getCurrentDeviceManufacturer());
    }

    /**
     * 判断是否为华为手机
     */
    public static boolean isHuawei() {
        return HUAWEI.equalsIgnoreCase(getCurrentDeviceManufacturer());
    }

    /**
     * 判断是否为魅族手机
     */
    public static boolean isMeizu() {
        return MEIZU.equalsIgnoreCase(getCurrentDeviceManufacturer());
    }

    /**
     * 判断是否为三星手机
     */
    public static boolean isSamsung(){
        return SAMSUNG.equalsIgnoreCase(getCurrentDeviceManufacturer());
    }

}
