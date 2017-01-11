package com.ryanh.ryanutils.commonutils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * NetWork Utils
 * <ul>
 * <strong>Attentions</strong>
 * <li>You should add <strong>android.permission.ACCESS_NETWORK_STATE</strong> in manifest, to get network status.</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-11-03
 */
public class NetWorkUtils {

    public static final String NETWORK_TYPE_WIFI       = "wifi";
    public static final String NETWORK_TYPE_3G         = "eg";
    public static final String NETWORK_TYPE_2G         = "2g";
    public static final String NETWORK_TYPE_WAP        = "wap";
    public static final String NETWORK_TYPE_UNKNOWN    = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    /**
     * Get network type
     * 
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    /**
     * Get network type name
     * 
     * @param context
     * @return
     */
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        String type = NETWORK_TYPE_DISCONNECT;
        if (manager == null || (networkInfo = manager.getActiveNetworkInfo()) == null) {
            return type;
        };

        if (networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(typeName)) {
                type = NETWORK_TYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? NETWORK_TYPE_3G : NETWORK_TYPE_2G)
                        : NETWORK_TYPE_WAP;
            } else {
                type = NETWORK_TYPE_UNKNOWN;
            }
        }
        return type;
    }

    /**
     * Whether is fast mobile network
     * 
     * @param context
     * @return
     */
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /*private NetworkUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }*/

    public enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * 打开网络设置界面
     * <p>3.0以下打开设置界面</p>
     */
    public static void openWirelessSettings() {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            Utils.getContext().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        } else {
            Utils.getContext().startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        }
    }

    /**
     * 获取活动网络信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
    public static boolean isAvailableByPing() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("ping -c 1 -w 1 123.125.114.144", false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            LogUtils.d("isAvailableByPing errorMsg", result.errorMsg);
        }
        if (result.successMsg != null) {
            LogUtils.d("isAvailableByPing successMsg", result.successMsg);
        }
        return ret;
    }

    /**
     * 判断移动数据是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean getDataEnabled() {
        try {
            TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnabledMethod) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     * <p>需系统应用 需添加权限{@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>}</p>
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setDataEnabled(boolean enabled) {
        try {
            TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否是4G
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否打开
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean getWifiEnabled() {
        WifiManager wifiManager = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}</p>
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setWifiEnabled( boolean enabled) {
        WifiManager wifiManager = (WifiManager)Utils.getContext().getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 判断wifi是否连接状态
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return {@code true}: 连接<br>{@code false}: 未连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断wifi数据是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailableByPing();
    }

    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 运营商名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    private static final int NETWORK_TYPE_GSM      = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN    = 18;

    /**
     * 获取当前网络类型
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return 网络类型
     * <ul>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_WIFI   } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_4G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_3G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_2G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_UNKNOWN} </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_NO     } </li>
     * </ul>
     */
    public static NetWorkUtils.NetworkType getNetworkType() {
        NetWorkUtils.NetworkType netType = NetWorkUtils.NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetWorkUtils.NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetWorkUtils.NetworkType.NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetWorkUtils.NetworkType.NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetWorkUtils.NetworkType.NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetWorkUtils.NetworkType.NETWORK_3G;
                        } else {
                            netType = NetWorkUtils.NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetWorkUtils.NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取IP地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @param useIPv4 是否用IPv4
     * @return IP地址
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名ip地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @param domain 域名
     * @return ip地址
     */
    public static String getDomainAddress(final String domain) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            Future<String> fs = exec.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    InetAddress inetAddress;
                    try {
                        inetAddress = InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            return fs.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
