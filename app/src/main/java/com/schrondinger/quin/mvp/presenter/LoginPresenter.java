package com.schrondinger.quin.mvp.presenter;

import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.common.RxManager;
import com.schrondinger.quin.mvp.constract.LoginConstract;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/1/3.
 */

public class LoginPresenter extends LoginConstract.Presenter {
    @Override
    public void login(Map<String, String> sendMap) {
        mRxManager.add(mModel.login(sendMap).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                mView.onRequestStart(Constants.WAITING);
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
                mRxManager.post(RxManager.ISLOGIN,user);
                mView.jumpToMain();
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
