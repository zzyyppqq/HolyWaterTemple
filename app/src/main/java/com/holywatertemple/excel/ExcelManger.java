package com.holywatertemple.excel;

import android.content.Context;

import com.google.gson.Gson;
import com.holywatertemple.Config;
import com.holywatertemple.java_lib.ExcelUtil;
import com.holywatertemple.java_lib.bean.Header;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.java_lib.bean.Table;
import com.holywatertemple.util.AppSharePref;

import java.util.List;

public class ExcelManger {

    private static ExcelManger instance;
    private ExcelManger(Context context) {
        init(context);
    }

    public static ExcelManger getInstance(Context context) {
        if (instance == null) {
            synchronized (ExcelManger.class) {
                if (instance == null) instance = new ExcelManger(context);
            }
        }
        return instance;
    }

    private void init(Context context) {
        Table table = ExcelUtil.readExcel(Config.EXCEL_FILE_PATH);
        Header header = table.getHeader();
        AppSharePref.getInstance().setTableInfo(new Gson().toJson(header));
        List<Person> personList = table.getPersonList();
        for (Person person : personList){

        }


    }


}
