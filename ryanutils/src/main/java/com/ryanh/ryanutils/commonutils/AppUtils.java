package com.ryanh.ryanutils.commonutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * AppUtils
 * <ul>
 * <li>{@link AppUtils#isNamedProcess(Context, String)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-5-07
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * whether this process is named with processName
     * 
     * @param context
     * @param processName
     * @return <ul>
     *         return whether this process is named with processName
     *         <li>if context is null, return false</li>
     *         <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     *         <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     *         true, otherwise return false</li>
     *         </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (ListUtils.isEmpty(processInfoList)) {
            return false;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && ObjectUtils.isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     * 
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * activity MetaData读取
     *
     * @throws PackageManager.NameNotFoundException
     */
    public static String getActivityMetaData(Context context, String key) throws PackageManager.NameNotFoundException {
        ActivityInfo info;
        info = context.getPackageManager().getActivityInfo(((Activity) context).getComponentName(),
                PackageManager.GET_META_DATA);

        return info.metaData.getString(key);

    }

    /**
     * appliction MetaData读取
     *
     * @throws PackageManager.NameNotFoundException
     */
    public static String getApplicationMetaDataString(Context context, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo info;
        info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        return info.metaData.getString(key);

    }
    /**
     * appliction MetaData读取
     *
     * @throws PackageManager.NameNotFoundException
     */
    public static int getApplicationMetaDataInt(Context context, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo info;
        info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        return info.metaData.getInt(key);

    }
}
