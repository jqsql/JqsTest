package com.jqs.Utils.RxHttp;

import com.jqs.Beans.MovicesList_Bean;
import com.jqs.Beans.ServiceBean;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络封装请求工具类
 */

public class RxHttp {
    /**
     * 基本封装（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     */
    public static void sendTestRequest(Flowable<MovicesList_Bean> flowables, Consumer<List<MovicesList_Bean.SubjectsBean>> response, Consumer<Throwable> fail){
        flowables.map(new Function<MovicesList_Bean, List<MovicesList_Bean.SubjectsBean>>() {
            @Override
            public List<MovicesList_Bean.SubjectsBean> apply(MovicesList_Bean movicesList_bean) throws Exception {
                return movicesList_bean.getSubjects();
            }
        })/*.flatMap(new Function<List<MovicesList_Bean.SubjectsBean>, Publisher<MovicesList_Bean.SubjectsBean>>() {
            @Override
            public Publisher<MovicesList_Bean.SubjectsBean> apply(List<MovicesList_Bean.SubjectsBean> list) throws Exception {
                return Flowable.fromIterable(list);
            }
        })*/.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
    /**
     * 基本封装（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     * @param <T>
     */
    public static <T> void sendRequest(Flowable<T> flowables, Consumer<T> response,Consumer<Throwable> fail){
        flowables.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
    /**
     * 基本封装获取list（io线程-----APP程序主线程）
     * @param flowables
     * @param response
     */
    public static void sendRequestList(Flowable<ServiceBean> flowables, Consumer<Object> response,Consumer<Throwable> fail){
        flowables.filter(new Predicate<ServiceBean>() {
                    @Override
                    public boolean test(ServiceBean bean) throws Exception {
                        return bean.getCode()==0;
                    }
                })
                .flatMap(new Function<ServiceBean, Publisher<Object>>() {
                    @Override
                    public Publisher<Object> apply(ServiceBean bean) throws Exception {
                        return Flowable.fromArray(bean.getData());
                    }
                 }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response,fail);
    }
}
