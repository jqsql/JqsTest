package com.jqs.Utils;

import android.app.Application;
import com.jqs.greendao.gen.GreenDaoManager;

/**
 *APP初始化操作
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //GreenDao的初始化
        GreenDaoManager.getInstance(getApplicationContext());

    }

}
