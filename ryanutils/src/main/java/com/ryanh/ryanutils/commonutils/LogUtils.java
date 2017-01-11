package com.ryanh.ryanutils.commonutils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class can replace android.util.Log.
 *
 * @author jingle1267@163.com
 * @description And you can turn off the log by set DEBUG_LEVEL = Log.ASSERT.
 */
public final class LogUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private LogUtils() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * Master switch.To catch error info you need set this value below Log.WARN
     */
    public static final int DEBUG_LEVEL = 0;

    /**
     * 'System.out' switch.When it is true, you can see the 'System.out' log.
     * Otherwise, you cannot.
     */
    public static final boolean DEBUG_SYSOUT = false;

    /**
     * Send a {@link Log#VERBOSE} log message.
     *
     * @param obj
     */
    public static void v(Object obj) {
        if (Log.VERBOSE > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.v(tag, msg);
        }
    }

    /**
     * Send a {@link #DEBUG_LEVEL} log message.
     *
     * @param obj
     */
    public static void d(Object obj) {
        if (Log.DEBUG > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.d(tag, msg);
        }
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param obj
     */
    public static void i(Object obj) {
        if (Log.INFO > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.i(tag, msg);
        }
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param obj
     */
    public static void w(Object obj) {
        if (Log.WARN > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.w(tag, msg);
        }
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param obj
     */
    public static void e(Object obj) {
        if (Log.ERROR > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.e(tag, msg);
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen. The
     * error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param obj
     */
    public static void wtf(Object obj) {
        if (Log.ASSERT > DEBUG_LEVEL) {
            String tag = getClassName();
            String msg = obj != null ? obj.toString() : "obj == null";
            Log.wtf(tag, msg);
        }
    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (Log.VERBOSE > DEBUG_LEVEL) {
            Log.v(tag, msg);
        }
    }

    /**
     * Send a {@link #DEBUG_LEVEL} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (Log.DEBUG > DEBUG_LEVEL) {
            Log.d(tag, msg);
        }
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (Log.INFO > DEBUG_LEVEL) {
            Log.i(tag, msg);
        }
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (Log.WARN > DEBUG_LEVEL) {
            Log.w(tag, msg);
        }
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (Log.ERROR > DEBUG_LEVEL) {
            Log.e(tag, msg);
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen. The
     * error will always be logged at level ASSERT with the call stack.
     * Depending on system configuration, a report may be added to the
     * {@link android.os.DropBoxManager} and/or the process may be terminated
     * immediately with an error dialog.
     *
     * @param tag Used to identify the source of a log message.
     * @param msg The message you would like logged.
     */
    public static void wtf(String tag, String msg) {
        if (Log.ASSERT > DEBUG_LEVEL) {
            Log.wtf(tag, msg);
        }
    }

    /**
     * Send a {@link Log#VERBOSE} log message. And just print method name and
     * position in black.
     */
    public static void print() {
        if (Log.VERBOSE > DEBUG_LEVEL) {
            String tag = getClassName();
            String method = callMethodAndLine();
            Log.v(tag, method);
            if (DEBUG_SYSOUT) {
                System.out.println(tag + "  " + method);
            }
        }
    }

    /**
     * Send a {@link #DEBUG_LEVEL} log message.
     *
     * @param object The object to print.
     */
    public static void print(Object object) {
        if (Log.DEBUG > DEBUG_LEVEL) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String content = "";
            if (object != null) {
                content = object.toString() + "                    ----    "
                        + method;
            } else {
                content = " ## " + "                ----    " + method;
            }
            Log.d(tag, content);
            if (DEBUG_SYSOUT) {
                System.out.println(tag + "  " + content + "  " + method);
            }
        }
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param object The object to print.
     */
    public static void printError(Object object) {
        if (Log.ERROR > DEBUG_LEVEL) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String content = "";
            if (object != null) {
                content = object.toString() + "                    ----    "
                        + method;
            } else {
                content = " ## " + "                    ----    " + method;
            }
            Log.e(tag, content);
            if (DEBUG_SYSOUT) {
                System.err.println(tag + "  " + method + "  " + content);
            }
        }
    }

    /**
     * Print the array of stack trace elements of this method in black.
     *
     * @return
     */
    public static void printCallHierarchy() {
        if (Log.VERBOSE > DEBUG_LEVEL) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String hierarchy = getCallHierarchy();
            Log.v(tag, method + hierarchy);
            if (DEBUG_SYSOUT) {
                System.out.println(tag + "  " + method + hierarchy);
            }
        }
    }

    /**
     * Print debug log in blue.
     *
     * @param object The object to print.
     */
    public static void printMyLog(Object object) {
        if (Log.DEBUG > DEBUG_LEVEL) {
            String tag = "MYLOG";
            String method = callMethodAndLine();
            String content = "";
            if (object != null) {
                content = object.toString() + "                    ----    "
                        + method;
            } else {
                content = " ## " + "                ----    " + method;
            }
            Log.d(tag, content);
            if (DEBUG_SYSOUT) {
                System.out.println(tag + "  " + content + "  " + method);
            }
        }
    }

    private static String getCallHierarchy() {
        String result = "";
        StackTraceElement[] trace = (new Exception()).getStackTrace();
        for (int i = 2; i < trace.length; i++) {
            result += "\r\t" + trace[i].getClassName() + ""
                    + trace[i].getMethodName() + "():"
                    + trace[i].getLineNumber();
        }
        return result;
    }

    private static String getClassName() {
        String result = "";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = thisMethodStack.getClassName();
        return result;
    }

    /**
     * Realization of double click jump events.
     *
     * @return
     */
    private static String callMethodAndLine() {
        String result = "at ";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result += thisMethodStack.getClassName() + "";
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")  ";
        return result;
    }

    /*private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }*/

    private static boolean logSwitch      = true;
    private static boolean log2FileSwitch = false;
    private static char    logFilter      = 'v';
    private static String  tag            = "TAG";
    private static String  dir            = null;

    /**
     * 初始化函数
     * <p>与{@link #getBuilder()}两者选其一</p>
     *
     * @param logSwitch      日志总开关
     * @param log2FileSwitch 日志写入文件开关
     * @param logFilter      输入日志类型有{@code v, d, i, w, e}<br>v代表输出所有信息，w则只输出警告...
     * @param tag            标签
     */
    public static void init(boolean logSwitch, boolean log2FileSwitch, char logFilter, String tag) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator;
        }
        LogUtils.logSwitch = logSwitch;
        LogUtils.log2FileSwitch = log2FileSwitch;
        LogUtils.logFilter = logFilter;
        LogUtils.tag = tag;
    }

    /**
     * 获取LogUtils建造者
     * <p>与{@link #init(boolean, boolean, char, String)}两者选其一</p>
     *
     * @return Builder对象
     */
    public static LogUtils.Builder getBuilder() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator + "log" + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator + "log" + File.separator;
        }
        return new LogUtils.Builder();
    }

    public static class Builder {

        private boolean logSwitch      = true;
        private boolean log2FileSwitch = false;
        private char    logFilter      = 'v';
        private String  tag            = "TAG";

        public LogUtils.Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public LogUtils.Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public LogUtils.Builder setLogFilter(char logFilter) {
            this.logFilter = logFilter;
            return this;
        }

        public LogUtils.Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public void create() {
            LogUtils.logSwitch = logSwitch;
            LogUtils.log2FileSwitch = log2FileSwitch;
            LogUtils.logFilter = logFilter;
            LogUtils.tag = tag;
        }
    }

    /**
     * Verbose日志
     *
     * @param msg 消息
     */
    /*public static void v(Object msg) {
        v(tag, msg);
    }*/

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    /**
     * Verbose日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * Debug日志
     *
     * @param msg 消息
     */
    /*public static void d(Object msg) {
        d(tag, msg);
    }*/

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    /**
     * Debug日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /**
     * Info日志
     *
     * @param msg 消息
     */
    /*public static void i(Object msg) {
        i(tag, msg);
    }*/

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    /**
     * Info日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /**
     * Warn日志
     *
     * @param msg 消息
     */
    /*public static void w(Object msg) {
        w(tag, msg);
    }*/

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    /**
     * Warn日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /**
     * Error日志
     *
     * @param msg 消息
     */
    /*public static void e(Object msg) {
        e(tag, msg);
    }*/

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     */
    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    /**
     * Error日志
     *
     * @param tag 标签
     * @param msg 消息
     * @param tr  异常
     */
    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag  标签
     * @param msg  消息
     * @param tr   异常
     * @param type 日志类型
     */
    private static void log(String tag, String msg, Throwable tr, char type) {
        if (logSwitch) {
            if ('e' == type && ('e' == logFilter || 'v' == logFilter)) {
                Log.e(tag, msg, tr);
            } else if ('w' == type && ('w' == logFilter || 'v' == logFilter)) {
                Log.w(tag, msg, tr);
            } else if ('d' == type && ('d' == logFilter || 'v' == logFilter)) {
                Log.d(tag, msg, tr);
            } else if ('i' == type && ('d' == logFilter || 'v' == logFilter)) {
                Log.i(tag, msg, tr);
            }
            if (log2FileSwitch) {
                log2File(type, tag, msg + '\n' + Log.getStackTraceString(tr));
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param type    日志类型
     * @param tag     标签
     * @param content 内容
     **/
    private synchronized static void log2File(final char type, final String tag, final String content) {
        if (content == null) return;
        Date now = new Date();
        String date = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(now);
        final String fullPath = dir + date + ".txt";
        if (!com.ryanh.ryanutils.utils.FileUtils.createOrExistsFile(fullPath)) return;
        String time = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(now);
        final String dateLogContent = time + ":" + type + ":" + tag + ":" + content + '\n';
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(dateLogContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(bw);
                }
            }
        }).start();
    }

}
