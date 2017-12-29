package com.jqs.Utils.StatusBarUtils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jqs.R;
import com.jqs.Utils.ALog;

/**
 * 修改状态栏颜色工具
 */

public class StatusBarColor {
    /**
     * Android 5.0 以上
     * 着色模式
     * @param activity
     */
    public static void setStatusBarColor(Activity activity,int color){
        Window window=activity.getWindow();
        //取消设置占用状态栏空间
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //必须设置这个属性才可以设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
        //获取activity绑定的视图
        ViewGroup mContentView=activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView=mContentView.getChildAt(0);//获取视图的第一个子控件
        if(mChildView!=null){
            mChildView.setFitsSystemWindows(true);//预留出状态栏的空间
        }
    }
    /**
     * Android 5.0 以上
     * 全屏模式
     * @param activity
     */
    public static void setStatusBar(Activity activity){
        Window window=activity.getWindow();
        //设置透明状态栏,这样才能让ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //必须设置这个属性才可以设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.transparent));
        }
        //获取activity绑定的视图
        ViewGroup mContentView=activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView=mContentView.getChildAt(0);//获取视图的第一个子控件
        if(mChildView!=null){
            mChildView.setFitsSystemWindows(false);//不预留出状态栏的空间
        }
    }

    /**
     *  Android 4.4-5.0
     * 全屏模式
     * @param activity
     */
    public static void setStatusBar2(Activity activity){
        Window window = activity.getWindow();
        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        //首先使 ChildView 不预留空间
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
        }
        int statusBarHeight = getStatusBarHeight(activity);
        //需要设置这个 flag 才能设置状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //避免多次调用该方法时,多次移除了View
        if (mChildView != null && mChildView.getLayoutParams() != null && mChildView.getLayoutParams().height == statusBarHeight) {
            //移除假的 View.
            mContentView.removeView(mChildView);
            mChildView = mContentView.getChildAt(0);
        }
        if (mChildView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
            //清除 ChildView 的 marginTop 属性
            if (lp != null && lp.topMargin >= statusBarHeight) {
                lp.topMargin -= statusBarHeight;
                mChildView.setLayoutParams(lp);
            }
        }
    }
    /**
     * 获取状态栏高度
     * */
    public static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
