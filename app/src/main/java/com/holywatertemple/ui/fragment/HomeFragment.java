package com.holywatertemple.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.holywatertemple.BuildConfig;
import com.holywatertemple.R;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.ui.base.BaseFragment;
import com.holywatertemple.ui.fragment.adapter.PersonAdapter;
import com.holywatertemple.util.DimensUtil;
import com.holywatertemple.util.Logger;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class HomeFragment extends BaseFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final int QUERY_ALL = 1;
    private static final int QUERY_LIKE = 2;

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_use)
    RadioButton rbUse;
    @BindView(R.id.rb_no_use)
    RadioButton rbNoUse;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.spinner)
    Spinner spinner;
    Unbinder unbinder;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.swipeRecyclerView)
    SwipeMenuRecyclerView swipeRecyclerView;
    private PersonAdapter personAdapter;
    private ArrayAdapter<String> adapter;
    private String like;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rbAll.setChecked(true);

        initSwipeRecyclerView();
    }

    private void initSwipeRecyclerView() {
        swipeRecyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO，搞事情...
            }
        });
        // 设置监听器。
        swipeRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setWidth((int) DimensUtil.dp2px(getContext(),130f));
                deleteItem.setHeight((int) DimensUtil.dp2px(getContext(),130f));
                deleteItem.setImage(R.mipmap.delete);
                deleteItem.setBackgroundColorResource(R.color.gray);
                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            }
        });

        // 菜单点击监听。
        swipeRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                alertDialog(personAdapter.getDatas().get(menuBridge.getAdapterPosition()));
            }
        });
        personAdapter = new PersonAdapter();
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRecyclerView.setAdapter(personAdapter);
    }

    @Override
    protected void initData() {
        etInput.setText("");
        spinner();
        queryDB();
    }


    private String jossType = "所有类型";

    private void spinner() {
        if (BuildConfig.DEBUG) Logger.e(TAG, "初始化apinner");
        jossType = "所有类型";
        //为dataList赋值，将下面这些数据添加到数据源中
        List<String> dataList = new ArrayList<>();
        dataList.add("所有类型");
        dataList.add("释迦牟尼佛");
        dataList.add("药师佛");
        dataList.add("阿弥陀佛");
        dataList.add("文殊菩萨");
        dataList.add("地藏菩萨");
        dataList.add("观音菩萨");

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        spinner.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (BuildConfig.DEBUG) Logger.e(TAG, "您当前选择的是：" + adapter.getItem(position));
                jossType = adapter.getItem(position);

                queryFilterData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (BuildConfig.DEBUG) Logger.e(TAG, "请选择您的城市");

            }
        });

    }

    private void queryFilterData() {
        Observable.create(new Observable.OnSubscribe<List<PersonData>>() {
            @Override
            public void call(Subscriber<? super List<PersonData>> subscriber) {
                Logger.e(TAG, Thread.currentThread().getName());
                List<PersonData> datas = null;

                datas = HolyDBManager.getInstance(getContext()).queryDataByJossType(jossType, type);

                if (datas == null) {
                    subscriber.onError(new Throwable("datas == null"));
                } else {
                    subscriber.onNext(datas);
                }
                subscriber.onCompleted();
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<PersonData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PersonData> result) {
                        //加载数据
                        if (BuildConfig.DEBUG) Logger.e(TAG, result);
                        Logger.e(TAG, "size: " + result.size());
                        tvCount.setText(result.size() + "");
                        personAdapter.setDatas(result, "");
                    }
                });
    }

    private void queryData(final int queryType, final String like) {

        Observable.create(new Observable.OnSubscribe<List<PersonData>>() {
            @Override
            public void call(Subscriber<? super List<PersonData>> subscriber) {
                Logger.e(TAG, Thread.currentThread().getName());
                List<PersonData> datas = null;
                if (queryType == QUERY_ALL) {
                    datas = HolyDBManager.getInstance(getContext()).queryAllData();
                } else if (queryType == QUERY_LIKE) {
                    datas = HolyDBManager.getInstance(getContext()).queryLikeData(like);
                }

                if (datas == null) {
                    subscriber.onError(new Throwable("datas == null"));
                } else {
                    subscriber.onNext(datas);
                }
                subscriber.onCompleted();
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<PersonData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PersonData> result) {
                        //加载数据
                        if (BuildConfig.DEBUG) Logger.e(TAG, result);
                        Logger.e(TAG, "size: " + result.size());
                        tvCount.setText(result.size() + "");
                        personAdapter.setDatas(result, like);
                    }
                });

    }

    private int type = ALL;
    public static final int ALL = 1;
    public static final int USE = 2;
    public static final int NO_USE = 3;

    @Override
    protected void initListener() {
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryDB();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rb_all:

                        break;
                    case R.id.rb_use:

                        break;
                    case R.id.rb_no_use:

                        break;
                    default:

                        break;
                }
            }

        });
    }

    @OnClick({R.id.rb_all, R.id.rb_use, R.id.rb_no_use})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_all:
                type = ALL;
                queryFilterData();
                break;
            case R.id.rb_use:
                type = USE;
                queryFilterData();
                break;
            case R.id.rb_no_use:
                type = NO_USE;
                queryFilterData();
                break;
        }
    }

    private void queryDB() {
        like = etInput.getText().toString();
        if (TextUtils.isEmpty(like)) {
            queryData(QUERY_ALL, "");
        } else {
            queryData(QUERY_LIKE, etInput.getText().toString());
        }
    }

    public void show() {
        initData();
    }


    private void alertDialog(final PersonData personData) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")//设置title
                .setMessage("是否从供养人中删除【" + personData.getName() + "】?")//设置要显示的message
                .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HolyDBManager.getInstance(getContext()).clearDataWithoutJossId(personData.getJossId());
                                initData();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
