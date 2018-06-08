package com.holywatertemple.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentId(), container, false);
        ButterKnife.bind(mView);
        
        initView(savedInstanceState);
        
        initListener();
        
        initData();
        
        
        return mView;
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract int getFragmentId();

}
