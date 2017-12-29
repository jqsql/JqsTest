package com.jqs.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqs.Presenter.LoginPresenter;
import com.jqs.PresenterImp.LoginPresenterImp;
import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.APP_Property;
import com.jqs.Utils.BaseActivityUtils.BaseActivity;
import com.jqs.Utils.ProgressDialog;
import com.jqs.Views.LoginView;
import com.jqs.greendao.gen.GreenDaoManager;
import com.jqs.greendao.gen.UserInfoBean;
import com.jqs.greendao.gen.UserInfoBeanDao;

public class LoginActivity extends BaseActivity implements LoginView{
    ImageView mReturn;//返回
    EditText mLoginName;//登录名
    EditText mPassWord;//登录密码
    Button mLogin;//登录按钮
    TextView mSign;//注册按钮
    TextView mForgetPassWord;//忘记密码

    private LoginPresenter mLoginPresenter;
    private ProgressDialog mProgressDialog;
    private UserInfoBeanDao mInfoBeanDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        initListen();
    }


    /**
     * 控件初始化
     */
    private void initView() {
        mReturn= (ImageView) findViewById(R.id.Login_Return);
        mLoginName= (EditText) findViewById(R.id.Login_Name);
        mPassWord= (EditText) findViewById(R.id.Login_PassWord);
        mLogin= (Button) findViewById(R.id.Login_LoginBtn);
        mSign= (TextView) findViewById(R.id.Login_Sign);
        mForgetPassWord= (TextView) findViewById(R.id.Login_ForgetPassWord);

        mInfoBeanDao= GreenDaoManager.getInstance(this).getDaoSession().getUserInfoBeanDao();

        mLoginPresenter=new LoginPresenterImp(this);
        mProgressDialog=new ProgressDialog(LoginActivity.this);
    }

    /**
     * 数据处理
     */
    private void initData() {

    }

    /**
     * 事件监听
     */
    private void initListen() {
        //返回
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //登录
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.loginGo(mLoginName.getText().toString(),mPassWord.getText().toString());
            }
        });
        //注册
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        mProgressDialog.showProgress("登录中...");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.hideProgress();

    }

    @Override
    public void loginError() {
        mProgressDialog.setProgressHint("登录失败");
    }

    @Override
    public void loginSuccess() {
        UserInfoBean userInfoBean=new UserInfoBean((long)0,"柚子可以减肥",
                "http://g.hiphotos.baidu.com/zhidao/pic/item/aec379310a55b3191510402645a98226cefc17fc.jpg",
                "http://d.hiphotos.baidu.com/zhidao/pic/item/72f082025aafa40fe871b36bad64034f79f019d4.jpg",
                "u are sd");
        mInfoBeanDao.save(userInfoBean);
        APP_Property.user=userInfoBean;
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
