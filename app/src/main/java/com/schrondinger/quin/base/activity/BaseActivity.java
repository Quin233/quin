package com.schrondinger.quin.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.schrondinger.quin.BuildConfig;
import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.AntiHijackingUtil;
import com.schrondinger.quin.Utils.App;
import com.schrondinger.quin.Utils.Util;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

//import static com.schrongder.nba.utils.StatusBarUtil.transparencyBar;

/**
 * Created by Schrodinger on 2017/12/28.
 */

public class BaseActivity extends SupportActivity implements BaseFunction, View.OnClickListener{

    private static final String TAG = "BaseActivity";
    private int title;

    protected Unbinder mUnbinder;

    //进程保护
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 9999:
                    if(!AntiHijackingUtil.checkActivity(BaseActivity.this)){
                        Toast.makeText(BaseActivity.this, "我的应用正在后台运行!", Toast.LENGTH_LONG).show(); //提示用户App运行在后台
                    }
                break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getClass().isAnnotationPresent(ActivityInject.class)){
            ActivityInject annotation=getClass().getAnnotation(ActivityInject.class);
            if (annotation.rootViewId()!=-1){
                setContentView(annotation.rootViewId());
            }else {
                throw new RuntimeException("rootView is null");
            }
            title=annotation.title();
        }else{
            throw new RuntimeException("getClass().isAnnotationPresent(ActivityInject.class) is failed");
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
        //View inject helper
        mUnbinder= ButterKnife.bind(this);
        App.getInstance().addActivity(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
        initView();
        initLoad();
        initListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!BuildConfig.DEBUG){
            handler.sendEmptyMessageDelayed(9999,1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initLoad() {

    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Activity跳转
     * @param clazz 目标Activity
     */
    protected void toActivity(Class<? extends Activity> clazz){
       toActivity(clazz,0);
    }

    /**
     * Activity跳转
     * @param clazz 目标Activity
     * @param closeCount 关闭Activity的个数
     */
    protected void toActivity(Class<? extends Activity> clazz,int closeCount){
        Intent intent=new Intent();
        intent.setClass(this,clazz);
        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        if (closeCount>0){
            App.getInstance().removeActivity(closeCount);
        }

    }

    /**
     * Activity跳转
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     */
    protected void toActivity(Bundle bundle,Class<? extends Activity> clazz){
        toActivity(bundle,clazz,0);
    }

    /**
     * Activity跳转
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     * @param closeCount 关闭栈中count个Activity
     */
    protected void toActivity(Bundle bundle, Class<? extends Activity> clazz,int closeCount){
        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setClass(this,clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        if (closeCount>0){
            App.getInstance().removeActivity(closeCount);
        }
    }

    /**
     * 打开目标Activity并等待返回
     * @param clazz 目标Activity
     * @param requestCode 请求码
     */
    protected void toActivityForResult(Class<? extends Activity> clazz,int requestCode){
        Bundle bundle = new Bundle();
        toActivityForResult(bundle,clazz,requestCode,false);
    }

    /**
     * 打开目标Activity并等待返回
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     * @param requestCode 请求码
     */
    protected void toActivityForResult(Bundle bundle,Class<? extends Activity> clazz,int requestCode){
        toActivityForResult(bundle,clazz,requestCode,false);
    }

    /**
     * 打开目标Activity并等待返回
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     * @param requestCode 请求码
     * @param popOut 是否关闭当前Activity
     */
    protected void toActivityForResult(Bundle bundle,Class<? extends Activity> clazz,int requestCode,boolean popOut){
        Intent intent=new Intent();
        intent.putExtras(bundle);
        intent.setClass(this,clazz);
        if (popOut){
            App.getInstance().removeActivity(this);
        }
        startActivityForResult(intent,requestCode);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    protected void closeActivity(){
        App.getInstance().removeActivity(this);
    }

    protected void setResultActivity(Bundle bundle,int tag){
        Intent intent=new Intent();
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        setResult(tag,intent);
    }

    protected void setToolBar(Toolbar toolBar){
        if (!Util.isNullOrEmpty(toolBar)){
            setBar(toolBar);
        }
    }
    protected void setToolBar(Toolbar toolBar, RelativeLayout relativeLayout, Activity activity){
        if (!Util.isNullOrEmpty(toolBar)){
            setBar(toolBar);
        }
//        transparencyBar(activity);
        if (!Util.isNullOrEmpty(relativeLayout)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Resources resources = getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
                int height = resources.getDimensionPixelSize(resourceId)+Util.dip2px(this,56);
                relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1,height));
            }
//            relativeLayout.setBackgroundResource(R.drawable.toolbar_bg);
        }
    }
    protected void setToolBarCoordinator(Toolbar toolBar, Activity activity){
        if (!Util.isNullOrEmpty(toolBar)){
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeActivity();
                }
            });
        }
//        transparencyBar(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Resources resources = getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
            int height = resources.getDimensionPixelSize(resourceId);
            CollapsingToolbarLayout.LayoutParams tl = (CollapsingToolbarLayout.LayoutParams)toolBar.getLayoutParams();
            tl.setMargins(0,height,0,0);
            toolBar.setLayoutParams(tl);
        }
    }

    protected void setBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
    }

    protected void setToolBar(Toolbar toolbar, boolean flag){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            closeActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

}
