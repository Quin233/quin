package com.schrondinger.quin.ui.mine;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.App;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SnackBarUtil;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.common.RxManager;
import com.schrondinger.quin.widget.CircleImageView;
import com.schrondinger.quin.widget.dialog.SchrodingerDialog;
import com.schrondinger.quin.widget.dialog.UserHeadSelectDialog;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_user_info)
public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.view_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rl_user_head)
    RelativeLayout mRlUserHead;
    @BindView(R.id.rl_user_name)
    RelativeLayout mRlUserName;
    @BindView(R.id.rl_user_gender)
    RelativeLayout mRlUserGender;
    @BindView(R.id.rl_user_qianming)
    RelativeLayout mRlUserSignature;

    @BindView(R.id.user_head)
    CircleImageView mUserHead;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_id)
    TextView mUserId;
    @BindView(R.id.user_gender)
    TextView mUserGender;
    @BindView(R.id.user_qianming)
    TextView mUserSignature;
    @BindView(R.id.out)
    TextView mLoginOut;
    private RxManager mRxManager;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        setBar(mToolbar);
        initViewData();
    }

    @Override
    public void initListener() {
        super.initListener();
        mLoginOut.setOnClickListener(this);
        mRlUserHead.setOnClickListener(this);
        mRlUserName.setOnClickListener(this);
        mRlUserGender.setOnClickListener(this);
        mRlUserSignature.setOnClickListener(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.out:
                loginOut();
                break;
            case R.id.rl_user_head:
                showHeadSelect();
                break;
            case R.id.rl_user_name:
                // 更改名字
                break;
            case R.id.rl_user_gender:
                // 性别
                break;
            case R.id.rl_user_qianming:
                // 个性签名
                break;
        }
    }

    private void loginOut(){
        SchrodingerDialog.confirmDialog(this,"退出登陆", "您确定要退出登录吗？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.clearCookies();
                Constants.loginState = false;
                App.getInstance().removeActivity(2);
                //post订阅事件
                mRxManager = new RxManager();
                mRxManager.post(RxManager.ISLOGIN,new User());

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {}});
    }

    private void initViewData(){
        if (Util.isNullOrEmpty(Constants.user.getUserName())){
            mUserName.setText("竟然还没有名字");
        }else{
            mUserName.setText(Constants.user.getUserName());
        }
        if (Util.isNullOrEmpty(Constants.user.getUserHeadPhoto())){
            mUserHead.setImageResource(R.mipmap.head_default);
        }else {
            Glide.with(this).load(Constants.user.getUserHeadPhoto()).into(mUserHead);
        }
        if (Util.isNullOrEmpty(Constants.user.getGender())){
            mUserGender.setText("");
        } else if (Constants.user.getGender().equals("F")){
            mUserGender.setText("女");
        }else if (Constants.user.getGender().equals("M")){
            mUserGender.setText("男");
        }else{
            mUserGender.setText("");
        }
        mUserId.setText(Constants.user.getUid()+"");
        if (Util.isNullOrEmpty(Constants.user.getInfo())){
            mUserSignature.setText("这个人很懒，什么都没有写。");
        }else{
            mUserSignature.setText(Constants.user.getInfo());
        }
    }

    private void showHeadSelect(){
        UserHeadSelectDialog dialog = new UserHeadSelectDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackBarUtil.ShortSnackbar(mLoginOut,"去拍照！", Snackbar.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(UserHeadPhotoActivity.class);
            }
        });
        dialog.show();
    }
}
