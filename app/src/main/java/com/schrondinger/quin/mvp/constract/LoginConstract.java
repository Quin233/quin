package com.schrondinger.quin.mvp.constract;


import com.schrondinger.quin.base.mvp.BaseModel;
import com.schrondinger.quin.base.mvp.BasePresenter;
import com.schrondinger.quin.base.mvp.BaseView;
import com.schrondinger.quin.bean.User;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by HP on 2018/1/3.
 */

public interface LoginConstract {
    interface Model extends BaseModel {
        Observable<User> login(Map<String, String> sendMap);
    }

    interface View extends BaseView {
        void jumpToMain();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(Map<String,String> sendMap);
    }
}
