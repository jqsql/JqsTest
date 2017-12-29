package com.jqs.Utils.BaseActivityUtils;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jqs.Beans.APPColor_BusBean;
import com.jqs.R;
import com.jqs.Utils.ALog;
import com.jqs.Utils.RxJavaUtils.RxBus;
import com.jqs.Utils.StatusBarUtils.StatusBarColor;

import io.reactivex.functions.Consumer;

/**
 * 通用activity
 */

public class BaseActivity extends AppCompatActivity{
    public static String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        TAG=getClass().getSimpleName();//获取Activity名称
        BaseAppManager.getInstance().addActivity(this);//将当前activity添加到队列里面
        if(isFullScreen())
            StatusBarColor.setStatusBar(this);
        else
            StatusBarColor.setStatusBarColor(this, R.color.APP_COLOR);//修改状态栏颜色
        /**
         * APP换肤回调
         */
        RxBus.getDefault().doSubscribeMain(this, APPColor_BusBean.class, new Consumer<APPColor_BusBean>() {
            @Override
            public void accept(APPColor_BusBean appColor_busBean) throws Exception {
                ALog.e("APP换肤回调");
                if(appColor_busBean!=null)
                    StatusBarColor.setStatusBarColor(BaseActivity.this, appColor_busBean.getColor());//修改状态栏颜色
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unDisposable(this);//取消订阅
        BaseAppManager.getInstance().removeActivity(this);//移出当前activity
    }

    /**
     * 是否需要全屏
     * @return
     */
    protected boolean isFullScreen(){
        return false;
    }
}
