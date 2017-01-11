/**
 * Copyright 2014 Zhenguo Jin (jingle1267@163.com)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ryanh.ryanutils.commonutils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * 根据经纬度查询地址信息和根据地址信息查询经纬度
 *
 * @author jingle1267@163.com
 */
public final class LocationUtils {

    private final static boolean DEBUG = true;
    private final static String TAG = "LocationUtils";

    private static OnLocationChangeListener mListener;
    private static MyLocationListener myLocationListener;
    private static LocationManager mLocationManager;

    /**
     * Don't let anyone instantiate this class.
     */
    private LocationUtils() {
        throw new Error("Do not need instantiate!");
    }

    /*public LocationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }*/

    /**
     * 根据地址获取对应的经纬度
     *
     * @param address 地址信息
     * @return 经纬度数组
     */
    public static double[] getLocationInfo(String address) {
        if (TextUtils.isEmpty(address)) {
            return null;
        }
        if (DEBUG) {
            LogUtils.d(TAG, "address : " + address);
        }
        // 定义一个HttpClient，用于向指定地址发送请求
        HttpClient client = new DefaultHttpClient();
        // 向指定地址发送GET请求
        HttpGet httpGet = new HttpGet("http://maps.google."
                + "com/maps/api/geocode/json?address=" + address
                + "ka&sensor=false");
        StringBuilder sb = new StringBuilder();
        try {
            // 获取服务器的响应
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            // 获取服务器响应的输入流
            InputStream stream = entity.getContent();
            int b;
            // 循环读取服务器响应
            while ((b = stream.read()) != -1) {
                sb.append((char) b);
            }
            // 将服务器返回的字符串转换为JSONObject对象
            JSONObject jsonObject = new JSONObject(sb.toString());
            // 从JSONObject对象中取出代表位置的location属性
            JSONObject location = jsonObject.getJSONArray("results")
                    .getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location");
            // 获取经度信息
            double longitude = location.getDouble("lng");
            // 获取纬度信息
            double latitude = location.getDouble("lat");
            if (DEBUG) {
                LogUtils.d(TAG, "location : (" + longitude + "," + latitude + ")");
            }
            // 将经度、纬度信息组成double[]数组
            return new double[]{longitude, latitude};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取对应的地址
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param lang      语言 如果位空则默认en
     * @return 地址信息
     * @throws Exception
     */
    public static String getAddress(double longitude, double latitude,
                                    String lang) throws Exception {
        if (DEBUG) {
            LogUtils.d(TAG, "location : (" + longitude + "," + latitude + ")");
        }
        if (lang == null) {
            lang = "en";
        }
        // 设定请求的超时时间
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        HttpConnectionParams.setSoTimeout(params, 10 * 1000);
        // 定义一个HttpClient，用于向指定地址发送请求
        HttpClient client = new DefaultHttpClient(params);
        // 向指定地址发送GET请求
        HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/"
                + "geocode/json?latlng=" + latitude + "," + longitude
                + "&sensor=false&language=" + lang);
        if (DEBUG) {
            LogUtils.d(TAG,
                    "URL : " + httpGet.getURI());
        }
        StringBuilder sb = new StringBuilder();
        // 执行请求
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        // 获取服务器响应的字符串
        InputStream stream = entity.getContent();
        int b;
        while ((b = stream.read()) != -1) {
            sb.append((char) b);
        }
        // 把服务器相应的字符串转换为JSONObject
        JSONObject jsonObj = new JSONObject(sb.toString());
        Log.d("ConvertUtil", "getAddress:" + sb.toString());
        // 解析出响应结果中的地址数据
        JSONObject addressObject = jsonObj.getJSONArray("results")
                .getJSONObject(0);
        String address = decodeLocationName(addressObject);
        if (DEBUG) {
            LogUtils.d(TAG, "address : " + address);
        }
        return address;
    }

    /**
     * 根据Google API 解析出国家和城市名称
     * https://developers.google.com/maps/documentation/geocoding
     *
     * @param jsonObject 地址Json对象
     * @return 返回国家和城市
     */
    public static String decodeLocationName(JSONObject jsonObject) {
        JSONArray jsonArray;
        String country = "", city = "";
        String TYPE_COUNTRY = "country";
        String TYPE_LOCALITY = "locality";
        String TYPE_POLITICAL = "political";
        boolean hasCountry = false;
        try {
            jsonArray = jsonObject.getJSONArray("address_components");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                JSONArray types = jo.getJSONArray("types");
                boolean hasLocality = false, hasPolicical = false;

                for (int j = 0; j < types.length(); j++) {
                    String type = types.getString(j);
                    if (type.equals(TYPE_COUNTRY) && !hasCountry) {
                        country = jo.getString("long_name");
                    } else {
                        if (type.equals(TYPE_POLITICAL)) {
                            hasPolicical = true;
                        }
                        if (type.equals(TYPE_LOCALITY)) {
                            hasLocality = true;
                        }
                        if (hasPolicical && hasLocality) {
                            city = jo.getString("long_name");
                        }
                    }
                }
            }
            return city + "," + country;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject.has("formatted_address")) {
            try {
                return jsonObject.getString("formatted_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断Gps是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unregister()}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}来定时更新；</p>
     * <p>{@code minDistance}不为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则随时刷新。</p>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return {@code true}: 初始化成功<br>{@code false}: 初始化失败
     */
    public static boolean register(Context context, long minTime, long minDistance, OnLocationChangeListener listener) {
        if (listener == null) return false;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mListener = listener;
        if (!isLocationEnabled(context)) {
            ToastUtils.showShortToastSafe(context, "无法定位，请打开定位服务");
            return false;
        }
        String provider = mLocationManager.getBestProvider(getCriteria(), true);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) listener.getLastKnownLocation(location);
        if (myLocationListener == null) myLocationListener = new MyLocationListener();
        mLocationManager.requestLocationUpdates(provider, minTime, minDistance, myLocationListener);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // coarse permission is granted
            // Todo
        } else { // permission is not granted, request for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) { // show some info to user why you want this permission
                Toast.makeText(this, "Allow Location Permission to use this functionality.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123 *//*LOCATION_PERMISSION_REQUEST_CODE*//*);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123 *//*LOCATION_PERMISSION_REQUEST_CODE*//*);

            }
        }*/
        return true;
    }


    /**
     * 注销
     */
    public static void unregister() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager.removeUpdates(myLocationListener);
                myLocationListener = null;
            }
            mLocationManager = null;
        }
    }

    /**
     * 设置定位参数
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    private static class MyLocationListener
            implements LocationListener {
        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        @Override
        public void onLocationChanged(Location location) {
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListener != null) {
                mListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }

//    private static LocationUtils.OnLocationChangeListener mListener;
//    private static LocationUtils.MyLocationListener myLocationListener;
//    private static LocationManager mLocationManager;

    /*private LocationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }*/

    /**
     * 判断Gps是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGpsEnabled() {
        LocationManager lm = (LocationManager) Utils.getContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) Utils.getContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unregister()}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}来定时更新；</p>
     * <p>{@code minDistance}不为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则随时刷新。</p>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return {@code true}: 初始化成功<br>{@code false}: 初始化失败
     */
    /*public static boolean register(long minTime, long minDistance, LocationUtils.OnLocationChangeListener listener) {
        if (listener == null) return false;
        mLocationManager = (LocationManager) Utils.getContext().getSystemService(Context.LOCATION_SERVICE);
        mListener = listener;
        if (!isLocationEnabled()) {
            ToastUtils.showShortToastSafe("无法定位，请打开定位服务");
            return false;
        }
        String provider = mLocationManager.getBestProvider(getCriteria(), true);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) listener.getLastKnownLocation(location);
        if (myLocationListener == null)
            myLocationListener = new LocationUtils.MyLocationListener();
        mLocationManager.requestLocationUpdates(provider, minTime, minDistance, myLocationListener);
        return true;
    }*/


    /**
     * 注销
     */
    /*public static void unregister() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager.removeUpdates(myLocationListener);
                myLocationListener = null;
            }
            mLocationManager = null;
        }
    }*/

    /**
     * 设置定位参数
     *
     * @return {@link Criteria}
     */
    /*private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }*/

    /**
     * 根据经纬度获取地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(Utils.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    /*private static class MyLocationListener
            implements LocationListener {
        *//**
     * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
     *
     * @param location 坐标
     *//*
        @Override
        public void onLocationChanged(Location location) {
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }
        }

        *//**
     * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
     *
     * @param provider 提供者
     * @param status   状态
     * @param extras   provider可选包
     *//*
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListener != null) {
                mListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    LogUtils.d("onStatusChanged", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        *//**
     * provider被enable时触发此函数，比如GPS被打开
     *//*
        @Override
        public void onProviderEnabled(String provider) {
        }

        *//**
     * provider被disable时触发此函数，比如GPS被关闭
     *//*
        @Override
        public void onProviderDisabled(String provider) {
        }
    }*/

    /*public interface OnLocationChangeListener {

        *//**
     * 获取最后一次保留的坐标
     *
     * @param location 坐标
     *//*
        void getLastKnownLocation(Location location);

        *//**
     * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
     *
     * @param location 坐标
     *//*
        void onLocationChanged(Location location);

        *//**
     * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
     *
     * @param provider 提供者
     * @param status   状态
     * @param extras   provider可选包
     *//*
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }*/
}
