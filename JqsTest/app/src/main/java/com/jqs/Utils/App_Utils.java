package com.jqs.Utils;

import android.content.Context;
import android.view.View;

/**
 * 工具类
 */

public class App_Utils {
    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取view控件宽高（无法测量math_parent的情况）
     * @param view
     * getViewMeasure(view).getMeasuredWidth();宽
     * getViewMeasure(view).getMeasuredHeight();高
     */
    public static View getViewMeasure(View view){
        int  widthMeasureSpec= View.MeasureSpec.makeMeasureSpec((1<<30)-1, View.MeasureSpec.AT_MOST);
        int  heightMeasureSpec= View.MeasureSpec.makeMeasureSpec((1<<30)-1, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec,heightMeasureSpec);
        return view;
    }
}
