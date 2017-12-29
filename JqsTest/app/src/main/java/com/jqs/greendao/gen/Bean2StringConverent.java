package com.jqs.greendao.gen;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jqs on 2017/12/12.
 */

public class Bean2StringConverent implements PropertyConverter<UserInfoBean,String> {
    Gson mGson;

    public Bean2StringConverent() {
        mGson = new Gson();
    }

    @Override
    public UserInfoBean convertToEntityProperty(String databaseValue) {
        return mGson.fromJson(databaseValue,UserInfoBean.class);
    }

    @Override
    public String convertToDatabaseValue(UserInfoBean entityProperty) {
        return mGson.toJson(entityProperty);
    }
}
