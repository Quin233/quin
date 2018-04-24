package com.schrondinger.quin.common;


import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by HP on 2018/1/3.
 */
public class RxBus {
    private static RxBus instance;
    public static synchronized RxBus rxBus(){
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }
    private RxBus(){}

    private ConcurrentHashMap<Object,List<Subject>> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 订阅事件源
     * @param mObservable
     * @param consumer
     * @return
     */
    public RxBus OnEvent(Observable<?> mObservable, Consumer<Object> consumer) {
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(consumer, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return rxBus();
    }

    /**
     * 注册事件源
     * @param tag
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = concurrentHashMap.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            concurrentHashMap.put(tag, subjectList);
        }
        Subject<T> subject;
        subjectList.add(subject = PublishSubject.create());
        Logger.d("register", tag + "  size:" + subjectList.size());
        return subject;
    }

    /**
     * 取消监听
     * @param tag
     * @param observable
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RxBus unregister(@NonNull Object tag,@NonNull Observable<?> observable) {
        if (null == observable){
            return rxBus();
        }
        List<Subject> subjects = concurrentHashMap.get(tag);
        if (null != subjects) {
            subjects.remove((Subject<?>) observable);
            if (isEmpty(subjects)) {
                concurrentHashMap.remove(tag);
                Logger.d("unregister", tag + "  size:" + subjects.size());
            }
        }
        return rxBus();
    }

    @SuppressWarnings("rawtypes")
    public void unregister(@NonNull Object tag) {
        List<Subject> subjects = concurrentHashMap.get(tag);
        if (null != subjects) {
            concurrentHashMap.remove(tag);
        }
    }

    /**
     * 触发事件
     * @param content
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(@NonNull Object tag, @NonNull Object content) {
        Logger.d("post", "eventName: " + tag);
        List<Subject> subjectList = concurrentHashMap.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
                Logger.d("onEvent", "eventName: " + tag);
            }
        }
    }

    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}
