package com.xy.lib.helper;

import android.os.CountDownTimer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XieYan
 * @date 2020/4/26 19:18
 */
public class CountDownTimerHelper {
    private final static long DEFAULT_MILLIS_IN_FUTURE = 60 * 1000;
    private final static long DEFAULT_COUNT_DOWN_INTERVAL = 1000;
    private static CountDownTimerHelper instance;
    private Map<String, MyCountDownTimer> countDownTimerMap;
    private Map<String, CountDownListener> countDownListenerMap;
    private Map<String, Long> millisUntilFinishedMap;


    private CountDownTimerHelper() {
        countDownTimerMap = new HashMap<>();
        countDownListenerMap = new HashMap<>();
        millisUntilFinishedMap = new HashMap<>();
    }

    public static CountDownTimerHelper getInstance() {
        if (instance == null) {
            instance = CountDownTimerHelperInner.countDownTimerHelper;
        }
        return instance;
    }

    static class CountDownTimerHelperInner {
        static CountDownTimerHelper countDownTimerHelper = new CountDownTimerHelper();
    }

    /**
     * 维持全局倒计时
     * 需要调用设置监听方法{@link #setCountDownListener(String key, CountDownListener countDownListener)}
     *
     * @param key 唯一key
     */
    public void start(String key) {
        start(key, DEFAULT_MILLIS_IN_FUTURE, DEFAULT_COUNT_DOWN_INTERVAL);
    }

    /**
     * 维持全局倒计时
     * 需要调用设置监听方法{@link #setCountDownListener(String key, CountDownListener countDownListener)}
     *
     * @param key               唯一key
     * @param millisInFuture    总计时的时间
     * @param countDownInterval 计时的速度
     */
    public void start(String key, long millisInFuture, long countDownInterval) {
        start(key, millisInFuture, countDownInterval, null);
    }

    /**
     * 倒计时只维持在当前页面
     *
     * @param key               唯一key
     * @param countDownListener 倒计时监听
     */
    public void start(String key, CountDownListener countDownListener) {
        start(key, DEFAULT_MILLIS_IN_FUTURE, DEFAULT_COUNT_DOWN_INTERVAL, countDownListener);
    }

    /**
     * 倒计时只维持在当前页面
     *
     * @param key               唯一key
     * @param millisInFuture    总计时的时间
     * @param countDownInterval 计时的速度
     * @param countDownListener 倒计时监听
     */
    public void start(String key, long millisInFuture, long countDownInterval, CountDownListener countDownListener) {
        MyCountDownTimer countDownTimer = countDownTimerMap.get(key);

        if (countDownListener != null) {
            //倒计时只维持在当前页面
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimerMap.remove(key);
            }
            countDownTimer = new MyCountDownTimer(key, millisInFuture, countDownInterval);
            countDownTimerMap.put(key, countDownTimer);
            setCountDownListener(key, countDownListener);
        } else {
            //倒计时作用于全局
            if (countDownTimer == null) {
                countDownTimer = new MyCountDownTimer(key, millisInFuture, countDownInterval);
                countDownTimerMap.put(key, countDownTimer);
            } else {
                return;
            }

        }

        if (millisUntilFinishedMap != null) {
            millisUntilFinishedMap.put(key, millisInFuture);
        }
        countDownTimer.start();
    }

    /**
     * 获取当前剩余时间
     *
     * @param key
     * @return
     */
    public long getMillisUntilFinished(String key) {
        if (millisUntilFinishedMap != null && millisUntilFinishedMap.containsKey(key)) {
            return millisUntilFinishedMap.get(key);
        }
        return 0;
    }

    /**
     * 获取当前正在倒计时的size
     *
     * @return
     */
    public int getCountDownTimerSize() {
        if (countDownTimerMap != null) {
            return countDownTimerMap.size();
        }
        return 0;
    }

    /**
     * 是否包含key
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        if (countDownTimerMap != null) {
            return countDownTimerMap.containsKey(key);
        }
        return false;
    }

    /**
     * 设置倒计时监听
     *
     * @param key
     * @param countDownListener
     */
    public void setCountDownListener(String key, CountDownListener countDownListener) {
        countDownListenerMap.put(key, countDownListener);
    }

    private class MyCountDownTimer extends CountDownTimer {
        private String key;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(String key, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.key = key;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (countDownListenerMap != null && countDownListenerMap.containsKey(key)) {
                CountDownListener countDownListener = countDownListenerMap.get(key);
                if (countDownListener != null) {
                    countDownListener.onTick(millisUntilFinished);
                }
            }

            if (millisUntilFinishedMap != null) {
                millisUntilFinishedMap.put(key, millisUntilFinished);
            }
        }

        @Override
        public void onFinish() {
            if (countDownListenerMap != null && countDownListenerMap.containsKey(key)) {
                CountDownListener countDownListener = countDownListenerMap.get(key);
                if (countDownListener != null) {
                    countDownListener.onFinish();
                }
                countDownListenerMap.remove(key);
            }
            if (countDownTimerMap != null && countDownTimerMap.containsKey(key)) {
                countDownTimerMap.remove(key);
            }
            if (millisUntilFinishedMap != null) {
                millisUntilFinishedMap.remove(key);
            }
        }
    }

    public interface CountDownListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }
}
