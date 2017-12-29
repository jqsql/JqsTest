package com.jqs.Utils.AlbumUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.jqs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义九宫图控件
 */

public class NineGridLayout extends ViewGroup{
    /**
     * 图片之间的间距
     */
    private int mSpace = 5;
    /**
     * 列
     */
    private int mColumns;
    /**
     * 行
     */
    private int mRows;
    /**
     * 图片集合
     */
    private List<String> mImages=new ArrayList<>();

    /**
     * 图片点击监听事件
     */
    private OnItemClickListener mItemClickListener;

    public NineGridLayout(Context context) {
        this(context,null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.NineLayoutStyle);
        mSpace= (int) mTypedArray.getDimension(R.styleable.NineLayoutStyle_space,5);

        mTypedArray.recycle();
    }

    interface OnItemClickListener{
        void click();
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }



    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    /**
     * 初始化行列数
     */
    private void initColumnRow(){
        int size=mImages.size();
        if(size>4 || size==3){//3张或4张以上
            mColumns = 3;
            mRows = size%3==0 ? size/3 : size/3+1;
        }else if(size==4){//4张
            mColumns = 2;
            mRows = 2;
        }else if(size==2){//2张
            mColumns = 2;
            mRows = 1;
        }else {//1张
            mColumns = 1;
            mRows = 1;
        }
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
