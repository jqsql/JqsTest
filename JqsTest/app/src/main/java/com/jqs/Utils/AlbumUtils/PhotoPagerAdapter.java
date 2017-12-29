package com.jqs.Utils.AlbumUtils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jqs.Beans.AlbumImageBean;
import com.jqs.Utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器
 */

public class PhotoPagerAdapter extends PagerAdapter {
    private ArrayList<View> listViews;
    private List<AlbumImageBean> mData;
    private Context mContext;
    private int size;
    public PhotoPagerAdapter(Context context,ArrayList<View> listViews,List<AlbumImageBean> data) {
        this.listViews = listViews;
        mContext=context;
        mData=data;
        size = listViews == null ? 0 : listViews.size();
    }

    public void setListViews(ArrayList<View> listViews) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public int getCount() {
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        if(mData.get(arg1 % size).getType()==1)
            //取消加载gif
            com.jqs.Utils.ImageLoader.getInstance(mContext).clearLoadGif(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    public Object instantiateItem(View arg0, int arg1) {
        int poistion=0;
        try {
            poistion=arg1 % size;
            //在此设置背景图片，提高加载速度，解决OOM问题
            PhotoView view=(PhotoView)listViews.get(poistion);
            view.setBackgroundColor(0xff000000);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if(mData.get(poistion).getType()==1){
                //GIF
                com.jqs.Utils.ImageLoader.getInstance(mContext).setLoadGif(view,mData.get(poistion).getImageName());
            }else {
                view.setImageBitmap(mData.get(poistion).getBitmap());
            }
            view.setLayoutParams(params);

            ((ViewPagerFixed) arg0).addView(view, 0);

        } catch (Exception e) {
        }
        return listViews.get(poistion);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}

