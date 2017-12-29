package com.jqs.Beans;

import android.graphics.Bitmap;

import com.jqs.Utils.AlbumUtils.AlbumSetting;

import java.io.IOException;
import java.io.Serializable;

/**
 * 图片加载器 图片信息实体类
 */

public class AlbumImageBean implements Serializable{
    private String imageName;//图片名称路径
    private Bitmap bitmap;//bitmap实体
    private int type;//0:普通图片 1:gif 2:视频

    public AlbumImageBean() {
    }

    public Bitmap getBitmap() {
        if(bitmap == null){
            try {
                bitmap = AlbumSetting.revitionImageSize(imageName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
