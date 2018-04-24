package com.schrondinger.quin.base.mvp;

/**
 * Created by hp on 2017/1/3.
 */

public interface BaseView {
    void onRequestStart(int tag);
    void onRequestError(int tag, String msg);
    void onRequestEnd(int tag);
    void onInternetError();
}
