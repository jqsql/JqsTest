package com.jqs.Utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 判断工具类
 */

public class Checked_Util {
    /**
     * 是否运行在模拟器上
     * @return
     */
    public static boolean isOnSimulator(Context context){
        String[] konw_ids={"000000000000000"};//默认模拟器ID
        TelephonyManager telephonyManager= (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String device_ids=telephonyManager.getDeviceId();
        for (String ids:konw_ids) {
            if(ids.equalsIgnoreCase(device_ids)){
                return true;
            }
        }
        return false;
    }
}
