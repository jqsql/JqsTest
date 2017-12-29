package com.jqs.PresenterImp;

import com.jqs.Dao.LoginDao;
import com.jqs.Dao.OnLoginFinishedListener;
import com.jqs.DaoImp.LoginDaoimp;
import com.jqs.Presenter.LoginPresenter;
import com.jqs.Views.LoginView;

/**
 * 1 完成presenter的实现。这里面主要是Model层和View层的交互和操作。
 * 2  presenter里面还有个OnLoginFinishedListener，
 * 其在Presenter层实现，给Model层回调，更改View层的状态，
 * 确保 Model层不直接操作View层。如果没有这一接口在LoginPresenterImpl实现的话，
 * LoginPresenterImpl只 有View和Model的引用那么Model怎么把结果告诉View呢？
 */
public class LoginPresenterImp implements LoginPresenter,OnLoginFinishedListener {
    private LoginDao mLoginDao;
    private LoginView mLoginView;

    public LoginPresenterImp(LoginView loginView) {
        mLoginView = loginView;
        mLoginDao=new LoginDaoimp();
    }


    @Override
    public void loginGo(String userName, String password) {
        if(mLoginView!=null){
            mLoginView.showProgress();
        }
        mLoginDao.login(userName,password,this);
    }

    @Override
    public void onDestroy() {
        mLoginView=null;
    }

    /**
     * 登录失败
     */
    @Override
    public void onError() {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.loginError();
        }
    }
    /**
     * 登录成功
     */
    @Override
    public void onSuccess() {
        if(mLoginView!=null){
            mLoginView.hideProgress();
            mLoginView.loginSuccess();
        }
    }
}
