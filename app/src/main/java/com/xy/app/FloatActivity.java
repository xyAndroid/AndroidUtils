package com.xy.app;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.xy.lib.utils.ConvertUtils;
import com.xy.lib.utils.ScreenUtils;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

/**
 * @author mayn
 * @date 2019/9/16
 * description:
 */
public class FloatActivity extends Activity {
    private static final String TAG = "tag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);
//        showVoiceFloat();
    }

    public void clickFloat(View view) {
        showVoiceFloat();
    }

    private static final String FLOAT_BUTTON = "FLOAT_BUTTON";
    private static final String FLOAT_CANCEL = "FLOAT_CANCEL";
    private static final String FLOAT_BIG_CANCEL = "FLOAT_BIG_CANCEL";

    private int cancelSize, cancelSize2, buttonSize, offsetX, offsetY;
    private long offsetTime = 300;

    //消失view的x和y坐标
    private int xCancelOffset, yCancelOffset, xCancelOffset2, yCancelOffset2;
    //屏幕右下角的x\y坐标
    private int xCoordinate, yCoordinate;

    public void showVoiceFloat() {
        initSize();
        IFloatWindow cancel2 = FloatWindow.get(FLOAT_BIG_CANCEL);
        if (cancel2 == null) {
            FloatWindow
                    .with(getApplicationContext())
                    .setWidth(cancelSize2)
                    .setHeight(cancelSize2)
                    .setTag(FLOAT_BIG_CANCEL)
                    .setView(R.layout.layout_window)
                    .setMoveType(MoveType.inactive, 0, 0)
                    .setDesktopShow(true)
                    .build();
            cancel2 = FloatWindow.get(FLOAT_BIG_CANCEL);
        }
        if (cancel2 != null) {
            cancel2.show();
            if (cancel2.getView() != null) {
                cancel2.getView().findViewById(R.id.fl_float).setBackgroundResource(R.drawable.bg_circle2);
                cancel2.getView().setVisibility(View.INVISIBLE);
            }
        }

        IFloatWindow cancel = FloatWindow.get(FLOAT_CANCEL);
        if (cancel == null) {
            FloatWindow
                    .with(getApplicationContext())
                    .setWidth(cancelSize)
                    .setHeight(cancelSize)
                    .setTag(FLOAT_CANCEL)
                    .setView(R.layout.layout_window)
                    .setMoveType(MoveType.inactive, 0, 0)
                    .setDesktopShow(true)
                    .build();
            cancel = FloatWindow.get(FLOAT_CANCEL);
        }
        if (cancel != null) {
            cancel.show();
            if (cancel.getView() != null) {
                cancel.getView().setVisibility(View.INVISIBLE);
            }
        }

        if (FloatWindow.get(FLOAT_BUTTON) == null) {
            TextView textView = new TextView(getApplicationContext());
            textView.setBackgroundResource(R.mipmap.ic_launcher);
//            textView.setText("港股");
//            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.WHITE);
            textView.getPaint().setFakeBoldText(true);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            FloatWindow.with(getApplicationContext())
                    .setTag(FLOAT_BUTTON)
                    .setView(textView)
                    .setX(0)
                    .setY(Screen.height, 0.7f)
                    .setWidth(buttonSize)
                    .setHeight(buttonSize)
                    .setMoveType(MoveType.slide)
                    .setMoveStyle(cancelSize, new BounceInterpolator())
                    .setDesktopShow(true)
                    .setViewStateListener(mViewStateListener)
                    .build();
        }
        if (FloatWindow.get(FLOAT_BUTTON) != null) {
            FloatWindow.get(FLOAT_BUTTON).show();
        }
    }

    private void initSize() {
        cancelSize = ConvertUtils.dp2px(this,230);
        cancelSize2 = ConvertUtils.dp2px(this,252);
        buttonSize = ConvertUtils.dp2px(this,58);
//        int realHeight = ScreenUtils.getScreenHeight(getApplicationContext());
        int width = ScreenUtils.getScreenWidth(getApplicationContext());
        int height = ScreenUtils.getScreenHeight(getApplicationContext());
        int navigationHeight = ScreenUtils.getNavigationBarHeight(this);
        int statusbarHeight = ScreenUtils.getStatusBarHeight(this);
//        boolean flag = realHeight == (height + navigationHeight + statusbarHeight);
//        int contentHeight = flag ? height : (height - statusbarHeight);
        int contentHeight = height - statusbarHeight;

        offsetX = width - buttonSize / 2 - cancelSize2 / 2;
        offsetY = contentHeight - buttonSize / 2 - cancelSize2 / 2;
        if (xCancelOffset == 0) {
            xCancelOffset = width - cancelSize / 2;
            yCancelOffset = contentHeight - cancelSize / 2;
        }
        if (xCancelOffset2 == 0) {
            xCancelOffset2 = width - cancelSize2 / 2;
            yCancelOffset2 = contentHeight - cancelSize2 / 2;
        }
        if (xCoordinate == 0) {
            xCoordinate = width;
            yCoordinate = height;
        }
    }


    private void showWithAnimator(final boolean isShow) {
        ValueAnimator mAnimator = new ValueAnimator();
        mAnimator.setDuration(cancelSize);
        if (isShow) {
            mAnimator.setObjectValues(new PointF(xCoordinate, yCoordinate), new PointF(xCancelOffset, yCancelOffset));
            Log.d(TAG, "onAnimationUpdate SHOW-----" + xCoordinate + "," + yCoordinate + xCancelOffset + "," + yCancelOffset);
        } else {
            mAnimator.setObjectValues(new PointF(xCancelOffset, yCancelOffset), new PointF(xCoordinate, yCoordinate));
            Log.d(TAG, "onAnimationUpdate hide-----" + xCancelOffset + "," + yCancelOffset + xCoordinate + "," + yCoordinate);
        }

        mAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                int valueX = (int) (startValue.x + fraction * (endValue.x - startValue.x));
                int valueY = (int) (startValue.y + fraction * (endValue.y - startValue.y));
                return new PointF(valueX, valueY);
            }
        });
        mAnimator.start();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                if (isShow) {
                    IFloatWindow bigWindow = FloatWindow.get(FLOAT_BIG_CANCEL);
                    if (bigWindow != null && bigWindow.getView() != null && bigWindow.getView().getVisibility() == View.VISIBLE) {
                        if (FloatWindow.get(FLOAT_CANCEL) != null) {
                            FloatWindow.get(FLOAT_CANCEL).updateX(xCancelOffset);
                            FloatWindow.get(FLOAT_CANCEL).updateY(yCancelOffset);
                        }
                        return;
                    }
                }
                if (FloatWindow.get(FLOAT_CANCEL) != null) {
                    FloatWindow.get(FLOAT_CANCEL).updateX((int) point.x);
                    FloatWindow.get(FLOAT_CANCEL).updateY((int) point.y);
                    Log.d(TAG, "onAnimationUpdate" + point.x + "," + point.y);
                }
            }
        });
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };
    private long time;
    private ViewStateListener mViewStateListener = new ViewStateListener() {
        private boolean isActionUp = false;

        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d(TAG, "onPositionUpdate: x=" + x + " y=" + y);
            if (!isActionUp) {
                IFloatWindow bigWindow = FloatWindow.get(FLOAT_BIG_CANCEL);
                if (bigWindow != null && bigWindow.getView() != null) {
                    if (x > offsetX && y > offsetY) {
                        bigWindow.updateX(xCancelOffset2);
                        bigWindow.updateY(yCancelOffset2);
                        bigWindow.getView().setVisibility(View.VISIBLE);
                        Log.d(TAG, "onPositionUpdate---VISIBLE");
                    } else {
                        bigWindow.getView().setVisibility(View.GONE);
                        Log.d(TAG, "onPositionUpdate----GONE");
                    }
                }
            }
        }

        @Override
        public void onShow() {
            Log.d(TAG, "onShow");
        }

        @Override
        public void onHide() {
            Log.d(TAG, "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d(TAG, "onDismiss");
            if (FloatWindow.get(FLOAT_CANCEL) != null) {
                FloatWindow.get(FLOAT_CANCEL).hide();
                FloatWindow.destroy(FLOAT_CANCEL);
            }
            if (FloatWindow.get(FLOAT_BIG_CANCEL) != null) {
                FloatWindow.get(FLOAT_BIG_CANCEL).hide();
                FloatWindow.destroy(FLOAT_BIG_CANCEL);
            }
        }

        @Override
        public void onMoveAnimStart() {
            Log.d(TAG, "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d(TAG, "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d(TAG, "onBackToDesktop");
        }

        @Override
        public void onDown() {
            isActionUp = false;
            Log.d(TAG, "onDown");
            if (FloatWindow.get(FLOAT_CANCEL) != null) {
                FloatWindow.get(FLOAT_CANCEL).getView().setVisibility(View.VISIBLE);
                showWithAnimator(true);
            }
            time = System.currentTimeMillis();
        }

        @Override
        public void onUp() {
            isActionUp = true;
            Log.d(TAG, "onUp");
            IFloatWindow bigWindow = FloatWindow.get(FLOAT_BIG_CANCEL);
            if (bigWindow != null && bigWindow.getView() != null) {
                if (bigWindow.getView().getVisibility() == View.VISIBLE) {
                    Log.d(TAG, "destory" + (bigWindow.getView().getVisibility() == View.VISIBLE));
                    destory();
                    FloatWindow.destroy(FLOAT_BIG_CANCEL);
                } else {
                    if (System.currentTimeMillis() - time < offsetTime) {
                        FloatWindow.get(FLOAT_CANCEL).getView().setVisibility(View.INVISIBLE);
                    } else {
                        showWithAnimator(false);
                    }
                }
            } else {
                if (System.currentTimeMillis() - time < offsetTime) {
                    FloatWindow.get(FLOAT_CANCEL).getView().setVisibility(View.INVISIBLE);
                } else {
                    showWithAnimator(false);
                }
            }
        }
    };

    public void destory() {
        if (FloatWindow.get(FLOAT_BUTTON) != null) {
            FloatWindow.get(FLOAT_BUTTON).hide();
            FloatWindow.destroy(FLOAT_BUTTON);
        }
    }

}
