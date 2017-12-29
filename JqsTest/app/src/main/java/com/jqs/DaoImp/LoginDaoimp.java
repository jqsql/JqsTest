package com.jqs.DaoImp;

import android.os.Handler;
import android.util.Log;

import com.jqs.Dao.LoginDao;
import com.jqs.Dao.OnLoginFinishedListener;

/**
 * 登录操作实现
 */

public class LoginDaoimp implements LoginDao{
    @Override
    public void login(final String UserName, final String PassWord, final OnLoginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error=false;
                if(UserName.isEmpty() || PassWord.isEmpty()) {
                    listener.onError();
                    error = true;
                }
                if(!"123".equals(UserName) || !"123".equals(PassWord)) {
                    listener.onError();
                    error = true;
                }
                if(!error){
                    listener.onSuccess();
                }

            }
        },2000);

    }
}
