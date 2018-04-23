package com.schrondinger.quin.https.exception;

public class ApiException extends RuntimeException  {
    private String errorCode;

    public ApiException(String msg){
        super(msg);
        this.errorCode=msg;
    }

    public ApiException(String code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
