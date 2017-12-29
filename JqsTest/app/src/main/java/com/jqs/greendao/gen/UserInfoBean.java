package com.jqs.greendao.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户基本信息实体类
 */
@Entity
public class UserInfoBean {
    @Id(autoincrement = true)
    private Long Uid;//用户id
    private String UserName;//用户昵称
    private String UserHeader;//用户头像
    private String Portrait;//用户主页背景图
    private String Signature;//签名

    @Generated(hash = 222251865)
    public UserInfoBean(Long Uid, String UserName, String UserHeader,
            String Portrait, String Signature) {
        this.Uid = Uid;
        this.UserName = UserName;
        this.UserHeader = UserHeader;
        this.Portrait = Portrait;
        this.Signature = Signature;
    }

    @Generated(hash = 1818808915)
    public UserInfoBean() {
    }

    public Long getUid() {
        return Uid;
    }

    public void setUid(Long uid) {
        Uid = uid;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserHeader() {
        return UserHeader;
    }

    public void setUserHeader(String userHeader) {
        UserHeader = userHeader;
    }

    public String getPortrait() {
        return Portrait;
    }

    public void setPortrait(String portrait) {
        Portrait = portrait;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }
}
