package com.holywatertemple.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.holywatertemple.R;
import com.holywatertemple.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

}
