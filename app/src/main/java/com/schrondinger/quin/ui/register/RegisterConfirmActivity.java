package com.schrondinger.quin.ui.register;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.Utils.encrypt.MD5Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPActivity;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.mvp.constract.RegisterConfirmConstract;
import com.schrondinger.quin.mvp.model.RegisterConfirmModel;
import com.schrondinger.quin.mvp.presenter.RegisterConfirmPresenter;
import com.schrongder.nba.kotlin.TopToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


@ActivityInject(rootViewId = R.layout.activity_register_confirm)
public class RegisterConfirmActivity extends BaseMVPActivity<RegisterConfirmPresenter, RegisterConfirmModel> implements RegisterConfirmConstract.View {

    @BindView(R.id.view_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRl_Toolbar;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwdTwo)
    EditText mEtPwdT;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    // 参数部分
    private String userPhone;
    private String userpwd;
    private String userpwd_t;
    private String country;
    private String countryCode;

    @Override
    public void initData() {
        super.initData();
        userPhone = getIntent().getStringExtra("userPhone");
        country = getIntent().getStringExtra("country");
        countryCode = getIntent().getStringExtra("countryCode");
    }

    @Override
    public void initView() {
        super.initView();
        setToolBar(mToolbar);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        // 展示验证码
    }

    @Override
    public void initListener() {
        super.initListener();
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if (checkData()){
                    Map<String,String> sendMap = new HashMap();
                    sendMap.put("country",country);
                    sendMap.put("countryCode",countryCode);
                    sendMap.put("userPhone",userPhone);
                    sendMap.put("userPwd", MD5Util.getMD5String(userpwd));
                    mPresenter.register(sendMap);
                }
        }
    }

    @Override
    public void onRequestStart(int tag) {
        if (tag == Constants.INSTANCE.getWAITING()){
//            getMLoadingView().show();
        }
    }

    @Override
    public void onInternetError() {

    }

    @Override
    public void updateUI(User user) {
        toActivity(RegisterResultActivity.class);
    }

    private boolean checkData(){
        userpwd = mEtPwd.getText().toString().trim();
        userpwd_t = mEtPwdT.getText().toString().trim();
        TopToast topToast = new TopToast(this);
        if (Util.isNullOrEmpty(userpwd)){
            topToast.showType(TopToast.Error).setMessageText("请填写密码！").duration(TopToast.LENGTH_SHORT).show();
            return false;
        }else if (Util.isNullOrEmpty(userpwd_t)){
            topToast.showType(TopToast.Error).setMessageText("请再次填写密码！").duration(TopToast.LENGTH_SHORT).show();
            return false;
        }else if (!userpwd.equals(userpwd_t)){
            topToast.showType(TopToast.Error).setMessageText("请确认两次输入的密码一致！").duration(TopToast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}
