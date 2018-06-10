package com.holywatertemple.util;

import android.widget.Toast;

import com.holywatertemple.app.TempleApplication;

public class ToastUtil {
    public static void showToast(String msg){
        Toast.makeText(TempleApplication.getApplication(),msg,Toast.LENGTH_SHORT).show();
    }
}
