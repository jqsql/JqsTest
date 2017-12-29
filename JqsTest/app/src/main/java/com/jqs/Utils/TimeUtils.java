package com.jqs.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转化工具类
 */

public class TimeUtils {
    public static String getNowTimeStr(){
        long seconds=getNowTime();
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }
    public static long getNowTime(){
        long TimeMillis=System.currentTimeMillis();
        return TimeMillis/1000;
    }
}
