package com.jqs.Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 相册文件夹实体类
 */

public class FolderBean implements Serializable{
    private String dir;//文件夹的路径
    private String fiestImgPath;//文件夹第一张图片的路径
    private List<AlbumImageBean> imgs;//文件下图片集合
    private String name;//文件夹名称
    private int count;//文件夹中图片的数量
    private boolean isChecked=false;//当前文件夹是否被选择

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        if(!"".equals(dir)) {
            int lastIndexOf = this.dir.lastIndexOf("/") + 1;
            this.name = this.dir.substring(lastIndexOf);
        }else {
            this.name = "所有图片";
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<AlbumImageBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<AlbumImageBean> imgs) {
        this.imgs = imgs;
    }

    public String getFiestImgPath() {
        return fiestImgPath;
    }

    public void setFiestImgPath(String fiestImgPath) {
        this.fiestImgPath = fiestImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
