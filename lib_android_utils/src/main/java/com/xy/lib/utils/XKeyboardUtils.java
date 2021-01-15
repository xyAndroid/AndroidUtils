package com.xy.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

/**
 * 键盘工具类：提供显示隐藏软键盘，监听软键盘的显示隐藏和高度等功能
 *
 * @author wangtao
 * @date 2020/7/29
 */
public class XKeyboardUtils {

    /**
     * 打开软键盘
     */
    public static void showKeyboard(Activity activity) {
        showKeyboard(activity, 0);
    }

    public static void showKeyboard(Activity activity, int flags) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        showKeyboard(view, flags);
    }

    public static void showKeyboard(View view) {
        showKeyboard(view, 0);
    }

    /**
     * 打开软键盘
     *
     * @param view  The currently focused view, which would like to receive soft keyboard input.
     * @param flags Provides additional operating flags
     */
    public static void showKeyboard(View view, int flags) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN || resultCode == InputMethodManager.RESULT_HIDDEN) {
                        imm.toggleSoftInput(0, 0);
                    }
                }
            });
        }
    }

    /**
     * 切换软键盘的可见性
     *
     * @param showFlags Provides additional operating flags.  May be 0 or have the InputMethodManager.SHOW_IMPLICIT
     * @param hideFlags Provides additional operating flags.  May be 0 or have the InputMethodManager.HIDE_IMPLICIT_ONLY
     */
    public static void toggleSoftInput(int showFlags, int hideFlags) {
        InputMethodManager imm = (InputMethodManager) XAndroidUtils.app().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(showFlags, hideFlags);
    }

    public static void hideKeyboard(View view) {
        hideKeyboard(view, 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param view  当前Activity中的任一View
     * @param flags Provides additional operating flags
     */
    public static void hideKeyboard(View view, int flags) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), flags, new ResultReceiver(new Handler()) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN || resultCode == InputMethodManager.RESULT_SHOWN) {
                        imm.toggleSoftInput(0, 0);
                    }
                }
            });
        }
    }

    /**
     * 添加软键盘是否显示的监听器
     *
     * @param activity 需要添加监听的Activity
     * @param listener IKeyBoardVisibleListener
     */
    public static void addOnKeyBoardVisibleListener(Activity activity, final IKeyBoardVisibleListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                boolean visible = (double) displayHeight / height < 0.8;
                if (visible) {
                    listener.onSoftKeyBoardVisibleChange(true, keyboardHeight);
                } else {
                    listener.onSoftKeyBoardVisibleChange(false, 0);
                }
            }
        });
    }


    /**
     * 软键盘高度变化监听
     * @param activity
     * @param keyboardHeightObserver
     */
    public static void registerKeyboardChangeListener(Activity activity, @NonNull XKeyboardHeightProvider.KeyboardHeightObserver keyboardHeightObserver){
        XKeyboardHeightProvider keyboardHeightProvider = new XKeyboardHeightProvider(activity);
        keyboardHeightProvider.setKeyboardHeightObserver(keyboardHeightObserver);
        keyboardHeightProvider.start();
    }

    public interface IKeyBoardVisibleListener {
        /**
         * 键盘可见性变化的监听
         *
         * @param visible        是否可见
         * @param keyboardHeight 键盘高度
         */
        void onSoftKeyBoardVisibleChange(boolean visible, int keyboardHeight);
    }

}
