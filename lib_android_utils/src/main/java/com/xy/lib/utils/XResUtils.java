package com.xy.lib.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * 获取Android相关资源
 *
 * @author yh
 * @date 2020/01/10
 */
public class XResUtils {

    private XResUtils() {
    }

    /**
     * Returns a drawable object associated with a particular resource ID.
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(XAndroidUtils.app(), id);
    }

    /**
     * Returns a color associated with a particular resource ID
     */
    @ColorInt
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(XAndroidUtils.app(), id);
    }

    public static ColorStateList getColorStateList(@ColorRes int id){
        return ContextCompat.getColorStateList(XAndroidUtils.app(),id);
    }


    public static float getDimension(int resId) {
        return XAndroidUtils.app().getResources().getDimension(resId);
    }

    public static float getDimensionPixelSize(int resId) {
        return XAndroidUtils.app().getResources().getDimensionPixelSize(resId);
    }


    public static String getString(@StringRes int resId) {
        return XAndroidUtils.app().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return XAndroidUtils.app().getString(resId, formatArgs);
    }

    public static String[] getStringArray(@ArrayRes int id){
        return XAndroidUtils.app().getResources().getStringArray(id);
    }

    public static int getId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "id", XAndroidUtils.app().getPackageName());
    }

    public static int getStringId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "string", XAndroidUtils.app().getPackageName());
    }

    public static int getColorId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "color", XAndroidUtils.app().getPackageName());
    }

    public static int getDimenId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "dimen", XAndroidUtils.app().getPackageName());
    }

    public static int getDrawableId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "drawable", XAndroidUtils.app().getPackageName());
    }

    public static int getMipmapId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "mipmap", XAndroidUtils.app().getPackageName());
    }

    public static int getLayoutId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "layout", XAndroidUtils.app().getPackageName());
    }

    public static int getStyleId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "style", XAndroidUtils.app().getPackageName());
    }

    public static int getAnimId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "anim", XAndroidUtils.app().getPackageName());
    }

    public static int getMenuId(String name) {
        return XAndroidUtils.app().getResources().getIdentifier(name, "menu", XAndroidUtils.app().getPackageName());
    }
}
