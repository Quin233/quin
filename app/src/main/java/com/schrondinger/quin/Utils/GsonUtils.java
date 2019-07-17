package com.schrondinger.quin.Utils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.schrondinger.quin.Utils.encrypt.DESUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by hp on 2018/2/28.
 */

public class GsonUtils {

    public static RequestBody getGsonData(Map<String,String> sendMap){
        if (sendMap==null){
            sendMap=new HashMap<>();
        }
        String TIME = System.currentTimeMillis()+"";
        sendMap.put("SSID",SystemUtil.getIMEI(App.getInstance().getApplicationContext()));
        sendMap.put("TIME",TIME);

        String gson=new Gson().toJson(sendMap);
        Logger.d(gson);
        String desContentStr= null;
        try {
            desContentStr = DESUtil.desEncode(Constants.INSTANCE.getKEY(), Constants.INSTANCE.getIV(),gson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject afterJson=new JSONObject();
        try {
            afterJson.put("time",TIME);
            afterJson.put("content",desContentStr);
            gson=afterJson.toString();

            Logger.d(gson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"),gson);
    }

    public static JSONObject resolveResponse(JSONObject jsonObject){
        //处理_dataMap数据
//        if (!Util.isNullOrEmpty(jsonObject.optString("_dataMap",""))){
//            String _dataMap=jsonObject.optString("_dataMap","");
//            JSONObject jsonMap = null;
//            try {
//                jsonMap=new JSONObject(_dataMap);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonMap;
//        }
        return jsonObject;
    }

//    public static Map ConvertObjToMap(Object obj){
//        Map<String,Object> reMap = new HashMap<String,Object>();
//        if (obj == null)
//            return null;
//        Field[] fields = obj.getClass().getDeclaredFields();
//        try {
//            for(int i=0;i<fields.length;i++){
//                try {
//                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
//                    if (f.getName().equals("AcBean")){//不要回传给后台
//                        continue;
//                    }
//                    f.setAccessible(true);
//                    Object o = f.get(obj);
//                    reMap.put(fields[i].getName(), o);
//                } catch (NoSuchFieldException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalArgumentException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        } catch (SecurityException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return reMap;
//    }


    /**
     * 上传参数进行key值的排序
     *
     * @param str
     */
//    public static void sort(List<String> str) {
//        String temp = "";
//        for (int i = 0; i < str.size(); i++) {
//            for (int j = i + 1; j < str.size(); j++) {
//                if (str.get(i).compareTo(str.get(j)) > 0) {
//                    temp = str.get(i);
//                    str.set(i, str.get(j));
//                    str.set(j, temp);
//                }
//            }
//        }
//    }
}
