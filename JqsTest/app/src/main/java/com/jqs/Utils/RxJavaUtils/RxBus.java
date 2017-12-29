package com.jqs.Utils.RxJavaUtils;

import java.util.HashMap;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * 观察者-被观察者 回调
 */

public class RxBus {
    //volatile防止模拟器优化。要直接读取
    private static volatile RxBus mRxBus;
    private final FlowableProcessor<Object> mBus;
    private HashMap<Object,CompositeDisposable> mMap;


    private RxBus() {
        mBus=PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault(){
        if(mRxBus==null){
            synchronized (RxBus.class){
                if(mRxBus==null){
                    mRxBus=new RxBus();
                }
            }
        }
        return mRxBus;
    }

    /**
     * 发送消息
     * @param o
     */
    public void post(Object o){
        mBus.onNext(o);
    }

    /**
     * 返回指定类型Flowable实例
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> Flowable<T> toFlowable(Class<T> tClass){
            return mBus.ofType(tClass);
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    public boolean haveSubscriblers(){
        return mBus.hasSubscribers();
    }

    /**
     * 保存订阅后的disposable
     * @param o
     * @param disposable
     */
    private void addDisposable(Object o,Disposable disposable){
        if(mMap==null){
            mMap=new HashMap<>();
        }
        String key=o.getClass().getName();
        if(mMap.get(key)!=null){
            mMap.get(key).add(disposable);
        }else {
            CompositeDisposable disposable1=new CompositeDisposable();
            disposable1.add(disposable);
            mMap.put(key,disposable1);
        }
    }

    /**
     * 取消订阅
     * @param o
     */
    public void unDisposable(Object o){
        if(mMap==null)
            return;
        String key=o.getClass().getName();
        //判断是否包含目标
        if(!mMap.containsKey(key))
            return;
        if(mMap.get(key)!=null)
            mMap.get(key).dispose();
        mMap.remove(key);
    }
    /***************************************使用封装*******************************************/

    /**
     * 简单封装
     * @param o
     * @param tClass
     * @param result 这个是成功的回调。没加error回调，如果要可以再加一个参数来接受error回调
     * @param <T>
     */
    public <T> void doSubscribeMain(Object o,Class<T> tClass,Consumer<T> result){
        Disposable disposable=toFlowable(tClass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result);
        addDisposable(o,disposable);
    }
}
