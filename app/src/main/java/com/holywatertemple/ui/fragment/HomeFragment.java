package com.holywatertemple.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.holywatertemple.BuildConfig;
import com.holywatertemple.R;
import com.holywatertemple.app.TempleApplication;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.handler.HandlerThreadManager;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.ui.base.BaseFragment;
import com.holywatertemple.ui.dialog.EditDialogActivity;
import com.holywatertemple.ui.fragment.adapter.PersonAdapter;
import com.holywatertemple.util.AppSharePref;
import com.holywatertemple.util.DimensUtil;
import com.holywatertemple.util.Logger;
import com.holywatertemple.util.SmsUtil;
import com.holywatertemple.util.ToastUtil;
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

import static com.holywatertemple.ui.dialog.EditDialogActivity.PERSON_DATA;
import static com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LEFT_DIRECTION;
import static com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.RIGHT_DIRECTION;

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
    @BindView(R.id.bt_batch_send_msg)
    Button btBatchSendMsg;
    @BindView(R.id.bt_remain_day_query)
    Button btRemainDayQuery;
    private PersonAdapter personAdapter;
    private ArrayAdapter<String> adapter;
    private String like;
    private int mSendSmsDay;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rbAll.setChecked(true);

        initSwipeRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        show();
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
                deleteItem.setWidth((int) DimensUtil.dp2px(getContext(), 80f));
                deleteItem.setHeight((int) DimensUtil.dp2px(getContext(), 130f));
                deleteItem.setText("删除");
                deleteItem.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                deleteItem.setBackgroundColorResource(android.R.color.holo_red_light);
                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem editItem = new SwipeMenuItem(getContext());
                editItem.setWidth((int) DimensUtil.dp2px(getContext(), 80f));
                editItem.setHeight((int) DimensUtil.dp2px(getContext(), 130f));
                editItem.setText("编辑");
                editItem.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                editItem.setBackgroundColorResource(android.R.color.holo_green_light);
                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(editItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem phoneItem = new SwipeMenuItem(getContext());
                phoneItem.setWidth((int) DimensUtil.dp2px(getContext(), 80f));
                phoneItem.setHeight((int) DimensUtil.dp2px(getContext(), 130f));
                phoneItem.setText("短信");
                phoneItem.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                phoneItem.setBackgroundColorResource(android.R.color.holo_orange_light);
                leftMenu.addMenuItem(phoneItem); // 在Item左侧添加一个菜单。
            }
        });

        // 菜单点击监听。
        swipeRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int position = menuBridge.getPosition();
                int direction = menuBridge.getDirection();
                final PersonData personData = personAdapter.getDatas().get(menuBridge.getAdapterPosition());
                if (direction == LEFT_DIRECTION) {
                    alertPhoneDialog(personData);
                } else if (direction == RIGHT_DIRECTION) {
                    if (position == 0) {//删除
                        alertDeleteDialog(personData);
                    } else if (position == 1) {//编辑
                        alertEditDialog(personData);
                    }
                }
            }
        });
        personAdapter = new PersonAdapter();
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRecyclerView.setAdapter(personAdapter);

        personAdapter.addBtnClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, PersonData personData) {
                alertPhoneDialog(personData);
            }
        });
    }

    private void alertEditDialog(PersonData personData) {
        Person person = personData.convertPerson();
        final Intent intent = new Intent(getContext(), EditDialogActivity.class);
        intent.putExtra(PERSON_DATA, person);
        startActivity(intent);
    }

    private void alertPhoneDialog(final PersonData personData) {
        final String sms = AppSharePref.getInstance().getSms();
        if (TextUtils.isEmpty(sms)) {
            ToastUtil.showToast("短信内容不能为空");
            return;
        }
        final String msg = sms.replace("name", personData.getName());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")//设置title
                .setMessage("是否发送短信给" + personData.getName() + "?\r\n【" + msg + "】")//设置要显示的message
                .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String phoneNum = personData.getPhoneNum();
                                if (phoneNum.trim() == null || phoneNum.trim().equals("")) {
                                    Toast.makeText(getActivity(), "对不起，手机号不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (phoneNum.trim() != null && !(phoneNum.trim().equals(""))) {

                                    if (SmsUtil.isExistSimCard(TempleApplication.getApplication())) {
                                        SmsUtil.sendSMS(getActivity(), phoneNum, msg);
                                    }else {
                                        ToastUtil.showToast("sim不存在");
                                    }
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
    }


    private void alertDeleteDialog(final PersonData personData) {
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
    protected void initData() {
        mSendSmsDay = AppSharePref.getInstance().getSendSmsDay();
        btRemainDayQuery.setText("小于" + mSendSmsDay + "天");
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

                datas = HolyDBManager.getInstance(getContext()).queryDataByJossTypeAndSearch(jossType, type, etInput.getText().toString());

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

    private void queryRemainDayData() {
        Observable.create(new Observable.OnSubscribe<List<PersonData>>() {
            @Override
            public void call(Subscriber<? super List<PersonData>> subscriber) {
                Logger.e(TAG, Thread.currentThread().getName());
                List<PersonData> datas = null;

                datas = HolyDBManager.getInstance(getContext()).queryDataByRemainDay(mSendSmsDay);

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

    private void queryRemainDayDataAndSendSms() {
        Observable.create(new Observable.OnSubscribe<List<PersonData>>() {
            @Override
            public void call(Subscriber<? super List<PersonData>> subscriber) {
                Logger.e(TAG, Thread.currentThread().getName());
                List<PersonData> datas = null;

                datas = HolyDBManager.getInstance(getContext()).queryDataByRemainDay(mSendSmsDay);

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

                        for (PersonData personData : result) {
                            Logger.e(TAG, personData.getPhoneNum() + "");
                            if (!TextUtils.isEmpty(personData.getPhoneNum())) {
                                HandlerThreadManager.getInstance().sendMsg(personData);
                            }
                        }
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

    @OnClick({R.id.rb_all, R.id.rb_use, R.id.rb_no_use, R.id.bt_remain_day_query, R.id.bt_batch_send_msg})
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
            case R.id.bt_remain_day_query:
                queryRemainDayData();
                break;
            case R.id.bt_batch_send_msg:
                //批量发送短信
                alertSendSmsDialog();
                break;
        }
    }

    private void alertSendSmsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("温馨提示")//设置title
                .setMessage("是否批量发送短信 ？")//设置要显示的message
                .setCancelable(false)//表示点击dialog其它部分不能取消(除了“取消”，“确定”按钮)
                .setPositiveButton("确定", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                queryRemainDayDataAndSendSms();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
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

    public void recyclerViewScrollBottom() {
        if (swipeRecyclerView != null) {
            swipeRecyclerView.scrollToPosition(personAdapter.getItemCount());
        }
    }
}
