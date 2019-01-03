package com.schrondinger.quin.base.mvp

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import com.schrondinger.quin.common.RxManager


/**
 * Created by hp on 2018/1/3.
 */

abstract class BasePresenter<M, V> {
    var context: Context? = null
    var mModel: M? = null
    var mView: V? = null
    var mRxManager = RxManager()

    lateinit var lifecycleOwner: LifecycleOwner

    fun setVM(v: V?, m: M?) {
        this.mView = v
        this.mModel = m
    }


    fun onDestroy() {
        mRxManager.clear()
    }
}
