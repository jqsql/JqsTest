package com.jqs.Beans;

import java.io.Serializable;

/**
 * 网络数据格式实体
 */

public class ServiceBean implements Serializable{
    private int Code;
    private Object Data;
    private String Message;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
