package com.holywatertemple.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentId(), container, false);
        initBinding(mView);
        initView(savedInstanceState);
        
        initListener();
        
        initData();
        
        
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    protected abstract void initBinding(View view);

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract int getFragmentId();

}
