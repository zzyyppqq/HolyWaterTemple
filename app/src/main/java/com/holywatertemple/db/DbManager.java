package com.holywatertemple.db;


import android.content.Context;

import com.holywatertemple.db.model.DaoMaster;
import com.holywatertemple.db.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by wangxue on 2018/3/12.
 */

public class DbManager {
    private static final boolean ENCRYPTED = false;
    private static final String PASSWORD = new String("holy-water-temple");
    private static HoyeDatabaseHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private static Database getWritableDatabase(Context context) {
        if (mHelper == null) {
            mHelper = new HoyeDatabaseHelper(context);
        }
        return mHelper.getWritableDb();
    }

    private static Database getWritableDatabaseEncrypted(Context context, String password) {
        if (mHelper == null) {
            mHelper = new HoyeDatabaseHelper(context);
        }
        return mHelper.getEncryptedWritableDb(password);
    }

    private static DaoMaster getDaoMaster(Context context) {
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(getWritableDatabase(context));
        }
        return mDaoMaster;
    }

    private static DaoMaster getDaoMasterEncrypted(Context context, String password) {
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(getWritableDatabaseEncrypted(context, password));
        }
        return mDaoMaster;
    }

    private static DaoSession getDaoSession(Context context) {
        if (mDaoSession == null) {
            synchronized (DbManager.class) {
                if (mDaoSession == null) {
                    mDaoSession = getDaoMaster(context).newSession();
                }
            }
        }
        return mDaoSession;
    }

    private static DaoSession getDaoSessionEncrypted(Context context, String password) {
        if (mDaoSession == null) {
            synchronized (DbManager.class) {
                if (mDaoSession == null) {
                    mDaoSession = getDaoMasterEncrypted(context, password).newSession();
                }
            }
        }
        return mDaoSession;
    }

    public static DaoSession getSession(Context context) {
        if (ENCRYPTED) {
            return getDaoSessionEncrypted(context.getApplicationContext(), PASSWORD);
        } else {
            return getDaoSession(context.getApplicationContext());
        }
    }
}
