package com.jqs.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jqs.R;

/**
 * 自定义加载框
 */

public class ProgressDialog extends Dialog{
    private ProgressBar mProgressBar;
    private TextView mHintText;

    public ProgressDialog(@NonNull Context context) {
        super(context,R.style.progress_dialog);
    }

    public ProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_view);
        //去黑角
        getWindow().setBackgroundDrawable(new BitmapDrawable());

        mProgressBar=findViewById(R.id.ProgressDialog);
        mHintText=findViewById(R.id.ProgressDialogHint);

    }

    /**
     * 显示
     */
    public void showProgress(String hint){
        show();
        mProgressBar.setVisibility(View.VISIBLE);
        mHintText.setText(hint);
    }
    /**
     * 中途改变状态提示
     */
    public void setProgressHint(String hint){
        if(isShowing()) {
            mProgressBar.setVisibility(View.GONE);
            mHintText.setText(hint);
        }else {
            show();
            mProgressBar.setVisibility(View.GONE);
            mHintText.setText(hint);
        }
        //提示一段时间后自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },APP_Property.hintDialogTime);
    }
    /**
     * 隐藏
     */
    public void hideProgress(){
        dismiss();
    }
}
