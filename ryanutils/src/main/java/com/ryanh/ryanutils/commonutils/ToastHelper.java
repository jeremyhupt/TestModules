//package com.ryanh.ryanutils.commonutils;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Handler;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import com.ryanh.ryanutils.R;
//
//
///**
// * 说明:自定义toast
// * <p>
// * author: 吕飞
// * since: 2015-03-17
// * Copyright:恒信汽车电子商务有限公司
// */
//public class ToastHelper {
//    public static final int GREEN = 1;
//    public static final int YELLOW = 2;
//    public static final int ORANGE = 3;
//    public static final int SHOW_TIME = 2200;
//    private WindowManager mWindowManager;
//    private WindowManager.LayoutParams mWindowParams;
//    private View toastView;
//    private Context mContext;
//    private Handler mHandler;
//    private String mToastContent = "";
//    private int duration = 0;
//    public static int sHeight;//吐司高度
//    private int animStyleId = android.R.style.Animation_Toast;
//    private final Runnable timerRunnable = new Runnable() {
//        @Override
//        public void run() {
//            removeView();
//        }
//    };
//
//    private ToastHelper(Context context) {
//        Context ctx = context.getApplicationContext();
//        if (ctx == null) {
//            ctx = context;
//        }
//        this.mContext = ctx;
//        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        init();
//    }
//
//    public static void showOrangeToast(Context context, String mToastContent) {
//        setHeight(context);
//        makeText(context, mToastContent).setAnimation(R.style.Toast).show(ORANGE);
//    }
//
//    public static void showGreenToast(Context context, String mToastContent) {
//        setHeight(context);
//        makeText(context, mToastContent).setAnimation(R.style.Toast).show(GREEN);
//    }
//
//    public static void showYellowToast(Context context, String mToastContent) {
//        setHeight(context);
//        makeText(context, mToastContent).setAnimation(R.style.Toast).show(YELLOW);
//    }
//
//    public static void showOrangeToast(Context context, int resId) {
//        setHeight(context);
//        makeText(context, resId).setAnimation(R.style.Toast).show(ORANGE);
//    }
//
//    private static void setHeight(Context context) {
//        if (context instanceof AppCompatActivity) {
//            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
//            if (actionBar != null) {
//                sHeight = actionBar.getHeight();
//                return;
//            }
//        }
////        sHeight = (int) context.getResources().getDimension(R.dimen.toast_height_56);
//    }
//
//    public static void showGreenToast(Context context, int resId) {
//        setHeight(context);
//        makeText(context, resId).setAnimation(R.style.Toast).show(GREEN);
//    }
//
//    public static void showYellowToast(Context context, int resId) {
//        setHeight(context);
//        makeText(context, resId).setAnimation(R.style.Toast).show(YELLOW);
//    }
//
//    private void init() {
//        mWindowParams = new WindowManager.LayoutParams();
//        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        mWindowParams.alpha = 1.0f;
//        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        mWindowParams.height = ScreenUtils.dip2px(mContext, 56);
//        mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//        mWindowParams.setTitle("ToastHelper");
//        mWindowParams.packageName = mContext.getPackageName();
//        mWindowParams.windowAnimations = animStyleId;
//        mWindowParams.y = 0;
//    }
//
//    @SuppressWarnings("deprecation")
//    @SuppressLint("NewApi")
//    private View getDefaultToastView(int color) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View v = null;
//        switch (color) {
//            case GREEN:
////                v = inflater.inflate(R.layout.toast_green, null);
//                break;
//            case ORANGE:
////                v = inflater.inflate(R.layout.toast_orange, null);
//                break;
//            case YELLOW:
////                v = inflater.inflate(R.layout.toast_yellow, null);
//                break;
//        }
//        TextView textView = null;
////        textView  = (TextView) (v != null ? v.findViewById(R.id.content) : null);
//        if (textView != null) {
//            textView.setText(mToastContent);
//        }
//        return v;
//    }
//
//    public void show(int color) {
//        if (!OtherUtil.isBackground(mContext)) {
//            removeView();
//            toastView = getDefaultToastView(color);
//            mWindowParams.gravity = android.support.v4.view.GravityCompat
//                    .getAbsoluteGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP,
//                            android.support.v4.view.ViewCompat
//                                    .getLayoutDirection(toastView));
//            removeView();
//            mWindowManager.addView(toastView, mWindowParams);
//            if (mHandler == null) {
//                mHandler = new Handler();
//            }
//            mHandler.postDelayed(timerRunnable, duration);
//        }
//    }
//
//    public void removeView() {
//        if (toastView != null && toastView.getParent() != null) {
//            mWindowManager.removeView(toastView);
//            mHandler.removeCallbacks(timerRunnable);
//        }
//    }
//
//    /**
//     * @param context
//     * @param content
//     * @return
//     */
//    public static ToastHelper makeText(Context context, String content) {
//        ToastHelper helper = new ToastHelper(context);
//        helper.setDuration(SHOW_TIME);
//        helper.setContent(content);
//        return helper;
//    }
//
//    /**
//     * @param context
//     * @param strId
//     */
//    public static ToastHelper makeText(Context context, int strId) {
//        ToastHelper helper = new ToastHelper(context);
//        helper.setDuration(SHOW_TIME);
//        helper.setContent(context.getString(strId));
//        return helper;
//    }
//
//    public ToastHelper setContent(String content) {
//        this.mToastContent = content;
//        return this;
//    }
//
//    public ToastHelper setDuration(int duration) {
//        this.duration = duration;
//        return this;
//    }
//
//    public ToastHelper setAnimation(int animStyleId) {
//        this.animStyleId = animStyleId;
//        mWindowParams.windowAnimations = this.animStyleId;
//        return this;
//    }
//}
//
