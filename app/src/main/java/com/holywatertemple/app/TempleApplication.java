package com.holywatertemple.app;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.holywatertemple.BuildConfig;
import com.holywatertemple.util.PackageUtil;
import com.squareup.leakcanary.LeakCanary;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class TempleApplication extends Application{
    public static final String TAG = "TempleApplication";

    /**
     * 维护Activity的List
     */
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    private static TempleApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        /**打开严苛模式，严苛模式主要检测两大问题，一个是线程策略，即TreadPolicy，另一个是VM策略，即VmPolicy。*/
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        //如果不是同一个进程return
        if (!PackageUtil.getCurProcessName(this).equals(getPackageName())) return;
    }



    public static TempleApplication getApplication() {
        return mApplication;
    }

    public static List<Activity> getActivitys() {
        return mActivitys;
    }

    public static void finishAllActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
    }


    /**
     * 获取当前Activity（栈中最后一个压入得）
     */
    public static Activity getCurrentActivity() {
        if (mActivitys == null || mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size() - 1);
        return activity;
    }

    /**
     * 注册ActivityListener
     */
    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (null == mActivitys) {
                        return;
                    }
                    mActivitys.add(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                }

                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null == activity && mActivitys.isEmpty()) {
                        return;
                    }
                    if (mActivitys.contains(activity)) {
                        mActivitys.remove(activity);
                    }
                }
            });
        }
    }
}
