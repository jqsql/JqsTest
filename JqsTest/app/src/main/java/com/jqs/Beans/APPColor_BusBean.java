package com.jqs.Beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * app换肤实体类
 */

public class APPColor_BusBean implements Parcelable {
    private int color;
    private String flag;

    public APPColor_BusBean(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.color);
        dest.writeString(this.flag);
    }

    protected APPColor_BusBean(Parcel in) {
        this.color = in.readInt();
        this.flag = in.readString();
    }

    public static final Parcelable.Creator<APPColor_BusBean> CREATOR = new Parcelable.Creator<APPColor_BusBean>() {
        @Override
        public APPColor_BusBean createFromParcel(Parcel source) {
            return new APPColor_BusBean(source);
        }

        @Override
        public APPColor_BusBean[] newArray(int size) {
            return new APPColor_BusBean[size];
        }
    };
}
