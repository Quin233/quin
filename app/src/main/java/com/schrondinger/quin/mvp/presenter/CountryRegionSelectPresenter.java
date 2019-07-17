package com.schrondinger.quin.mvp.presenter;

import android.content.Context;

import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.bean.CountryRegionResult;
import com.schrondinger.quin.mvp.constract.CountryRegionSelectConstrct;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by HP on 2018/1/15.
 */

public class CountryRegionSelectPresenter extends CountryRegionSelectConstrct.Presenter {

    @Override
    public void getCountryRegionList(Context context) {
        getMRxManager().add(getMModel().getCountryRegionList(context).doOnSubscribe(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getMView().onRequestStart(Constants.INSTANCE.getWAITING());
            }
        }).subscribe(new Consumer<CountryRegionResult>() { // onNext
            @Override
            public void accept(CountryRegionResult countryRegionResult) throws Exception {
                getMView().updateList(countryRegionResult);
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
