package com.schrondinger.quin.ui.main;

import android.Manifest;
import android.os.Build;
import android.view.View;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.StatusBarUtil;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPActivity;
import com.schrondinger.quin.mvp.constract.SplashConstract;
import com.schrondinger.quin.mvp.model.SplashModel;
import com.schrondinger.quin.mvp.presenter.SplashPresenter;
import com.schrondinger.quin.widget.CountDownView;
import com.schrondinger.quin.widget.dialog.SchrodingerDialog;
import com.schrongder.nba.kotlin.TopToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

@ActivityInject(rootViewId = R.layout.activity_splash,title = R.string.app_name)
public class SplashActivity extends BaseMVPActivity<SplashPresenter, SplashModel> implements SplashConstract.View, CountDownView.CountDownTimerListener {

    @BindView(R.id.cv_countDownView)
    CountDownView mCountDownView;

    private final long TIMES = 3600;

    @Override
    public void initView() {
        super.initView();
        StatusBarUtil.transparencyBar(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        if (SpUtil.getFirstBoolean(SpUtil.ISPERMISSION) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SchrodingerDialog.showPermissionDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpUtil.setBoolean(SpUtil.ISPERMISSION, false);
                    checkLoadingPermission();
                }
            });
        } else {
            checkLoadingPermission();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        mCountDownView.setCountDownTimerListener(this);
        mCountDownView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_countDownView:
                mCountDownView.cancel();
                break;
        }
    }

    /**
     * 启动检查权限以及版本
     */
    private void checkLoadingPermission() {
        // 获取权限
        RxPermissions rxPermissions=new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean){
                    // 检查版本中......
                    // 检查完成！
                    mCountDownView.setVisibility(View.VISIBLE);
                    mCountDownView.start(TIMES);
                }else {
                    TopToast topToast = new TopToast(SplashActivity.this);
                    topToast.showType(TopToast.Error).setMessageText("请在手机设置中确认存储、电话权限开启状态。").duration(2000).show();
                }
            }
        });
    }

    @Override
    public void onRequestStart(int tag) {

    }

    @Override
    public void onInternetError() {

    }

    @Override
    public void jumpToMain() {
        toActivity(MainActivity.class,1);
    }

    @Override
    public void onStartCount() {}

    @Override
    public void onFinishCount() {
        if (SpUtil.getFirstBoolean(SpUtil.ISFIRST)) {
            // 第一次登陆，进入欢迎界面
            toActivity(WelcomeActivity.class,1);
        } else {
            if (SpUtil.getString(SpUtil.ISLODINSELF).equals("NO")){
                //清除session信息
                SpUtil.clearCookies();
            }
            toActivity(MainActivity.class,1);
        }
    }

}
