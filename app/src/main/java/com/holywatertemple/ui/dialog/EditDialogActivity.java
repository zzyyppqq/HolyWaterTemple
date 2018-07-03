package com.holywatertemple.ui.dialog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.holywatertemple.BuildConfig;
import com.holywatertemple.R;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.ui.base.BaseActivity;
import com.holywatertemple.ui.fragment.InputFragment;
import com.holywatertemple.util.Logger;
import com.holywatertemple.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhangyiipeng on 2018/7/3.
 */

public class EditDialogActivity extends BaseActivity {

    public static final String TAG = InputFragment.class.getSimpleName();
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tv_feed_time)
    TextView tvFeedTime;
    @BindView(R.id.tv_extend_time)
    TextView tvExtendTime;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_feed_price)
    EditText etFeedPrice;
    @BindView(R.id.bt_commit)
    Button btCommit;
    Unbinder unbinder;

    public static final String PERSON_DATA = "person_data";

    //定义一个String类型的List数组作为数据源
    private List<String> dataList;

    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter;
    private String jossId;
    private String name;
    private String phoneNum;
    private String feedPrice;
    private String fendTime;
    private String extendTime;
    private Person mPerson;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_dialog;
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData(Bundle bundle) {
        final Intent intent = getIntent();
        if (intent != null) {
            mPerson = (Person) intent.getSerializableExtra(PERSON_DATA);
        }
        spinner();

        etId.setText(mPerson.getJossId());
        etName.setText(mPerson.getName());
        etPhoneNum.setText(mPerson.getPhoneNum());
        etFeedPrice.setText(mPerson.getFendPrice());
        final String extendTime = mPerson.getExtendTime();
        if (TextUtils.isEmpty(extendTime)){
            tvExtendTime.setText("选择日期");
        }else {
            tvExtendTime.setText(extendTime);
        }
        final String fendTime = mPerson.getFendTime();
        if (TextUtils.isEmpty(fendTime)){
            tvFeedTime.setText("选择日期");
        }else {
            tvFeedTime.setText(fendTime);
        }
        final String jossType = mPerson.getJossType();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).equals(jossType)){
                spinner.setSelection(i, true);
                break;
            }
        }
    }


    @Override
    protected void initListener() {

    }


    @OnClick({R.id.tv_feed_time, R.id.tv_extend_time, R.id.bt_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_feed_time:
                showDatePickerDialog(tvFeedTime);
                break;
            case R.id.tv_extend_time:
                showDatePickerDialog(tvExtendTime);
                break;
            case R.id.bt_commit:
                if (verty()) {
                    HolyDBManager.getInstance(this).updateData(jossId, new Person(jossId, name, phoneNum, jossType, feedPrice, fendTime, extendTime,0));
//                    clearAllText();
                    ToastUtil.showToast("编辑成功");
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private boolean verty() {
        jossId = etId.getText().toString();
        name = etName.getText().toString();
        phoneNum = etPhoneNum.getText().toString();
        feedPrice = etFeedPrice.getText().toString();
        fendTime = tvFeedTime.getText().toString();
        extendTime = tvExtendTime.getText().toString();
        if ("选择日期".equals(fendTime)){
            fendTime = "";
        }
        if ("选择日期".equals(extendTime)){
            extendTime = "";
        }

        if (TextUtils.isEmpty(jossId)) {
            ToastUtil.showToast("序号不能为空");
            return false;
        }

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名为空");
            return false;
        }
        if (TextUtils.isEmpty(jossType) || "选择佛像种类".equals(jossType)) {
            ToastUtil.showToast("请选择佛像种类");
            return false;
        }

//        if (TextUtils.isEmpty(phoneNum)) {
//            ToastUtil.showToast("手机号不能为空");
//            return false;
//        }
        if (TextUtils.isEmpty(feedPrice)) {
            ToastUtil.showToast("供养金额不能为空");
            return false;
        }
        if (TextUtils.isEmpty(fendTime)) {
            ToastUtil.showToast("供养时间不能为空");
            return false;
        }
//        if (TextUtils.isEmpty(extendTime)) {
//            ToastUtil.showToast("到期时间不能为空");
//            return false;
//        }


        return true;
    }

    private boolean isUseJossId(String jossId) {
        PersonData personData = HolyDBManager.getInstance(this).queryDataByJossId(jossId);
        boolean is = false;
        if (personData != null) {
            String name = personData.getName();
            if (!TextUtils.isEmpty(name)) {
                is = true;
            }
        }
        return is;
    }

    private void clearAllText() {
        etFeedPrice.setText("");
        etPhoneNum.setText("");
        etId.setText("");
        etName.setText("");
        tvExtendTime.setText("");
        tvFeedTime.setText("选择日期");
        tvExtendTime.setText("选择日期");

        spinner();
    }

    private void showDatePickerDialog(final TextView tv) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                tv.setText(year + "年" + (++month) + "月" + day + "日");      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
    }


    private String jossType = "选择佛像种类";

    private void spinner() {
        jossType = "选择佛像种类";
        //为dataList赋值，将下面这些数据添加到数据源中
        dataList = new ArrayList<String>();
        dataList.add("选择佛像种类");
        dataList.add("释迦牟尼佛");
        dataList.add("药师佛");
        dataList.add("阿弥陀佛");
        dataList.add("文殊菩萨");
        dataList.add("地藏菩萨");
        dataList.add("观音菩萨");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (BuildConfig.DEBUG) Logger.e(TAG, "请选择您的城市");

            }
        });

    }

}
