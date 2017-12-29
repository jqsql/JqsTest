package com.jqs.Views;

/**
 * 登录
 */

public interface LoginView {
    void showProgress();
    void hideProgress();
    void loginError();
    void loginSuccess();

}
