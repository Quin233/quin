package com.schrondinger.quin.mvp.model;



import com.schrondinger.quin.Utils.GsonUtils;
import com.schrondinger.quin.bean.Result;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.https.Retrofit.RetrofitHelper;
import com.schrondinger.quin.https.Retrofit.RxUtil;
import com.schrondinger.quin.mvp.constract.SplashConstract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by HP on 2018/1/3.
 */

public class SplashModel implements SplashConstract.Model {
    @Override
    public Observable<User> login(Map<String, String> sendMap) {
        return RetrofitHelper.getInstance().getApi().getUserInfo(GsonUtils.getGsonData(sendMap))
                .compose(RxUtil.<Result<User>>rxTransformer())
                .compose(RxUtil.<User>resultTransformer());
    }
}
