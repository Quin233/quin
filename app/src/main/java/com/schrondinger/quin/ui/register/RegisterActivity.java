package com.schrondinger.quin.ui.register;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPActivity;
import com.schrondinger.quin.bean.CountryRegionResult;
import com.schrondinger.quin.mvp.constract.RegisterConstract;
import com.schrondinger.quin.mvp.model.RegisterModel;
import com.schrondinger.quin.mvp.presenter.RegisterPresenter;
import com.schrondinger.quin.webview.ProtocolActivity;
import com.schrongder.nba.kotlin.TopToast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_register)
public class RegisterActivity extends BaseMVPActivity<RegisterPresenter, RegisterModel> implements RegisterConstract.View,AdapterView.OnItemSelectedListener {

    @BindView(R.id.view_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_countryCode)
    TextView mTv_countryCode;
    @BindView(R.id.sp_country)
    Spinner mSp_country;
    @BindView(R.id.et_phone)
    EditText mEt_phone;
    @BindView(R.id.protocol)
    TextView mTV_protocol;
    @BindView(R.id.btn_register)
    Button mBtn_register;

    private List<CountryRegionResult.CountryRegionListBean> countryList; // 国家
    private List<String> countryNameList; // 国际名称
    private ArrayAdapter<String> adapter;

    //参数部分
    private String userPhone;
    private String country;
    private String countryCode;

    @Override
    public void initData() {
        super.initData();
        countryList = new ArrayList<>();
        countryNameList = new ArrayList<>();
        countryList.add(new CountryRegionResult.CountryRegionListBean("China","中国","CN","86"));
        countryList.add(new CountryRegionResult.CountryRegionListBean("Hongkong","香港","HK","852"));
        countryList.add(new CountryRegionResult.CountryRegionListBean("Macao","澳门","MO","853"));
        countryList.add(new CountryRegionResult.CountryRegionListBean("Roreign","国外","GW","0"));
        for (int i=0;i<countryList.size();i++){
            countryNameList.add(countryList.get(i).getC_Name());
        }
        adapter = new ArrayAdapter<>(this,R.layout.register_spanner,countryNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 默认中国
        country = "中国";
        countryCode = "86";
    }

    @Override
    public void initView() {
        super.initView();
        setToolBar(mToolbar);
        initProtocol(); // 处理协议文字
        mTv_countryCode.setText("中国 +86");//默认中国
        mSp_country.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSp_country.setOnItemSelectedListener(this);
        mBtn_register.setOnClickListener(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
    }

    @Override
    public void onRequestStart(int tag) {

    }

    @Override
    public void onInternetError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                toRegisterPsw();
                break;
        }
    }

    /**
     * Spinner的选择事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
        switch (position){
            case 0:
                mTv_countryCode.setText("中国 +86");
                country = "中国";
                countryCode = "86";
                break;
            case 1:
                mTv_countryCode.setText("香港 +852");
                country = "香港";
                countryCode = "852";
                break;
            case 2:
                mTv_countryCode.setText("澳门 +853");
                country = "澳门";
                countryCode = "853";
                break;
            case 3:
                mTv_countryCode.setText("");
                toActivityForResult(CountryRegionSelectActivity.class,1000);
                break;
        }
    }

    /**
     * Spinner没有选中的处理
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 校验手机号并跳转到设置个人信息界面
     */
    private void toRegisterPsw(){
        if (checkData()){
            Map<String,String> sendMap = new HashMap<>();
            sendMap.put("phone",userPhone);
            mPresenter.register(sendMap);
        }
    }

    /**
     * 处理协议文字的展示
     */
    private void initProtocol(){
        SpannableString spannableString = new SpannableString("已阅读并同意以下《法律申明及隐私权政策》、《薛定谔平台服务协议》协议,接受免除或者限制责任、诉讼管辖约定等粗体下划线标识条款，愿意同步创建薛定谔个人账户。");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showProtocol("法律申明及隐私权政策");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#2196F3"));
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showProtocol("薛定谔平台服务协议");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#2196F3"));
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan1, 8, 20, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 21, 32, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTV_protocol.setMovementMethod(LinkMovementMethod.getInstance());
        mTV_protocol.setText(spannableString);
    }
    private void showProtocol(String protocol){
        Bundle bundle = new Bundle();
        bundle.putString("title", protocol);
        bundle.putString("url", "file:///android_asset/my_protocol.html");
        toActivity(bundle, ProtocolActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 2000:
                mTv_countryCode.setText("中国 +86");
                Toast.makeText(this,"返回按键返回+2000", Toast.LENGTH_SHORT).show();
                country = "中国";
                countryCode = "86";
                mSp_country.setSelection(0,true);
                break;
            case 2001:
                mTv_countryCode.setText(data.getStringExtra("country")+" +"+data.getStringExtra("code"));
                country = data.getStringExtra("country");
                countryCode = data.getStringExtra("code");
                Toast.makeText(this,"返回按钮返回+2001",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private boolean checkData(){
        userPhone = mEt_phone.getText().toString().trim();
        if (Util.isNullOrEmpty(userPhone)){
            TopToast topToast = new TopToast(this);
            topToast.showType(TopToast.Error).setMessageText("请填写手机号！").duration(2000).show();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void toRegister() {
        Bundle bundle = new Bundle();
        bundle.putString("userPhone",userPhone);
        bundle.putString("country",country);
        bundle.putString("countryCode",countryCode);
        toActivity(bundle,RegisterConfirmActivity.class);
    }
}
