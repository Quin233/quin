package com.schrondinger.quin.mvp.presenter;


import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.bean.Empty;
import com.schrondinger.quin.mvp.constract.RegisterConstract;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/1/3.
 */

public class RegisterPresenter extends RegisterConstract.Presenter {
    @Override
    public void register(Map<String, String> sendMap) {
        mRxManager.add(mModel.register(sendMap).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                mView.onRequestStart(Constants.WAITING);
            }
        }).subscribe(new Consumer<Empty>() { // onNext
            @Override
            public void accept(Empty empty) throws Exception {
                mView.toRegister();
            }
        }, new Consumer<Throwable>() { // onError
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

                mView.onRequestError(Constants.WAITING,throwable.getLocalizedMessage());
            }
        }, new Action() { // onComplete
            @Override
            public void run() throws Exception {
                mView.onRequestEnd(Constants.WAITING);
            }
        }));
    }
}
