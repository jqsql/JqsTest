package com.jqs.DaoImp;

import com.jqs.Beans.MovicesList_Bean;
import com.jqs.Dao.MovicesDao;
import com.jqs.Dao.OnResultClick;
import com.jqs.Service.MovieService;
import com.jqs.Utils.RxHttp.RxHttp;
import com.jqs.Utils.RxHttp.RxRetrofit;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by jqs on 2017/11/22.
 */

public class MovicesDaoImp implements MovicesDao{
    /**
     * 获取电影列表
     * @param start
     * @param count
     * @param listener
     */
    @Override
    public void getMovicesList(int start, int count, final OnResultClick<List<MovicesList_Bean.SubjectsBean>> listener) {
        //数据请求
        MovieService movieService= RxRetrofit.getInstance().create(MovieService.class);
        RxHttp.sendTestRequest(movieService.getMovices(start, count), new Consumer<List<MovicesList_Bean.SubjectsBean>>() {
            @Override
            public void accept(List<MovicesList_Bean.SubjectsBean> beans) throws Exception {
                listener.success(beans);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.error(throwable);
            }
        });
    }

    /**
     * 获取电影详情
     * @param start
     * @param count
     * @param listener
     */
    @Override
    public void getMovicesInformation(int start, int count, OnResultClick listener) {

    }
}
