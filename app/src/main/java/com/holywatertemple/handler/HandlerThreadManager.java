package com.holywatertemple.handler;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.holywatertemple.app.TempleApplication;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.util.AppSharePref;
import com.holywatertemple.util.Logger;
import com.holywatertemple.util.SPUtils;
import com.holywatertemple.util.SmsUtil;
import com.holywatertemple.util.ToastUtil;

/**
 * Created by zhangyiipeng on 2018/7/3.
 */

public class HandlerThreadManager {
    public static final String TAG = "HandlerThreadManager";

    private static HandlerThreadManager instance = null;
    private Handler mWorkerHandler;

    public static HandlerThreadManager getInstance() {
        if (instance == null) {
            synchronized (HandlerThreadManager.class) {
                if (instance == null) instance = new HandlerThreadManager();
            }
        }
        return instance;
    }

    private HandlerThreadManager() {
        init();
    }

    private void init() {
        HandlerThread handlerThread = new HandlerThread("handler_send_sms"){
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();

            }
        };
        handlerThread.start();
        mWorkerHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        PersonData personData = (PersonData) msg.obj;
                        final String phoneNum = personData.getPhoneNum();
                        final String sms = AppSharePref.getInstance().getSms();
                        if (TextUtils.isEmpty(sms)){
                            ToastUtil.showToast("短信内容不能为空");
                            return;
                        }
                        if (TextUtils.isEmpty(phoneNum)){
                            ToastUtil.showToast("手机号码不能为空");
                            return;
                        }
                        final String message = sms.replace("name", personData.getName());
                        Logger.e(TAG,phoneNum+" , "+message);
                        if (SmsUtil.isExistSimCard(TempleApplication.getApplication())) {
                            SmsUtil.sendSMS(TempleApplication.getApplication(), phoneNum, message);
                        }else {
                            ToastUtil.showToast("sim不存在");
                        }

                        break;
                }
            }

            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
            }
        };
    }

    public void sendMsg(PersonData object) {
        final Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = object;
        mWorkerHandler.sendMessage(msg);
    }


}
