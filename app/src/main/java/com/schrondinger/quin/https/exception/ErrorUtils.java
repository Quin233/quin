package com.schrondinger.quin.https.exception;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorUtils {
    public static final String HOST20001="host.200001";//查询无记录
    public static final String HOST20002="请加挂账号信息errorCode10000";

    public static String errorMsg(String errorCode){
        String msg=errorCode;
        if (errorCode.contains("Unable to resolve host \"netbank.nccbank.com.cn\": No address associated with hostname")){
            msg="0x001亲，您的网络跑丢了,请检查网络设置";
        }else if (errorCode.contains("Failed to connect to netbank.nccbank.com.cn")){
            msg="0x002亲，您的网络跑丢了,请检查网络设置";
        }else if(errorCode.contains("SSL handshake timed out")){
            msg="0x003通讯超时";
        }else if(errorCode.contains("timed out")){
            msg="0x004请求超时";
        }else if (errorCode.contains("after 10000ms: isConnected failed"))
            msg="0x005请求超时";
        return msg;
    }

    public static String resolveErrorMsg(String msg){
        //错误信息提示出来
        if (msg.startsWith("{")){//json错误
            try {
                JSONObject jsonObject=new JSONObject(msg);
                Logger.d(jsonObject.toString());
                return jsonObject.optString("errormsg","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    public static String resolveErrorCode(String msg){
        //错误信息提示出来
        if (msg.startsWith("{")){//json错误
            try {
                JSONObject jsonObject=new JSONObject(msg);
                Logger.d(jsonObject.toString());
                return jsonObject.optString("errorcode","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }
}
