package com.holywatertemple.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.holywatertemple.BuildConfig;
import com.holywatertemple.Config;
import com.holywatertemple.R;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.excel.ExcelManger;
import com.holywatertemple.excel.HolyDBManager;
import com.holywatertemple.java_lib.ExcelUtil;
import com.holywatertemple.java_lib.bean.Header;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.java_lib.bean.Table;
import com.holywatertemple.ui.base.BaseFragment;
import com.holywatertemple.util.AppSharePref;
import com.holywatertemple.util.ExecutorsManager;
import com.holywatertemple.util.Logger;
import com.holywatertemple.util.ToastUtil;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhangyiipeng on 2018/6/8.
 */

public class MessageFragment extends BaseFragment {
    public static final String TAG = MessageFragment.class.getSimpleName();

    @BindView(R.id.bt_import)
    Button btImport;
    Unbinder unbinder;
    @BindView(R.id.bt_export)
    Button btExport;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_message;
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

    @OnClick({R.id.bt_import, R.id.bt_export})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_import:

                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.xls")) // Filtering files and directories by file name using regexp
                        .withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(true) // Show hidden files and folders
                        .start();
                break;
            case R.id.bt_export:

                exportExcel();
                break;
        }
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日h时mm分ss秒");
    private void exportExcel() {
        final String filePath = Config.EXCEL_FILE_EXPORT_PORT_PATH + "person_" + sdf.format(new Date())+ ".xls";
        ToastUtil.showToast("开始导出数据 :" + filePath);
        ExecutorsManager.getInstance().getFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<PersonData> allData = HolyDBManager.getInstance(getContext()).queryAllData();
                List<Person> personList = new ArrayList<>();
                for (PersonData personData : allData) {
                    Person person = new Person(personData.getJossId(), personData.getName(), personData.getPhoneNum(), personData.getJossType(),
                            personData.getFendPrice(), personData.getFendTime(), personData.getExtendTime());
                    personList.add(person);
                }
                Table table = new Table();
                table.setPersonList(personList);
                String tableInfo = AppSharePref.getInstance().getTableInfo();
                table.setHeader(new Gson().fromJson(tableInfo, Header.class));
                ExcelUtil.writeExcel(filePath, table);

                btExport.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("导出数据成功 :" + filePath);
                    }
                });
            }
        });
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

    public void onFilePath(final String filePath) {
        ToastUtil.showToast("开始导入数据 :" + filePath);
        ExecutorsManager.getInstance().getFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (BuildConfig.DEBUG) Logger.e(TAG, filePath);
                if (!TextUtils.isEmpty(filePath)) {
                    ExcelManger.getInstance(getContext()).readExcelDataToDB(filePath);
                }
            }
        });
    }
}
