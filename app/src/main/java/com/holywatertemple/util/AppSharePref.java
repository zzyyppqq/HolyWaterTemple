package com.holywatertemple.util;

import android.content.SharedPreferences;

/**
 * Created by PanJiafang on 16/9/13.
 * 网络sp类,储存一些网络header信息
 */
@SuppressWarnings("unused")
public class AppSharePref {
    public static final String TAG = "AppSharePref";
    private static AppSharePref instance = null;
    private SharedPreferences appSP;
    private static final String APP_SP = "app_sp";

    private AppSharePref() {
        appSP = SPUtils.getSP(APP_SP);
    }

    public static AppSharePref getInstance() {
        if (instance == null) {
            synchronized (AppSharePref.class) {
                if (instance == null) instance = new AppSharePref();
            }
        }
        return instance;
    }

    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        appSP.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {
        appSP.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void setTableInfo(String tableInfo) {
        SPUtils.putStringSync(appSP, "table_info", tableInfo);
    }

    public String getTableInfo(){
        return appSP.getString("table_info","");
    }

}
