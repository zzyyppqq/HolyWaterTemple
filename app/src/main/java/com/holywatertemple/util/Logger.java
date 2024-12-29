package com.holywatertemple.util;

import android.util.Log;



/**
 * @author wanghaoyue
 */
public class Logger {

    private static boolean IS_PRINT_LOG = true;

    public static void setLoggerSwitch(final boolean isLogOn) {
        IS_PRINT_LOG = isLogOn;
    }

    private static String logFormat(Object... args) {
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] == null)) {
                if (i > 0) {
                    sb1.append(" ");
                }
                sb1.append(args[i]);
            }
        }

        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();

        sb.append("(")
                .append(stackTrace.getFileName())
                .append(":")
                .append(stackTrace.getLineNumber())
                .append(")")
                .append("#")
                .append(stackTrace.getMethodName())
                .append(":")
                .append(unicode2GBK(sb1.toString()));
        return sb.toString();
    }

    public static String getNoneSystemCallingChain() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        String className = null;
        StackTraceElement cur;
        for(int i=traces.length-1; i>2; i--) {
            cur = traces[i];
            className = cur.getClassName();
            if(className.contains("android") || className.contains("java")) {
                continue;
            }
            sb.append("(").append(cur.getFileName()).append(":").append(cur.getLineNumber()).append(")").append("#").append(cur.getMethodName());
            if(i != 3)
                sb.append("->");
        }
        return sb.toString();
    }

    public static void v(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.v(tag, logFormat(messages));
        }
    }

    public static void d(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.d(tag, logFormat(messages));
        }
    }

    public static void i(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.i(tag, logFormat(messages));
        }
    }

    /**
     * 程序crash后捕获log干扰信息太多，修改为Log.d() 如需要使用Log.e()函数，调用Logger.error();
     *
     * @param tag
     * @param messages
     */
    public static void e(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.e(tag, logFormat(messages));
        }
    }

    public static void error(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.e(tag, logFormat(messages));
        }
    }

    public static void w(String tag, Object... messages) {
        if (IS_PRINT_LOG) {
            Log.w(tag, logFormat(messages));
        }
    }

    public static void log(String str) {
        if (IS_PRINT_LOG) {
            System.out.print(str + "\n");
        }
    }

    public static void eSuper(String tag, String info) {
        StackTraceElement[] ste = new Throwable().getStackTrace();
        int i = 1;
        StackTraceElement s = ste[i];
        String className = s.getClassName().contains(".") ? s
                .getClassName().substring(
                        s.getClassName().lastIndexOf("."),
                        s.getClassName().length()) : s.getClassName();

        Log.e(tag, String.format("======[%s][%s][%s]=====%s",
                className, s.getLineNumber(), s.getMethodName(),
                info));
    }


    /**
     * unicode转gbk
     *
     * @param dataStr
     * @return
     */
    public static String unicode2GBK(String dataStr) {
        int index = 0;
        StringBuilder builder = new StringBuilder();

        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1||index + 6 > li_len
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                builder.append(dataStr.charAt(index));

                index++;
                continue;
            }

            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);

            char letter = (char) Integer.parseInt(charStr, 16);

            builder.append(letter);
            index += 6;
        }

        return builder.toString();
    }

}
