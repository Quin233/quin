package com.schrondinger.quin.ui.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.Utils.encrypt.MD5Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPActivity;
import com.schrondinger.quin.mvp.constract.LoginConstract;
import com.schrondinger.quin.mvp.model.LoginModel;
import com.schrondinger.quin.mvp.presenter.LoginPresenter;
import com.schrondinger.quin.ui.register.RegisterActivity;
import com.schrongder.nba.kotlin.TopToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.schrondinger.quin.Utils.StatusBarUtil.StatusBarLightMode;
import static com.schrondinger.quin.Utils.StatusBarUtil.transparencyBar;

@ActivityInject(rootViewId = R.layout.activity_login)
public class LoginActivity extends BaseMVPActivity<LoginPresenter,LoginModel> implements LoginConstract.View {
    @BindView(R.id.et_username)
    EditText mEt_userName;
    @BindView(R.id.et_password)
    EditText mEt_password;
    @BindView(R.id.iv_isRemenber)
    ImageView mIv_isRemenber;
    @BindView(R.id.tv_isRemenber)
    TextView mTv_isRemenber;
    @BindView(R.id.tv_fogot_pwd)
    TextView mTv_fogotPwd;
    @BindView(R.id.btn_login)
    Button mbtn_login;
    @BindView(R.id.tv_register)
    Button mTv_register;
    @BindView(R.id.tv_fogot_username)
    TextView mTv_fogotUsername;

    private String phone;
    private String pwd;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        transparencyBar(this);
        StatusBarLightMode(this);
        if (SpUtil.getString(SpUtil.ISLODINSELF).equals("YES")){
            mIv_isRemenber.setImageResource(R.drawable.login_select_a);
            mEt_userName.setText(SpUtil.getString(SpUtil.USERPHONE));
        }else{
            mIv_isRemenber.setImageResource(R.drawable.login_select_b);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        mTv_register.setOnClickListener(this);
        mTv_isRemenber.setOnClickListener(this);
        mIv_isRemenber.setOnClickListener(this);
        mbtn_login.setOnClickListener(this);
        mTv_fogotUsername.setOnClickListener(this);
        mTv_fogotPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
//                if (check()){
//                    Map<String,String> sendMap = new HashMap<>();
//                    sendMap.put("userName",phone);
//                    sendMap.put("userPwd",pwd);
//                    mPresenter.login(sendMap);
//                }
                jumpToMain();
                break;
            case R.id.tv_register:
                toActivity(RegisterActivity.class);
                break;
            case R.id.tv_isRemenber:
                if (SpUtil.getString(SpUtil.ISLODINSELF).equals("YES")){
                    mIv_isRemenber.setImageResource(R.drawable.login_select_b);
                    SpUtil.setString(SpUtil.ISLODINSELF,"NO");
                }else{
                    mIv_isRemenber.setImageResource(R.drawable.login_select_a);
                    SpUtil.setString(SpUtil.ISLODINSELF,"YES");
                }
                break;
            case R.id.tv_fogot_username:

                break;
            case R.id.tv_fogot_pwd:

                break;
        }
    }

    private boolean check(){
        TopToast topToast = new TopToast(this);
        phone = mEt_userName.getText().toString().trim();
        pwd = mEt_password.getText().toString().trim();

        if (Util.isNullOrEmpty(phone)){
            topToast.showType(TopToast.Error).setMessageText("请输入账号！").duration(TopToast.LENGTH_SHORT).show();
            return false;
        }else if (Util.isNullOrEmpty(pwd)){
            topToast.showType(TopToast.Error).setMessageText("请输入密码！").duration(TopToast.LENGTH_SHORT).show();
            return false;
        }else {
            pwd = MD5Util.getMD5String(pwd);
            return true;
        }
    }

    @Override
    public void onRequestStart(int tag) {

    }

    @Override
    public void onInternetError() {

    }

    @Override
    public void jumpToMain() {
        finish();
    }
}
