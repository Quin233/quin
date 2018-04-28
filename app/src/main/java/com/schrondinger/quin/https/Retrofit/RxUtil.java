package com.schrondinger.quin.https.Retrofit;

import com.schrondinger.quin.bean.Result;
import com.schrondinger.quin.https.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hp on 2018/3/1.
 */
public class RxUtil {

    /**
     * 对 Observable 整体的变换管理，主要是考虑到所有的网络请求在io线程，ui处理在主线程。
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T,T> rxTransformer(){
        return new ObservableTransformer<T,T>(){
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一返回结果处理
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Result<T>,T> resultTransformer(){
        return new ObservableTransformer<Result<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Result<T>> resultObservable) {
                return resultObservable.flatMap(new Function<Result<T>, ObservableSource<T>>() {
                    @Override
                    public Observable<T> apply(Result<T> result) {
                        if(result.getErrorcode().equals("0")) {//数据格式正确
                            return createData(result.getData());
                        } else {
                            return Observable.error(new ApiException(result.getErrorcode(),result.getErrormsg()));
                        }
                    }
                });
            }
        };
    }

    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> observableEmitter){
                try {
                    observableEmitter.onNext(t);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        });
    }
}
