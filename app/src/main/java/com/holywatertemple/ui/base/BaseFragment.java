package com.holywatertemple.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentId(), container, false);
        bind = ButterKnife.bind(this, mView);

        initView(savedInstanceState);
        
        initListener();
        
        initData();
        
        
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }



    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract int getFragmentId();

}
