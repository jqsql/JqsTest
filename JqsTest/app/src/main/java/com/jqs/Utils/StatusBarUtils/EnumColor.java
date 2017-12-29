package com.jqs.Utils.StatusBarUtils;

import com.jqs.R;

/**
 * 颜色枚举
 */

public enum EnumColor {
        RED(R.color.Red),
        BLACK(R.color.Black),
        WHITE(R.color.White),
        GREEN(R.color.Green);
    private int mColor;
    EnumColor(int color){
        mColor=color;
    }

    public int getColor() {
        return mColor;
    }
}
