/**
 * author 胡俊杰 Todo
 *//*

package com.ryanh.ryanutils.commonutils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ryanh.ryanutils.R;
import com.ryanh.ryanutils.common.io.stream.ByteArrayOutputStream;
import com.ryanh.ryanutils.common.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

*/
/**
 * author Administrator
 *//*

public class OtherUtil {

    */
/**
     * 获取ass图片
     *
     * @param context
     * @param fileName
     * @return
     *//*

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    */
/**
     * 获取设备信息
     *
     * @param ctx
     * @return
     *//*

    public static Map< String, String > collectDeviceInfo(Context ctx) {
        Map< String, String > infos = new HashMap< String, String >();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return infos;
    }

    // 从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName,AssetManager.ACCESS_BUFFER);
            result = readTextFile(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String readTextFile(InputStream inputStream) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            IOUtils.closeQuietly(outputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    public static final int NUMBER = 1;//数字
    public static final int LETTER = 2;//字母
    public static final int OTHER = 3;//其他


    */
/**
     * 判断boolean值
     *
     * @param s 非0为真
     *//*

    public static boolean tBoolen(int s) {
        return tBoolean(String.valueOf(s));
    }


    */
/**
     * 判断boolean值
     *
     * @param s 非0为真
     *//*

    public static boolean tBoolean(String s) {
        return !(s == null || s.length() == 0) && "1".equalsIgnoreCase(s);
    }


    */
/**
     * boolean转int
     *
     * @param b
     * @return
     *//*

    public static int boolean2Int(boolean b) {
        return b ? 1 : 0;
    }


    public static boolean int2Boolean(int i) {
        return i > 0;
    }


    */
/**
     * 金额格式化
     *
     * @param symbol 是否有羊角符  ¥
     * @return 1w以下单位为元，1w以上单位为w
     *//*

    public static String amountFormat(String value, boolean symbol) {
        return amountFormat(Float.valueOf(value), symbol);
    }


    */
/**
     * 金额格式化
     *
     * @param symbol 是否有羊角符  ¥
     * @return 1w以下单位为元，1w以上单位为w
     *//*

    public static String amountFormat(float value, boolean symbol) {

        if (symbol) {
            //有羊角符
            if (value >= 10000) {
                DecimalFormat format = new DecimalFormat("#####0.00");
                return String.format("¥ %s万", format.format(Math.floor(value / 100.00) / 100.00));
            } else {
                return String.format("¥ %.2f", value);
            }
        } else {
            DecimalFormat format = new DecimalFormat("######.##");
            if (value >= 10000) {
                return String.format("%s万", format.format(Math.floor(value / 100.00) / 100.00));
            } else {
                return String.format("%s元", format.format(value));
            }
        }
    }


    */
/**
     * 金额格式化
     *
     * @return 1w以下单位为元，1w以上单位为w
     *//*

    public static String amountFormat(float value) {
        if (value == 0) {
            return 0 + "元";
        }
        NumberFormat nf = NumberFormat.getInstance();
        if (value >= 10000) {
            nf.setMinimumFractionDigits(2);
            String str = nf.format(Math.floor(value / 100.00) / 100.00);
            return String.format("%s万", str);
        } else {
            DecimalFormat format = new DecimalFormat("###,###,###,###,##0.00");
//            return String.format("%s元", nf.format(value / 1.00));
            return String.format("%s元", format.format(value));
        }
    }


    public static String amountFormat(String value) {
        try {
            return amountFormat(Float.valueOf(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }


    */
/**
     * 金额转换
     *
     * @param money
     *//*

    public static String stringToMoney(String money) {
        if (TextUtils.isEmpty(money)) return "¥";
        DecimalFormat format = new DecimalFormat("###,###,###,###,##0.00");
        Double num = Double.parseDouble(money);
        return "¥" + format.format(num);
    }


    public static String stringToMoney(float money) {
        DecimalFormat format = new DecimalFormat("###,###,###,###,##0.00");
        return "¥" + format.format(money);
    }


    */
/**
     * 格式化 价格区间
     *
     * @param rangeString 10000-20000
     * @return 1.00-2.00万
     *//*

    public static String formatPriceRange(String rangeString) {
        if (rangeString.contains("-")) {
            String price[] = rangeString.split("-");
            StringBuilder stringBuilder = new StringBuilder();
            if (price.length == 2) {
                if (price[0].equals(price[1])) {
                    return OtherUtil.amountFormat(Float.valueOf(price[0]));
                }
                try {
                    NumberFormat nf = NumberFormat.getInstance();
                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                    price[0] = decimalFormat.format(Double.valueOf(nf.format(Math.floor(Float.valueOf(price[0]) / 100.00) / 100.00)));
                    price[1] = OtherUtil.amountFormat(Float.valueOf(price[1]), false);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    NumberFormat nf = NumberFormat.getInstance();
                    price[0] = nf.format(Float.valueOf(price[0]) / 10000.00);
                } finally {
                    stringBuilder.append(price[0]).append(" — ").
                            append(price[1]);
                }

            } else {
                return rangeString;
            }
            return stringBuilder.toString();
        }
        return rangeString;
    }

    */
/**
     * 50 - 60
     * ￥50 - ￥60
     * @param value
     * @return
     *//*

    public static String CalculateRangeMoney(String value,boolean symbol){
        if(value.contains("-")){
            String values[] = value.split("-");
            String minValue = values[0];
            String maxValue = values[1];

            return OtherUtil.amountFormat(minValue,symbol) + " - " + OtherUtil.amountFormat(maxValue,symbol);
        }else {
            return    amountFormat(value, symbol);
        }
    }

    */
/**
     * 测试某个字符是属于哪一类
     *//*

    public static int getCharMode(char c) {
        if (c >= 48 && c <= 57) {
            return NUMBER;
        } else if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
            return LETTER;
        } else return OTHER;
    }


    */
/**
     * 打开所有子选项
     *
     * @param adapter
     * @param expandableListView
     *//*

    public static void openAllChild(ExpandableListAdapter adapter, ExpandableListView expandableListView) {
        for (int i = 0, count = adapter.getGroupCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }


    */
/**
     * 判断程序是否在后台运行
     *
     * @param context
     *//*

    public static boolean isBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }


    */
/**
     * 首页特卖降幅变红
     *
     * @param text
     *//*

    public static SpannableStringBuilder showCut(String text) {
        String[] str = text.split(" ");
        int length = str[0].length();
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#e02c36")), 0, length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色
        return style;
    }


    */
/**
     * 变色打电话文字
     *
     * @param text
     *//*

    public static SpannableStringBuilder toCallText(String text, int start, int end, final String color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }


            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor(color));
                // 去掉下划线
                ds.setUnderlineText(false);
            }
        }, start, end, 0);
        return style;
    }


    */
/**
     * 设置头尾cardview的边距
     *
     * @param context
     *//*

    public static void setCardViewMargin(Context context, CardView cardView, int position, int size) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            lp.setMargins((int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)), (int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)), (int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)), (int) (context.getResources().getDimension(R.dimen.normal_margin_small_8)));
            cardView.setLayoutParams(lp);
        } else if (position == size - 1) {
            lp.setMargins((int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)), (int) (context.getResources().getDimension(R.dimen.normal_margin_small_8)), (int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)), (int) (context.getResources().getDimension(R.dimen.normal_margin_big_16)));
            cardView.setLayoutParams(lp);
        }
    }


    */
/**
     * 打开软键盘
     *
     * @param context
     *//*

    public static void openSoftKeyBoard(Context context, View view) {
        InputMethodManager im = ((InputMethodManager) (context.getSystemService(Context.INPUT_METHOD_SERVICE)));
        im.showSoftInput(view, 0);
    }


    */
/**
     * 关闭软键盘
     *//*

    public static void closeSoftKeyBoard(Context context, View view) {
        ((InputMethodManager) (context.getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    */
/**
     * 呼叫客服
     * 胡俊杰
     *//*

    public static void callHXService(Context context) {
        callPhone(context, context.getString(R.string.tv_service_phone));

    }


    */
/**
     * 拨打电话
     * liaoguilong
     *//*

    public static void callPhone(Context context, String phoneNum) {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(dialIntent);
    }


    */
/**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     *//*

    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    */
/**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @return
     *//*

    public static void setImageSquare(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).fit().placeholder(R.drawable.pic_normal_square).error(R.drawable.pic_normal_square).into(imageView);
        }
    }
    */
/**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @return
     *//*

    public static void setImage(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).fit().placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(imageView);
        }
    }


    */
/**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param resId     占位图
     * @return
     *//*

    public static void setImage(Context context, ImageView imageView, String url, int resId) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).fit().placeholder(resId).error(resId).into(imageView);
        }
    }


    */
/**
     * 设置控件是否显示
     *
     * @param view
     * @param visible
     * @return
     *//*

    public static void setVisible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    */
/**
     * yyyy-MM格式转"x月"
     *
     * @param month_of_yyyy_MM yyyy-MM格式
     *//*

    public static String singleMonth(String month_of_yyyy_MM) {
        if (month_of_yyyy_MM.length() != 7) return "";
        String month = month_of_yyyy_MM.split("-")[1];
        month = month.replace("0", "");
        return month + "月";
    }


    */
/**
     * 获取上一个月
     *
     * @param month_of_yyyy_MM
     * @return
     *//*

    public static String lastMonth(String month_of_yyyy_MM) {
        String lastMonth = "";
        if (month_of_yyyy_MM.length() < 7) return "";
        String[] split = month_of_yyyy_MM.split("-");
        if (split.length < 2) return "";
        String year = split[0];
        String month = split[1];
        int yearInt = 0, monthInt = 0;
        if (!TextUtils.isEmpty(year)) yearInt = Integer.parseInt(year);
        if (!TextUtils.isEmpty(month)) monthInt = Integer.parseInt(month);
        if (monthInt > 0 && yearInt > 0) {
            switch (monthInt) {
                case 1:
                    lastMonth = (yearInt - 1) + "-" + 12;
                    break;
                case 12:
                    lastMonth = yearInt + "-" + (monthInt - 1);
                    break;
                case 11:
                    lastMonth = yearInt + "-" + (monthInt - 1);
                    break;
                default:
                    lastMonth = yearInt + "-0" + (monthInt - 1);
            }
        }
        return lastMonth;
    }


    */
/** 格式化距离显示规则 **//*

    public static String reformatDistance(Object distance) {
        if (null == distance || (distance instanceof String && TextUtils.isEmpty((String) distance))) return "0.1km";
        if (distance instanceof String) {
            try {
                distance = Double.valueOf((String) distance);
            } catch (Exception e) {
                distance = 0;
            }
        }
        distance = (double) distance / 1000;
        if ((double) distance < 0.1) return "0.1km";
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(distance) + "km";
    }


    */
/** 格式化价格，保留两位小数，去掉价格中无效的0 **//*

    public static String reformatPrice(String price) {
        if (TextUtils.isEmpty(price)) return "0";
        DecimalFormat df = new DecimalFormat("######0.00");
        try {
            price = df.format(Double.valueOf(price));
        } catch (Exception e) {
            DebugLog.e("OtherUtil", "classCastException");
        }
        if (price.endsWith("0")) {
            price = price.substring(0, price.length() - 1);
            if (price.endsWith("0")) {
                return price.substring(0, price.length() - 2);
            }
        }
        return price;
    }


    */
/**
     * collection 转list
     *
     * @param collection
     * @return
     *//*

    public static  <T> ArrayList<T> collectionToList(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        ArrayList<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }




    */
/**
     * 文件存储根目录
     * @param context
     * @return  0/SD/QR_URL/XXXX.jpg
     *//*

    public static String getFullPath(Context context,String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                DebugLog.d("QR_CODE", "filepath->external-->" + external.getAbsolutePath() + File.separator + fileName);
                FileUtil.makeFileDirs(external.getAbsolutePath()+File.separator+fileName);
                return external.getAbsolutePath()+File.separator+fileName;
            }
        }
        FileUtil.makeFileDirs(context.getFilesDir().getAbsolutePath()+File.separator+fileName);
        DebugLog.d("QR_CODE", "filepath->context.getFilesDir()-->" + context.getFilesDir().getAbsolutePath() + File.separator+ fileName);
        return context.getFilesDir().getAbsolutePath()+File.separator+fileName;
    }

}
*/
