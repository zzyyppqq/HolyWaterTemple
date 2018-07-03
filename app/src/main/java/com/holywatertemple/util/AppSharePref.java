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


    public void setSendSmsDay(int day) {
        SPUtils.putIntSync(appSP, "send_sms_day", day);
    }

    public int getSendSmsDay(){
        return appSP.getInt("send_sms_day",30);
    }

    public void setSms(String sms) {
        SPUtils.putStringSync(appSP, "sms", sms);
    }

    public String getSms() {
        return appSP.getString("sms","尊敬的name居士您好，您供养的圣水寺佛像快到期啦！");
    }
}
