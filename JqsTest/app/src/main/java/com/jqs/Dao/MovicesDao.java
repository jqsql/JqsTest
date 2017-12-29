package com.jqs.Dao;

import com.jqs.Beans.MovicesList_Bean;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 电影接口
 */

public interface MovicesDao {
    /**
     * 获得电影列表
     * @param start
     * @param count
     * @param listener
     */
    void getMovicesList(int start,int count,OnResultClick<List<MovicesList_Bean.SubjectsBean>> listener);

    /**
     * 获取电影详情
     * @param start
     * @param count
     * @param listener
     */
    void getMovicesInformation(int start,int count,OnResultClick listener);
}
