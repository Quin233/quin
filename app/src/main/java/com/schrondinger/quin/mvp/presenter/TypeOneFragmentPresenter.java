package com.schrondinger.quin.mvp.presenter;

import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.mvp.constract.TypeOneFragmentConstract;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/3/1.
 */

public class TypeOneFragmentPresenter extends TypeOneFragmentConstract.Presenter {
    @Override
    public void getUserInfo(Map<String, String> sendMap) {
        mRxManager.add(mModel.getUserInfo(sendMap).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                mView.onRequestStart(Constants.WAITING);
            }
        }).subscribe(new Consumer<User>() { // onNext
            @Override
            public void accept(User user) throws Exception {
                mView.updateUI(user);
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
