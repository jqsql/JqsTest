package com.jqs.greendao.gen;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * GreenDao初始化类
 */

public class GreenDaoManager {
    //数据库SQLite是否加密标志
    public static final boolean ENCRYPTED = false;
    private static GreenDaoManager mManager;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static GreenDaoManager getInstance(Context context){
        if(mManager==null){
            synchronized (GreenDaoManager.class){
                if(mManager==null){
                    mManager=new GreenDaoManager(context);
                }
            }
        }
        return mManager;
    }

    public GreenDaoManager(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
