package com.schrondinger.quin.mvp.presenter;


import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.common.RxManager;
import com.schrondinger.quin.mvp.constract.SplashConstract;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/1/3.
 */

public class SplashPresenter extends SplashConstract.Presenter {
    @Override
    public void login(Map<String, String> sendMap) {
        getMRxManager().add(getMModel().login(sendMap).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getMView().onRequestStart(Constants.WAITING);
            }
        }).subscribe(new Consumer<User>() { // onNext
            @Override
            public void accept(User user) throws Exception {
                // 保存个人信息
                Constants.user = user;
                Constants.loginState=true;
                // 保存用户
                SpUtil.setString(SpUtil.USERNAME, user.getUserName());
                SpUtil.setString(SpUtil.USERPHONE, user.getUserPhone());
                SpUtil.setString(SpUtil.USERFLAG, user.getUserType());
                //post订阅消息
                getMRxManager().post(RxManager.ISLOGIN,user);
                getMView().jumpToMain();
            }
        }, new Consumer<Throwable>() { // onError
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

                getMView().onRequestError(Constants.WAITING,throwable.getLocalizedMessage());
            }
        }, new Action() { // onComplete
            @Override
            public void run() throws Exception {
                getMView().onRequestEnd(Constants.WAITING);
            }
        }));
    }
}
