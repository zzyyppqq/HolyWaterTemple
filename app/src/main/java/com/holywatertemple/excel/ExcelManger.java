package com.holywatertemple.excel;

import android.content.Context;

import com.google.gson.Gson;
import com.holywatertemple.BuildConfig;
import com.holywatertemple.java_lib.ExcelUtil;
import com.holywatertemple.java_lib.bean.Header;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.java_lib.bean.Table;
import com.holywatertemple.util.AppSharePref;
import com.holywatertemple.util.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ExcelManger {

    public static final String TAG = ExcelManger.class.getSimpleName();

    private static ExcelManger instance;
    private Context mContext;
    private final HolyDBManager hoyDBManager;

    private ExcelManger(Context context) {
        this.mContext = context;
        hoyDBManager = HolyDBManager.getInstance(context);
    }

    public static ExcelManger getInstance(Context context) {
        if (instance == null) {
            synchronized (ExcelManger.class) {
                if (instance == null) instance = new ExcelManger(context);
            }
        }
        return instance;
    }

    public void readExcelDataToDB(final String excelFilePath) {

        hoyDBManager.deleteAll();
        Observable.create(new Observable.OnSubscribe<Table>() {
            @Override
            public void call(Subscriber<? super Table> subscriber) {
                Logger.e(TAG,Thread.currentThread().getName());
                Table table = ExcelUtil.readExcel(excelFilePath);
                if (table == null) {
                    subscriber.onError(new Throwable("table == null"));
                } else {
                    subscriber.onNext(table);
                }
                subscriber.onCompleted();
            }
        }).observeOn(Schedulers.io()) // retryWhen的处理线程
                .map(new Func1<Table, String>() {
                    @Override
                    public String call(Table table) {
                        Header header = table.getHeader();
                        AppSharePref.getInstance().setTableInfo(new Gson().toJson(header));
                        final List<Person> personList = table.getPersonList();
                        if (personList != null) {
                            hoyDBManager.beginMediaStatisticsTransactio(new Runnable() {
                                @Override
                                public void run() {
                                    for (Person person : personList) {
                                        hoyDBManager.insertData(person);
                                    }
                                }
                            });
                            return "ok";
                        }else {
                            return "no";
                        }
                    }
                })
                .observeOn(Schedulers.io()) // retryWhen的处理线程
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String result) {
                        //加载数据
                        if(BuildConfig.DEBUG)Logger.e(TAG,result);
                    }
                });

    }
}
