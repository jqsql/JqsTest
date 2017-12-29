package com.jqs.greendao.gen;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 圈子实体类
 */
@Entity
public class CircleBean {
    @Id(autoincrement = true)
    private Long CircleId;//动态id
    private String TimeStr;//发布时间
    private String Content;//内容
    private int Type;//0：纯文字 1: 带图 2：带视频
    @Convert(/**转换器**/converter = StringListConverent.class,columnType = String.class)
    private List<String> Picture;//内容图片
    private String VoidePicture;//视频图片
    private String VoideName;//视频名称
    private int Comments;//评论数
    private int Likes;//点赞数
    private boolean isTransmit;//是否是转发的标志
    //@Transient
    @Convert(/**转换器**/converter = Bean2StringConverent.class ,columnType = String.class)
    private UserInfoBean Author;//作者

    @Generated(hash = 651001505)
    public CircleBean(Long CircleId, String TimeStr, String Content, int Type,
            List<String> Picture, String VoidePicture, String VoideName, int Comments,
            int Likes, boolean isTransmit, UserInfoBean Author) {
        this.CircleId = CircleId;
        this.TimeStr = TimeStr;
        this.Content = Content;
        this.Type = Type;
        this.Picture = Picture;
        this.VoidePicture = VoidePicture;
        this.VoideName = VoideName;
        this.Comments = Comments;
        this.Likes = Likes;
        this.isTransmit = isTransmit;
        this.Author = Author;
    }

    @Generated(hash = 563805370)
    public CircleBean() {
    }

    public CircleBean(String timeStr, String content, int type, List<String> picture,
                      String voidePicture, String voideName, int comments,
                      int likes, boolean isTransmit, UserInfoBean author) {
        TimeStr = timeStr;
        Content = content;
        Type = type;
        Picture = picture;
        VoidePicture = voidePicture;
        VoideName = voideName;
        Comments = comments;
        Likes = likes;
        this.isTransmit = isTransmit;
        Author = author;
    }

    public Long getCircleId() {
        return CircleId;
    }

    public void setCircleId(Long circleId) {
        CircleId = circleId;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public UserInfoBean getAuthor() {
        return Author;
    }

    public void setAuthor(UserInfoBean author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public List<String> getPicture() {
        return Picture;
    }

    public void setPicture(List<String> picture) {
        Picture = picture;
    }

    public String getVoidePicture() {
        return VoidePicture;
    }

    public void setVoidePicture(String voidePicture) {
        VoidePicture = voidePicture;
    }

    public String getVoideName() {
        return VoideName;
    }

    public void setVoideName(String voideName) {
        VoideName = voideName;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public boolean isTransmit() {
        return isTransmit;
    }

    public void setTransmit(boolean transmit) {
        isTransmit = transmit;
    }

    public boolean getIsTransmit() {
        return this.isTransmit;
    }

    public void setIsTransmit(boolean isTransmit) {
        this.isTransmit = isTransmit;
    }
}
