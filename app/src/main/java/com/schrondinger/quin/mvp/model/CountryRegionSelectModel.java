package com.schrondinger.quin.mvp.model;

import android.content.Context;


import com.schrondinger.quin.bean.CountryRegionResult;
import com.schrondinger.quin.bean.Result;
import com.schrondinger.quin.https.Retrofit.RxUtil;
import com.schrondinger.quin.https.api.LocalApi;
import com.schrondinger.quin.mvp.constract.CountryRegionSelectConstrct;

import io.reactivex.Observable;


/**
 * Created by HP on 2018/1/15.
 */

public class CountryRegionSelectModel implements CountryRegionSelectConstrct.Model {

    @Override
    public Observable<CountryRegionResult> getCountryRegionList(Context context) {
        return LocalApi.getCountryRegionList(context)
                .compose(RxUtil.<Result<CountryRegionResult>>rxTransformer())
                .compose(RxUtil.<CountryRegionResult>resultTransformer());
    }
}
