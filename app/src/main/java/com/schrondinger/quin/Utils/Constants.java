package com.schrondinger.quin.Utils;

import com.schrondinger.quin.bean.User;

import java.io.File;

public class Constants {
    public static User user;
    public static boolean loginState=false; //登录状态
    public static int WAITING=1; //网络请求状态：等待中... ...

    /*****************************************/

    public static String KEY = "2018SCOM"; // DES加密KEY
    public static String IV = "2018SNBA"; // DES加密CBC模式向量

    /*****************************************/
    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator;

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
}
