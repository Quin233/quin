package com.schrondinger.quin.mvp.constract;

import com.schrondinger.quin.base.mvp.BaseModel;
import com.schrondinger.quin.base.mvp.BasePresenter;
import com.schrondinger.quin.base.mvp.BaseView;
import com.schrondinger.quin.bean.User;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by HP on 2018/3/1.
 */

public interface TypeOneFragmentConstract {
    interface Model extends BaseModel {
        Observable<User> getUserInfo(Map<String, String> sendMap);
    }
    interface View extends BaseView {
        void updateUI(User user);
    }
    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getUserInfo(Map<String, String> sendMap);
    }
}
