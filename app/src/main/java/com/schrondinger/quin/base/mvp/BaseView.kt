package com.schrondinger.quin.base.mvp

/**
 * Created by hp on 2017/1/3.
 */

interface BaseView {
    fun onRequestStart(tag: Int)
    fun onRequestError(tag: Int, msg: String)
    fun onRequestEnd(tag: Int)
    fun onInternetError()
}
