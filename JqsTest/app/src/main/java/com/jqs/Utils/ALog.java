package com.jqs.Utils;

import android.util.Log;

import com.jqs.Utils.BaseActivityUtils.BaseActivity;

/**
 * 自定义log工具类
 */

public class ALog {
    public static void v(String content){
        if(content==null)
            content="";
        Log.v(BaseActivity.TAG,content);
    }
    public static void d(String content){
        if(content==null)
            content="";
        Log.d(BaseActivity.TAG,content);
    }
    public static void i(String content){
        if(content==null)
            content="";
        Log.i(BaseActivity.TAG,content);
    }
    public static void w(String content){
        if(content==null)
            content="";
        Log.w(BaseActivity.TAG,content);
    }
    public static void e(String content){
        if(content==null)
            content="";
        Log.e(BaseActivity.TAG,content);
    }
}
