package com.holywatertemple.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);

        initView();

        initListener();

        initData(savedInstanceState);


    }

    protected abstract int getContentViewId();

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
