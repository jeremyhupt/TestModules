package com.ryanh.ryanutils.commonutils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.ryanh.ryanutils.bar.BarUtils;

/**
 * ScreenUtils
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * <li>{@link ScreenUtils#dpToPx(Context, float)}</li>
 * <li>{@link ScreenUtils#pxToDp(Context, float)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-14
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_HEIGHT_DP;
    private static boolean sInitialed;

    public static void init(Context context) {
        if (!sInitialed && context != null) {
            sInitialed = true;
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            SCREEN_WIDTH_PIXELS = dm.widthPixels;
            SCREEN_HEIGHT_PIXELS = dm.heightPixels;
            SCREEN_DENSITY = dm.density;
            SCREEN_WIDTH_DP = (int) ((float) SCREEN_WIDTH_PIXELS / dm.density);
            SCREEN_HEIGHT_DP = (int) ((float) SCREEN_HEIGHT_PIXELS / dm.density);
        }
    }

    public static int dp2px(float dp) {
        float scale = SCREEN_DENSITY;
        return (int) (dp * scale + 0.5F);
    }

    public static int designedDP2px(float designedDp) {
        if (SCREEN_WIDTH_DP != 320) {
            designedDp = designedDp * (float) SCREEN_WIDTH_DP / 320.0F;
        }

        return dp2px(designedDp);
    }

    public static void setPadding(View view, float left, float top, float right, float bottom) {
        view.setPadding(designedDP2px(left), dp2px(top), designedDP2px(right), dp2px(bottom));
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    public static float getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 像素转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转像素
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转像素
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /*private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }*/

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) Utils.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) Utils.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     *
     * @param activity activity
     */
    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape() {
        return Utils.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait() {
        return Utils.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    public static int getScreenRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default:
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = BarUtils.getStatusBarHeight(activity);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 判断是否锁屏
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isScreenLock() {
        KeyguardManager km = (KeyguardManager) Utils.getContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }
}
