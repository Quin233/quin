package com.schrondinger.quin.ui.main;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.StatusBarUtil;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;
import com.schrondinger.quin.ui.main.adapter.ViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2017/12/17 9:29 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.btn_go)
    Button mBtn_go;

    private ViewPagerAdapter mAdapter;
    private ArrayList<View> views;
    private int imgs[]=new int[]{R.drawable.welcome_1,R.drawable.welcome_2,R.drawable.welcome_3};

    @Override
    public void initView() {
        super.initView();
        StatusBarUtil.transparencyBar(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        views=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.welcome_viewpager_item, null);
            views.add(view);
        }
        mAdapter=new ViewPagerAdapter(views,imgs,this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    mBtn_go.setVisibility(View.VISIBLE);
                }else {
                    mBtn_go.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        mBtn_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_go:
                SpUtil.setBoolean(SpUtil.ISFIRST,false);
                toActivity(MainActivity.class);
                this.finish();
                break;
        }
    }
}
