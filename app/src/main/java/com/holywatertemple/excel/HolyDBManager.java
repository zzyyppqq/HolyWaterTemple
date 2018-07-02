package com.holywatertemple.excel;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.holywatertemple.db.DbManager;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.db.model.PersonDataDao;
import com.holywatertemple.java_lib.bean.Person;
import com.holywatertemple.util.Logger;

import java.util.List;

;import static com.holywatertemple.ui.fragment.HomeFragment.ALL;
import static com.holywatertemple.ui.fragment.HomeFragment.NO_USE;
import static com.holywatertemple.ui.fragment.HomeFragment.USE;

/**
 * Created by zhangyiipeng on 2018/3/17.
 */

public class HolyDBManager {

    public static final String TAG = "StatisticsDBManager";

    private static HolyDBManager instance;
    private final PersonDataDao personDataDao;


    private HolyDBManager(Context context) {
        personDataDao = DbManager.getSession(context.getApplicationContext()).getPersonDataDao();
    }

    public static HolyDBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (HolyDBManager.class) {
                if (instance == null) instance = new HolyDBManager(context);
            }
        }
        return instance;
    }

    public void beginMediaStatisticsTransactio(Runnable runnable) {
        personDataDao.getSession().runInTx(runnable);
    }


    public synchronized long insertData(Person person) {
        final String json = new Gson().toJson(person);
        final long insert = personDataDao.insert(new PersonData(0,1,person.getJossId(),person.getName(),person.getPhoneNum(),person.getJossType(),
                person.getFendPrice(),person.getFendTime(),person.getExtendTime(), json));
        return insert;
    }



    public synchronized void updateData(String jossId, Person person) {
        final String json = new Gson().toJson(person);
        Long id = queryDataByJossId(jossId).getId();
        personDataDao.update(new PersonData(id,0,1,person.getJossId(),person.getName(),person.getPhoneNum(),person.getJossType(),
                person.getFendPrice(),person.getFendTime(),person.getExtendTime(), json));
    }

    public synchronized void clearDataWithoutJossId(String jossId) {
        Long id = queryDataByJossId(jossId).getId();
        personDataDao.update(new PersonData(id,0,1,jossId,"","","",
                "","","", ""));
    }

    public List<PersonData> queryAllData() {
        return personDataDao.queryBuilder().build().forCurrentThread().list();
    }


    public PersonData queryDataByJossId(String jossId) {
        List<PersonData> list = personDataDao.queryBuilder().where(PersonDataDao.Properties.JossId.eq(jossId)).build().forCurrentThread().list();
        if (list != null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public List<PersonData> queryLikeData(String like) {
        Logger.e(TAG,"like :"+ like);
        List<PersonData> list = personDataDao.queryBuilder().whereOr(
//                PersonDataDao.Properties.Id.like("%" + like + "%"),
                PersonDataDao.Properties.JossId.like("%" + like + "%"),
                PersonDataDao.Properties.Name.like("%" + like + "%"),
                PersonDataDao.Properties.PhoneNum.like("%" + like + "%"),
                PersonDataDao.Properties.JossType.like("%" + like + "%"),
                PersonDataDao.Properties.FendPrice.like("%" + like + "%"),
                PersonDataDao.Properties.FendTime.like("%" + like + "%"),
                PersonDataDao.Properties.ExtendTime.like("%" + like + "%")
        ).build().forCurrentThread().list();
        return list;
    }

    public synchronized void deleteByJossId(String jossId) {
        personDataDao.delete(queryDataByJossId(jossId));
    }

    public synchronized void deleteAll() {
        personDataDao.deleteAll();
    }

    public List<PersonData> queryDataByJossType(String jossType, int type) {
        if ("所有类型".equals(jossType)){//查询所有类型
            if (type == ALL) {
                return personDataDao.queryBuilder().build().forCurrentThread().list();
            } else if (type == USE) {
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.notEq("")).build().forCurrentThread().list();
            } else if (type == NO_USE) {
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.eq("")).build().forCurrentThread().list();
            }
        }else {
            if (type == ALL) {
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType)).build().forCurrentThread().list();
            } else if (type == USE) {
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.notEq("")).build().forCurrentThread().list();
            } else if (type == NO_USE) {
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.eq("")).build().forCurrentThread().list();
            }
        }
        return null;

    }

    public List<PersonData> queryDataByJossTypeAndSearch(String jossType, int type,String like) {
        if ("所有类型".equals(jossType)){//查询所有类型
            if (type == ALL) {
                if (TextUtils.isEmpty(like)){
                    return personDataDao.queryBuilder().build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            } else if (type == USE) {
                if (TextUtils.isEmpty(like)) {
                    return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.notEq("")).build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.notEq(""),PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            } else if (type == NO_USE) {
                if (TextUtils.isEmpty(like)) {
                    return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.eq("")).build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.Name.eq(""),PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            }
        }else {
            if (type == ALL) {
                if (TextUtils.isEmpty(like)) {
                    return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType)).build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType),PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            } else if (type == USE) {
                if (TextUtils.isEmpty(like)) {
                    return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.notEq("")).build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.notEq(""),PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            } else if (type == NO_USE) {
                if (TextUtils.isEmpty(like)) {
                    return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.eq("")).build().forCurrentThread().list();
                }
                return personDataDao.queryBuilder().where(PersonDataDao.Properties.JossType.eq(jossType), PersonDataDao.Properties.Name.eq(""),PersonDataDao.Properties.FendPrice.like("%" + like + "%")).build().forCurrentThread().list();
            }
        }
        return null;

    }
}
