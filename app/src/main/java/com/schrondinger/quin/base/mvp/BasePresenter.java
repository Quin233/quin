package com.schrondinger.quin.base.mvp;

import android.content.Context;

import com.schrongder.nba.common.RxManager;

/**
 * Created by hp on 2018/1/3.
 */

public abstract class BasePresenter<M,V> {
    public Context context;
    public M mModel;
    public V mView;
    public RxManager mRxManager = new RxManager();

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
    }


    public void onDestroy() {
        mRxManager.clear();
    }
}
