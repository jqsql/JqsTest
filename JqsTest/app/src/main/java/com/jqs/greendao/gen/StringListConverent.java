package com.jqs.greendao.gen;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jqs on 2017/12/12.
 */

public class StringListConverent implements PropertyConverter<List<String>,String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        String[] str=databaseValue.split(",");
        if(str!=null)
            return Arrays.asList(str);
        return null;
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        StringBuilder stringBuilder=new StringBuilder("");
        for (String str: entityProperty) {
            stringBuilder.append(str+",");
        }
        if(stringBuilder.length()!=0)
            return stringBuilder.substring(0,stringBuilder.length()-1);
        return "";
    }
}
