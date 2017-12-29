package com.jqs.Dao;

/**
 * 登录操作类
 */

public interface LoginDao {
    void login(String UserName,String PassWord,OnLoginFinishedListener listener);

}
