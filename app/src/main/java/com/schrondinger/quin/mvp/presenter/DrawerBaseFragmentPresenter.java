package com.schrondinger.quin.mvp.presenter;

import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.mvp.constract.DrawerBaseFragmentContract;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/3/1.
 */

public class DrawerBaseFragmentPresenter extends DrawerBaseFragmentContract.Presenter {
    @Override
    public void getUserInfo(Map<String, String> sendMap) {
        getMRxManager().add(getMModel().getUserInfo(sendMap).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getMView().onRequestStart(Constants.INSTANCE.getWAITING());
            }
        }).subscribe(new Consumer<User>() { // onNext
            @Override
            public void accept(User user) throws Exception {
                getMView().updateUI(user);
            }
        }, new Consumer<Throwable>() { // onError
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

                getMView().onRequestError(Constants.INSTANCE.getWAITING(),throwable.getLocalizedMessage());
            }
        }, new Action() { // onComplete
            @Override
            public void run() throws Exception {
                getMView().onRequestEnd(Constants.INSTANCE.getWAITING());
            }
        }));
    }
}
