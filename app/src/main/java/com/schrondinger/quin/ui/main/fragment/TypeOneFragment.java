package com.schrondinger.quin.ui.main.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPFragment;
import com.schrondinger.quin.bean.User;
import com.schrondinger.quin.mvp.constract.TypeOneFragmentConstract;
import com.schrondinger.quin.mvp.model.TypeOneFragmentModel;
import com.schrondinger.quin.mvp.presenter.TypeOneFragmentPresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.fragment_type_one)
public class TypeOneFragment extends BaseMVPFragment<TypeOneFragmentPresenter,TypeOneFragmentModel> implements TypeOneFragmentConstract.View  {
    @BindView(R.id.testText)
    TextView testText;
    @BindView(R.id.testButton)
    Button testButton;

    public static TypeOneFragment newInstance() {

        TypeOneFragment fragment = new TypeOneFragment();
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initListener() {
        super.initListener();
        testButton.setOnClickListener(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.testButton:
                Map<String,String> sendMap = new HashMap<>();
                sendMap.put("id","1");
                mPresenter.getUserInfo(sendMap);
                break;
        }
    }

    @Override
    public void updateUI(User user) {
        testText.setText(user.getUserName());
    }
}
