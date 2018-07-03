package com.holywatertemple.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.holywatertemple.BuildConfig;
import com.holywatertemple.Config;
import com.holywatertemple.R;
import com.holywatertemple.excel.ExcelManger;
import com.holywatertemple.java_lib.Main;
import com.holywatertemple.ui.base.BaseActivity;
import com.holywatertemple.ui.fragment.HomeFragment;
import com.holywatertemple.ui.fragment.InputFragment;
import com.holywatertemple.ui.fragment.MessageFragment;
import com.holywatertemple.util.Logger;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private HomeFragment mHomeFragment;
    private InputFragment mInputFragment;
    private MessageFragment mMessageFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setCurrentNav(NavIndex.ONE);
                    return true;
                case R.id.navigation_dashboard:
                    setCurrentNav(NavIndex.TWO);
                    return true;
                case R.id.navigation_notifications:
                    setCurrentNav(NavIndex.THREE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setCurrentNav(NavIndex.ONE);

    }

    public void setNav(@IdRes int itemId) {
        if (navigation != null) {
            navigation.setSelectedItemId(itemId);
        }
    }

    @IntDef({NavIndex.ONE, NavIndex.TWO, NavIndex.THREE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavIndex {
        int ONE = 0;
        int TWO = 1;
        int THREE = 2;
    }

    /**
     * 初始化一个值-1 （小于0即可）
     */
    private int curPageIndex = -1;

    public void setCurrentNav(@NavIndex int navIndex) {
        if (curPageIndex == navIndex) {
            if (BuildConfig.DEBUG) Logger.d(TAG, "点击的是当前的tab，不切换 retrun");
            return;
        }
        if (fm == null) fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        hideAllFragment(ft);
        curPageIndex = navIndex;
        switch (navIndex) {
            case NavIndex.ONE:
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.fl_container, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                    mHomeFragment.show();
                }
                break;
            case NavIndex.TWO:
                if (null == mInputFragment) {
                    mInputFragment = new InputFragment();
                    ft.add(R.id.fl_container, mInputFragment);
                } else {
                    ft.show(mInputFragment);
                }
                break;

            case NavIndex.THREE:
                if (null == mMessageFragment) {
                    mMessageFragment = new MessageFragment();
                    ft.add(R.id.fl_container, mMessageFragment);
                } else {
                    ft.show(mMessageFragment);
                    mMessageFragment.show();
                }
                break;

        }
        ft.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null && mHomeFragment.isVisible()) {
            transaction.hide(mHomeFragment);
        }
        if (mInputFragment != null && mInputFragment.isVisible()) {
            transaction.hide(mInputFragment);
        }
        if (mMessageFragment != null && mMessageFragment.isVisible()) {
            transaction.hide(mMessageFragment);
        }
    }

    public static void jumpActivity(Context context, Bundle bundle) {
        final Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public void homeRecyclerViewScrollBottom(){
        if (mHomeFragment != null){
            mHomeFragment.recyclerViewScrollBottom();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            mMessageFragment.onFilePath(filePath);
        }
    }

}
