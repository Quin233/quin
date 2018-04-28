package com.schrondinger.quin.mvp.model;


import com.schrondinger.quin.Utils.GsonUtils;
import com.schrondinger.quin.bean.Empty;
import com.schrondinger.quin.bean.Result;
import com.schrondinger.quin.https.Retrofit.RetrofitHelper;
import com.schrondinger.quin.https.Retrofit.RxUtil;
import com.schrondinger.quin.mvp.constract.RegisterConstract;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by HP on 2018/1/3.
 */

public class RegisterModel implements RegisterConstract.Model {
    @Override
    public Observable<Empty> register(Map<String, String> sendMap) {
        return RetrofitHelper.getInstance().getApi().checkRegister(GsonUtils.getGsonData(sendMap))
                .compose(RxUtil.<Result<Empty>>rxTransformer())
                .compose(RxUtil.<Empty>resultTransformer());
    }
}
