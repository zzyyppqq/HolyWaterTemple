package com.holywatertemple.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class PackageUtil {
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return "";
    }
}
