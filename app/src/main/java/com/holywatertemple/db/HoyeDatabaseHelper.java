package com.holywatertemple.db;

import android.content.Context;

import com.holywatertemple.db.model.DaoMaster;
import com.holywatertemple.db.model.PersonDataDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by wangxue on 2018/3/12.
 */

public class HoyeDatabaseHelper extends DaoMaster.OpenHelper {
    public static final String DB_NAME = "holy-water-temple.db";

    public HoyeDatabaseHelper(Context context) {
        super(context, DB_NAME, null);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //数据库升级策略：（1）创建临时表（2）删除原表（3）创建新表（4）复制临时表数据到新表（5）删除临时表
        MigrationHelper.getInstance().migrate(db, PersonDataDao.class);
    }
}
