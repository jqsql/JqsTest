package com.jqs.Dao;

/**
 * 网络请求通用结果回调
 */

public interface OnResultClick<T> {
    void success(T t);
    void error(Throwable throwable);
}
