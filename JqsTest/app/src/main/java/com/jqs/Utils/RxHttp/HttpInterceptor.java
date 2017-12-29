package com.jqs.Utils.RxHttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 */

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //添加消息头
        Request request = chain.request()
                .newBuilder()
                //.addHeader("test","test")
                .build();
        return chain.proceed(request);
    }
}
