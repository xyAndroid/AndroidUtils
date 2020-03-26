package com.xy.lib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;


/**
 * 判断是否有NavigationBar显示
 * <p>
 * 0:通过厂商的指定字段进行区分
 * 1:通过屏幕高度和屏幕真实高度进行区分
 * 2：NavigationBar真实高度进行区分（Activity 下的 StatusBar  Content  NavigationBar，但会和Activity耦合）
 * <p>
 * 注意事项：小米手机上，有导航栏模式下创建的Activity,切换为全屏手势，需要重新计算相关视图的高度(若有需要)
 * 默认值取-1是因为旧版本手机没有这个相关导航栏手势字段
 * @author yh
 * @date 2019/1/9
 */
public class NavigationBarUtils {

    /**
     * 判断NavigationBar是否显示
     */
    public static boolean hasNavigationBarCompat(Context context) {
        if (ManufacturerInfoUtils.isVivo()) {
            return hasNavigationBarCompatVivo(context);
        } else if (ManufacturerInfoUtils.isXiaomi()) {
            return hasNavigationBarCompatXiaomi(context);
        } else if (ManufacturerInfoUtils.isSamsung()) {
            return hasNavigationBarCompatSumsung(context);
        } else if (ManufacturerInfoUtils.isHuawei()) {
            return hasNavigationBarCompactHuawei(context);
        } else {
            return hasNavigationBarCommon(context);
        }
    }


    /**
     * 判断NavigationBar是否显示（一般方法）
     */
    private static boolean hasNavigationBarCommon(Context context) {
        if (ScreenInfoUtils.getOrientation(context) == 1) {
            int distance = ScreenInfoUtils.getRealHeight(context) - ScreenInfoUtils.getScreenHeight(context);
            //portrait
            return distance > 0 && distance != ScreenInfoUtils.getStatusBarHeight(context);
        } else {
            //landscape
            return (ScreenInfoUtils.getRealWidth(context) - ScreenInfoUtils.getScreenWidth(context)) > 0;
        }
    }


    /**
     * 判断NavigationBar是否显示(适配Vivo手机)
     */
    private static boolean hasNavigationBarCompatVivo(Context context) {
        final String navigationGesture = "navigation_gesture_on";
        final int navigationGestureOff = 0;
        int val = Settings.Secure.getInt(context.getContentResolver(), navigationGesture, -1);
        return val == navigationGestureOff;
    }

    /**
     * 判断NavigationBar是否显示(适配Sumsung手机)
     */
    private static boolean hasNavigationBarCompatSumsung(Context context) {
        // mKey = navigation_bar_gesture_while_hidden  mIntValue = 0{导航按钮模式}  mIntValue = 1{全屏手势模式}
        // content://settings/global/navigation_bar_gesture_while_hidden
        final String navigationGesture = "navigation_bar_gesture_while_hidden";
        final int navigationGestureOff = 0;
        int val = Settings.Global.getInt(context.getContentResolver(), navigationGesture, -10000);
        return val == navigationGestureOff;
    }

    /**
     * 判断Sumsung手机全屏手势模式下开启手势提示
     */
    public static boolean hasNavigationBarCompactSumsungGestureTip(Context context) {
        // mKey = navigation_bar_gesture_hint, mIntValue = 0{全屏手势模式下手势提示关闭}  mIntValue = 1{全屏手势模式下手势提示开启}
        // content://settings/global/navigation_bar_gesture_hint
        final String navigationGestureTip = "navigation_bar_gesture_hint";
        final int navigationGestureTipOn = 1;
        int val = Settings.Global.getInt(context.getContentResolver(), navigationGestureTip, navigationGestureTipOn);
        return val == navigationGestureTipOn;
    }

    /**
     * 判断小米全面屏NavigationBar是否显示
     * 1.-1 表示非全面屏手机.因为无法检测到这个配置信息
     * 2.0 表示全面屏手机，开启了全面屏
     * 3.1 表示全面屏手机，开启了虚拟键盘
     */
    private static boolean hasNavigationBarCompatXiaomi(Context context) {
        // mKey = force_fsg_nav_bar，  mIntValue = 0{经典导航键，虚拟键盘}  mIntValue = 1{全面屏手势}
        // content://settings/global/force_fsg_nav_bar
        final String navigationGesture = "force_fsg_nav_bar";
        final int navigationGestureOff = 0;
        int val = Settings.Global.getInt(context.getContentResolver(), navigationGesture, -1);
        return val == navigationGestureOff;
    }


    private static boolean hasNavigationBarCompactHuawei(Context context) {
        // mKey = secure_gesture_navigation  mIntValue = 0{屏幕内三键导航}     mIntValue = 1{全面屏手势 手势导航}
        // mKey = navigationbar_is_min mIntValue = 0 {屏幕内三键导航模式下导航键可隐藏} mIntValue = 1{屏幕内三键导航模式下导航键不隐藏}
        // 华为手机开启屏幕内三键导航之后，还可开启导航条的迷你模式
        //content://settings/secure/secure_gesture_navigation
        //content://settings/global/navigationbar_is_min
        final String navigationGesture = "secure_gesture_navigation";
        final String navigationBarIsMIn = "navigationbar_is_min";
        final int navigationGestureOff = 0;
        final int navigationBarMInOpen = 1;
        int val = Settings.Secure.getInt(context.getContentResolver(), navigationGesture, navigationGestureOff);
        int valMin = Settings.Global.getInt(context.getContentResolver(), navigationBarIsMIn, 0);
        return val == navigationGestureOff && valMin == navigationBarMInOpen;


    }

    /**
     * 开启全屏手势后，二外的手势提示高度
     * 目前只有三星手机增加了手势提示高度，其他机型默认为0
     * @param context   上下文
     * @return  手势提示高度值
     */
    public static int getNavigationBarGestureTipHeight(Context context){
        if (ManufacturerInfoUtils.isSamsung()){
            if (hasNavigationBarCompactSumsungGestureTip(context)){
                return getNavigationBarGestureTipHeightCompactSumsung(context);
            }else {
                return 0;
            }
        }else {
            return 0;
        }
    }


    /**
     * Get NavigationBar Sumsung Gesture Tip Height
     * 获取三星手机开启全屏手势后  开启手势提示的高度
     * 机型：SM-G8850
     */
    public static int getNavigationBarGestureTipHeightCompactSumsung(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height_for_gesture_hint", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


}
