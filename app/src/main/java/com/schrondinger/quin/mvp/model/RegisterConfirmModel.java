package com.schrondinger.quin.mvp.model;



import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.mvp.constract.RegisterConfirmConstract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by HP on 2018/1/3.
 */

public class RegisterConfirmModel implements RegisterConfirmConstract.Model {
    @Override
    public Observable<User> register(Map<String, String> sendMap) {
        return null;
    }
}
