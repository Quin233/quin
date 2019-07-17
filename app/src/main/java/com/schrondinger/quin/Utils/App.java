package com.schrondinger.quin.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.schrondinger.quin.BuildConfig;
import com.xye.realmindextest.MyRealmModule;

import java.util.Stack;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static App instance;
    private Stack<Activity> allActivities; //activity的栈列

    public static int SCREEN_WIDTH = -1; //屏幕宽度
    public static int SCREEN_HEIGHT = -1; //屏幕高度
    public static float DIMEN_RATE = -1.0F; //
    public static int DIMEN_DPI = -1;
    private static final String COOKIE_PREFS = "Cookies_Prefs";


    public static synchronized App getInstance() {
        return instance;
    }

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化屏幕宽高
        getScreenSize();

        //初始化日志
        if (BuildConfig.DEBUG){
            Logger.init(getPackageName()).hideThreadInfo();
        }else {
            Logger.init(getPackageName()).hideThreadInfo().logLevel(LogLevel.NONE);
        }

        // 初始化Relam
        Realm.init(this);
        RealmConfiguration myConfig =new RealmConfiguration.Builder().modules(Realm.getDefaultModule(), new MyRealmModule()).build();
        Realm.setDefaultConfiguration(myConfig);

        //初始化内存泄漏检测
//        LeakCanary.install(this);

        //初始化下载模块
//        FileDownloader.init(this);

    }


    /**
     * 向栈中增加一个Activity
     * @param act 增加的Activity
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new Stack<>();
        }
        allActivities.add(act);
    }

    /**
     * 移除栈中的一个Activity
     * @param act 被移除的Activity
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
            act.finish();
        }
    }

    /**
     * 移除栈中的多个Activity
     * @param count 移除Activity的个数
     */
    public void removeActivity(int count) {
        int length=allActivities.size()-1;
        if (allActivities != null) {
            for (int i=0;i<count;i++){
                allActivities.get(length-i).finish();
                allActivities.remove(length-i);

            }
        }
    }

    /**
     * 移除名字为actName的Activity
     * @param actName 被移除的Activity的名字
     */
    public void removeActivity(String actName){
        for (Activity activity :allActivities){
            if (activity.getClass().getSimpleName().equals(actName)){
                allActivities.remove(activity);
                activity.finish();
            }
        }
    }
    /**
     * 结束指定的Activity
     * @param activity 被结束的Activity名字
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            allActivities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     * @param clazz 被结束的Activity类名
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : allActivities) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 退出App
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        //清除session信息
        SpUtil.clearCookies();

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 获取屏幕尺寸
     */
    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
}
