package com.jqs.Beans;

/**
 * Created by jqs on 2017/12/13.
 */

public class RxBusModle<T> {
    private String flag;//标志
    private T Content;//内容

    public RxBusModle(String flag) {
        this.flag = flag;
    }

    public RxBusModle(String flag, T content) {
        this.flag = flag;
        Content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public T getContent() {
        return Content;
    }

    public void setContent(T content) {
        Content = content;
    }
}
