package com.holywatertemple.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.holywatertemple.Config;
import com.holywatertemple.R;
import com.holywatertemple.databinding.ActivityMainBinding;
import com.holywatertemple.excel.ExcelManger;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.java_lib.Main;
import com.holywatertemple.ui.base.BaseActivity;
import com.holywatertemple.ui.fragment.HomeFragment;
import com.holywatertemple.ui.fragment.InputFragment;
import com.holywatertemple.ui.fragment.MessageFragment;
import com.holywatertemple.util.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class MainActivity extends BaseActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private HomeFragment mHomeFragment;
    private InputFragment mInputFragment;
    private MessageFragment mMessageFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                setCurrentNav(NavIndex.ONE);
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                setCurrentNav(NavIndex.TWO);
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                setCurrentNav(NavIndex.THREE);
                return true;
            }
            return false;
        }
    };

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View getContentView() {
        return binding.getRoot();
    }


    @Override
    protected void initView() {
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setCurrentNav(NavIndex.ONE);

    }

    public void setNav(@IdRes int itemId) {
        if (binding.navigation != null) {
            binding.navigation.setSelectedItemId(itemId);
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
            Logger.d(TAG, "点击的是当前的tab，不切换 retrun");
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

//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//            // Do anything with file
//            mMessageFragment.onFilePath(filePath);
//        }
    }


    @Override
    public void onBackPressed() {
        alertExitAppDialog();
    }

    private void alertExitAppDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setTitle("温馨提示")//设置title
                .setMessage("是否退出应用？")//设置要显示的message
                .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
    }
}
