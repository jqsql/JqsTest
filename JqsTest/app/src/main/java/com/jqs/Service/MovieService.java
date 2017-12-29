package com.jqs.Service;

import com.jqs.Beans.MovicesList_Bean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 影片有关接口
 */

public interface MovieService {
    /**
     * 获取电影列表接口
     * @param start
     * @param count
     * @return
     */
    @GET("top250")
    Flowable<MovicesList_Bean> getMovices(@Query("start") int start, @Query("count") int count);

}
