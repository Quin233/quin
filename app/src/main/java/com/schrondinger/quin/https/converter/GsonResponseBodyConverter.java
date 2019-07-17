package com.schrondinger.quin.https.converter;

import android.provider.SyncStateContract;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.Utils.encrypt.DESUtil;
import com.schrondinger.quin.https.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson mGson;
    private final TypeAdapter<T> adapter;

    public  final String SUCCESS="0";
    public  final String ERROR="1";

    public GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        mGson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        //处理我们的格式
        JSONObject afterJson=new JSONObject();
        try {
            //先判断是否被踢
            if (response.startsWith("<")){//被T
                afterJson.put("errorcode","role.invalid_user");
                afterJson.put("errormsg","您的账号已在其它客户端登录，故您已被强制退出!");
                afterJson.put("data","{}");
                afterJson.put("response",response.toString());
                throw new ApiException(ERROR,afterJson.toString());
            }
            JSONObject jsonObject=new JSONObject(response);
            // 解密
            jsonObject=new JSONObject(DESUtil.decodeValue(Constants.INSTANCE.getKEY(), Constants.INSTANCE.getIV(),jsonObject.optString("content")));
            //过滤一些重复字段
//            jsonObject= GsonUtils.resolveResponse(jsonObject);

            if(Util.isNullOrEmpty(jsonObject.optString("errorCode"))){//数据返回正确
                afterJson.put("errorcode", SUCCESS);
                afterJson.put("errormsg","");
                afterJson.put("data",jsonObject);
            }else{//后台报错了
                afterJson.put("errorcode",jsonObject.optString("errorCode"));
                afterJson.put("errormsg",jsonObject.optString("jsonError"));
                afterJson.put("data","{}");
                throw new ApiException(ERROR,afterJson.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //重新组装ResponseBody
        response=afterJson.toString();
        Logger.d(response);

        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
        InputStreamReader reader = new InputStreamReader(bis,charset);
        JsonReader jsonReader = mGson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
