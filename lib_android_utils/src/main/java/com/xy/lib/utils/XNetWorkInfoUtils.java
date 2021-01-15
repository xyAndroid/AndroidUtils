package com.xy.lib.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresPermission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;


/**
 * 类说明：获取网络信息工具类
 *
 * @author yh
 * @date 2018/9/26
 */
public class XNetWorkInfoUtils {

    /**
     * 判断网络是否连接
     *
     * @param context Context
     * @return 网络是否连接
     */
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo mNetworkInfo = getNetworkInfo(context);
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    @IntDef({NetworkType.NETWORK_ETHERNET, NetworkType.NETWORK_WIFI,
            NetworkType.NETWORK_2G, NetworkType.NETWORK_3G,
            NetworkType.NETWORK_4G, NetworkType.NETWORK_5G, NetworkType.NETWORK_UNKNOWN, NetworkType.NETWORK_NO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {
        /**
         * 以太网
         */
        int NETWORK_ETHERNET = 1;
        int NETWORK_WIFI = 2;
        int NETWORK_3G = 3;
        int NETWORK_2G = 4;
        int NETWORK_4G = 5;
        int NETWORK_5G = 6;
        /**
         * 未知网络
         */
        int NETWORK_UNKNOWN = 7;
        /**
         * 没有网络
         */
        int NETWORK_NO = 8;
    }

    /**
     * 判断是否Wifi在线
     */
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    public static boolean isWifiConnect(Context mContext) {
        return getNetworkTypeForLink(mContext) == NetworkType.NETWORK_WIFI;
    }

    /**
     * 判断是否是手机网络
     */
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    public static boolean isMobile(Context mContext) {
        return getNetworkTypeForLink(mContext) != NetworkType.NETWORK_NO && getNetworkTypeForLink(mContext) != NetworkType.NETWORK_WIFI;
    }

    /**
     * 获取当前网络的类型
     */
    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    public static int getNetworkTypeForLink(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || !info.isAvailable()) {
            return NetworkType.NETWORK_NO;
        }
        if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return NetworkType.NETWORK_ETHERNET;
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetworkType.NETWORK_WIFI;
        }
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (info.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NetworkType.NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NetworkType.NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NetworkType.NETWORK_4G;
                case 20:
                    //5G判断条件，需要版本到android10才替换原生的常量表示
                    return NetworkType.NETWORK_5G;
                default://有机型返回16,17
                    //中国移动 联通 电信 三种3G制式
                    if (info.getSubtypeName().equalsIgnoreCase("TD-SCDMA") ||
                            info.getSubtypeName().equalsIgnoreCase("WCDMA") ||
                            info.getSubtypeName().equalsIgnoreCase("CDMA2000")) {
                        return NetworkType.NETWORK_3G;
                    } else {
                        return NetworkType.NETWORK_UNKNOWN;
                    }
            }
        }
        return NetworkType.NETWORK_UNKNOWN;
    }

    @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
    private static NetworkInfo getNetworkInfo(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        return manager.getActiveNetworkInfo();
    }

    /**
     * 注册网络状态监听
     *
     * @param listener
     */
    public static void registerNetworkStatusChangedListener(final OnBLNetworkStatusChangedListener listener) {
        NetworkChangedReceiver.getInstance().registerListener(listener);
    }

    /**
     * 取消注册网络状态变化监听
     *
     * @param listener
     */
    public static void unregisterNetworkStatusChangedListener(final OnBLNetworkStatusChangedListener listener) {
        NetworkChangedReceiver.getInstance().unregisterListener(listener);
    }

    /**
     * 是否有注册网络监听
     *
     * @param listener
     * @return
     */
    public static boolean isRegisteredNetworkStatusChangedListener(final OnBLNetworkStatusChangedListener listener) {
        return NetworkChangedReceiver.getInstance().isRegistered(listener);
    }


    private static class NetworkChangedReceiver {
        private List<OnBLNetworkStatusChangedListener> mListeners = new ArrayList<>();

        private NetworkChangedReceiver() {

        }

        private static class NetworkChangedReceiverInner {
            static NetworkChangedReceiver networkChangedReceiver = new NetworkChangedReceiver();
        }

        public static NetworkChangedReceiver getInstance() {
            return NetworkChangedReceiverInner.networkChangedReceiver;
        }

        void registerListener(final OnBLNetworkStatusChangedListener listener) {
            if (listener == null) {
                return;
            }
            XThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int preSize = mListeners.size();
                    mListeners.add(listener);
                    if (preSize == 0 && mListeners.size() == 1) {
                        register();
                    }
                }
            });
        }

        void unregisterListener(final OnBLNetworkStatusChangedListener listener) {
            if (listener == null) {
                return;
            }
            XThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int preSize = mListeners.size();
                    mListeners.remove(listener);
                    if (preSize == 1 && mListeners.size() == 0) {
                        unregister();
                    }
                }
            });
        }

        boolean isRegistered(final OnBLNetworkStatusChangedListener listener) {
            if (listener == null) return false;
            return mListeners.contains(listener);
        }

        private void register() {
            ConnectivityManager connectivityManager = (ConnectivityManager) XAndroidUtils.app().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//API 大于26时
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API 大于21时
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                NetworkRequest request = builder.build();
                connectivityManager.registerNetworkCallback(request, networkCallback);
            } else {//低版本
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                XAndroidUtils.app().registerReceiver(networkChangedBroadcastReceiver, intentFilter);
            }
        }

        private void unregister() {
            ConnectivityManager connectivityManager = (ConnectivityManager) XAndroidUtils.app().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//API 大于21时
                connectivityManager.unregisterNetworkCallback(networkCallback);
            } else {//低版本
                XAndroidUtils.app().unregisterReceiver(networkChangedBroadcastReceiver);
            }
        }


        private BroadcastReceiver networkChangedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    int netType = getNetworkTypeForLink(context);
                    networkStatusChangedCallback(netType);
                }
            }
        };

        private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                //网络已连接
                XLog.i("网络已连接");
                int netType = getNetworkTypeForLink(XAndroidUtils.app());
                networkStatusChangedCallback(netType);
            }

            @Override
            public void onLosing(Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                //在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                XLog.i("网络已断开");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                //网络丢失的回调
                XLog.i("网络已断开");
                networkStatusChangedCallback(NetworkType.NETWORK_NO);
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                //按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                XLog.i("无可用网络");
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                //按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                XLog.i("网络发生变化:" + networkCapabilities);
            }

            @Override
            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                //当建立网络连接时，回调连接的属性
                XLog.i("建立网络连接:" + linkProperties);
            }
        };

        private void networkStatusChangedCallback(@NetworkType int networkType) {
            for (OnBLNetworkStatusChangedListener listener : mListeners) {
                listener.onNetworkStatusChanged(networkType);
            }
        }

    }

    public interface OnBLNetworkStatusChangedListener {
        void onNetworkStatusChanged(@NetworkType int networkType);
    }
}
