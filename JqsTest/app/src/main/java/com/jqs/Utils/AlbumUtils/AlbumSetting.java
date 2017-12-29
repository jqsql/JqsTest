package com.jqs.Utils.AlbumUtils;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jqs.Beans.AlbumImageBean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 相册的一些设置
 */

public class AlbumSetting {
    public static final int MaxNumber=9;//选择图片数量
    public static List<Activity> AlumberActivity=new ArrayList<>();//相册的activity
    public static List<AlbumImageBean> CheckedBitmapList=new ArrayList<>();//临时存储选择的图片
    public static List<AlbumImageBean> CheckedSureBitmapList=new ArrayList<>();//已经确定选择的图片（回调给外部的）


    /**
     * 压缩获得bitmap
     * @param path
     * @return
     * @throws IOException
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)  //>>是右移，size>>1表示把size右移1位，相当于size/2
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        //Log.e("test","Bitmap: "+(bitmap.getRowBytes() * bitmap.getHeight())/1024);
        return bitmap;
    }

    /**
     * 清除信息
     */
    public static void clear(){
        CheckedBitmapList.clear();
        CheckedSureBitmapList.clear();
    }
    /**
     * 清除信息
     */
    public static void clearActivity(){
        //赋值给确认选择list
        AlbumSetting.CheckedSureBitmapList.addAll(AlbumSetting.CheckedBitmapList);
        AlbumSetting.CheckedBitmapList.clear();
        if(AlumberActivity!=null)
        for (Activity activity:AlumberActivity) {
            activity.finish();
        }
    }
}
