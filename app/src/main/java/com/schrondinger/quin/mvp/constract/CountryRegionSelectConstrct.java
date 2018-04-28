package com.schrondinger.quin.mvp.constract;

import android.content.Context;

import com.schrondinger.quin.base.mvp.BaseModel;
import com.schrondinger.quin.base.mvp.BasePresenter;
import com.schrondinger.quin.base.mvp.BaseView;
import com.schrondinger.quin.bean.CountryRegionResult;

import io.reactivex.Observable;


/**
 * Created by HP on 2018/1/15.
 */

public interface CountryRegionSelectConstrct {
    interface Model extends BaseModel {
        Observable<CountryRegionResult> getCountryRegionList(Context context);
    }

    interface View extends BaseView {
        void updateList(CountryRegionResult countryRegionResult);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void getCountryRegionList(Context context);
    }
}
