package com.schrondinger.quin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static SharedPreferences sp;
    private static SharedPreferences sp_cookie;
    private static final String APPSP="schrodinger_config";
    private static final String COOKIE_PREFS = "Cookies_Prefs";

    //标志位TAG
    public static String USERNAME="USERNAME";//用户名
    public static String USERPHONE="USERPHONE";//用户手机号
    public static String USERFLAG="USERFLAG";//用户级别
    public static String ISFIRST="ISFIRST";//是否第一次使用
    public static String ISPERMISSION="ISPERMISSION";//是否需要提示权限
    public static String ISLODINSELF="ISLODINSELF";//是否自动登陆

    static {
        sp= App.getInstance().getSharedPreferences(APPSP, Context.MODE_PRIVATE);
        sp_cookie= App.getInstance().getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
    }

    public static boolean getFirstBoolean(String tag){
        return sp.getBoolean(tag,true);
    }

    public static boolean getBoolean(String tag){
        return sp.getBoolean(tag,false);
    }

    public static void setBoolean(String tag,boolean value){
        sp.edit().putBoolean(tag,value).commit();
    }

    public static String getString(String tag){
        return sp.getString(tag,"");
    }

    public static void setString(String tag,String value){
        sp.edit().putString(tag,value).commit();
    }

    public static void clearCookies(){
        sp_cookie.edit().clear().commit();
    }
}
