package com.holywatertemple.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initView();

        initListener();

        initData(savedInstanceState);

    }

    protected abstract View getContentView();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
