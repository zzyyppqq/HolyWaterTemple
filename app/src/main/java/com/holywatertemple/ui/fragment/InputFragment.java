package com.holywatertemple.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.holywatertemple.R;
import com.holywatertemple.databinding.FragmentInputBinding;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.java_lib.Main;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.ui.activity.MainActivity;
import com.holywatertemple.ui.base.BaseFragment;
import com.holywatertemple.util.Logger;
import com.holywatertemple.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class InputFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = InputFragment.class.getSimpleName();

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

    private FragmentInputBinding binding;

    @Override
    protected void initBinding(View view) {
        binding = FragmentInputBinding.bind(view);
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_input;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        spinner();
    }


    @Override
    protected void initListener() {
        binding.tvFeedTime.setOnClickListener(this);
        binding.tvExtendTime.setOnClickListener(this);
        binding.btCommit.setOnClickListener(this);
    }

    //@OnClick({R.id.tv_feed_time, R.id.tv_extend_time, R.id.bt_commit})
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_feed_time) {
            showDatePickerDialog(binding.tvFeedTime);
        } else if (view.getId() == R.id.tv_extend_time) {
            showDatePickerDialog(binding.tvExtendTime);
        } else if (view.getId() == R.id.tv_extend_time) {
            if (verty()) {
                HolyDBManager.getInstance(getContext()).insertData(new Person(jossId, name, phoneNum, jossType, feedPrice, fendTime, extendTime, 0));
                clearAllText();
                ToastUtil.showToast("添加成功");

                switchHomeFragment();
            }
        } else {

        }
    }

    private void switchHomeFragment() {
        if (getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setNav(R.id.navigation_home);
//            mainActivity.setCurrentNav(MainActivity.NavIndex.ONE);
            mainActivity.homeRecyclerViewScrollBottom();
        }

    }

    private boolean verty() {
        jossId = binding.etId.getText().toString();
        name = binding.etName.getText().toString();
        phoneNum = binding.etPhoneNum.getText().toString();
        feedPrice = binding.etFeedPrice.getText().toString();
        fendTime = binding.tvFeedTime.getText().toString();
        extendTime = binding.tvExtendTime.getText().toString();

        if (TextUtils.isEmpty(jossId)) {
            ToastUtil.showToast("序号不能为空");
            return false;
        }
        if (!TextUtils.isEmpty(jossId) && isExistJossId(jossId)) {
            ToastUtil.showToast("序号 " + jossId + " 已存在");
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
        PersonData personData = HolyDBManager.getInstance(getContext()).queryDataByJossId(jossId);
        boolean is = false;
        if (personData != null) {
            String name = personData.getName();
            if (!TextUtils.isEmpty(name)) {
                is = true;
            }
        }
        return is;
    }

    private boolean isExistJossId(String jossId) {
        PersonData personData = HolyDBManager.getInstance(getContext()).queryDataByJossId(jossId);
        boolean is = false;
        if (personData != null) {
           is = true;
        }
        return is;
    }


    private void clearAllText() {
        binding.etFeedPrice.setText("");
        binding.etPhoneNum.setText("");
        binding.etId.setText("");
        binding.etName.setText("");
        binding.tvExtendTime.setText("");
        binding.tvFeedTime.setText("选择日期");
        binding.tvExtendTime.setText("选择日期");

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
        DatePickerDialog dialog = new DatePickerDialog(getContext(), 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
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

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dataList);

        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        binding.spinner.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Logger.e(TAG, "您当前选择的是：" + adapter.getItem(position));
                jossType = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Logger.e(TAG, "请选择您的城市");

            }
        });

    }

}
