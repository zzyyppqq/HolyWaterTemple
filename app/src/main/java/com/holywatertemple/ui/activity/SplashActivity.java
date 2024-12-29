package com.holywatertemple.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.holywatertemple.R;
import com.holywatertemple.databinding.ActivityEditDialogBinding;
import com.holywatertemple.databinding.ActivitySplashBinding;
import com.holywatertemple.ui.base.BaseActivity;


public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View getContentView() {
        return binding.getRoot();
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
