package com.schrondinger.quin.ui.register;


import android.view.View;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_register_result)
public class RegisterResultActivity extends BaseActivity {
    @BindView(R.id.tv_complete)
    TextView mTv_complete;

    @Override
    public void initListener() {
        super.initListener();
        mTv_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_complete:
                finish();
                break;
        }
    }
}
