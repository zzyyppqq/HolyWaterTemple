package com.holywatertemple.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.holywatertemple.R;
import com.holywatertemple.ui.base.BaseActivity;

import butterknife.BindView;


public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.jumpActivity(SplashActivity.this, null);
                finish();
            }
        }, 1000);
    }
}
