package com.schrondinger.quin.common;

/**
 * Created by HP on 2018/1/3.
 */

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 用于管理Rxjava相关代码
 */
public class RxManager {
    public static final String ISLOGIN="ISLOGIN";

    public RxBus mRxBus = RxBus.rxBus();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();// 管理订阅者者


    public void on(String eventName, Consumer<Object> consumer) {
        Observable<?> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeDisposable.add(mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(consumer, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }));
    }

    public void add(Disposable d) {
        mCompositeDisposable.add(d);
    }

    public void clear() {
        mCompositeDisposable.clear();// 取消订阅
    }

    public void unregister(){
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }


    public <T> Observable<T> register(@NonNull Object tag){
        return mRxBus.register(tag);
    }

    public void unregister(@NonNull Object tag){
        mRxBus.unregister(tag);
    }
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
