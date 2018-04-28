package com.schrondinger.quin.bean;

/**
 * Created by hp on 2018/2/28.
 */

public class Result<T> {
    private String errorcode;
    private String errormsg;
    private T data;

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
