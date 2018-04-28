package com.schrondinger.quin.ui.register;
import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseMVPActivity;
import com.schrondinger.quin.bean.CountryRegionResult;
import com.schrondinger.quin.mvp.constract.CountryRegionSelectConstrct;
import com.schrondinger.quin.mvp.model.CountryRegionSelectModel;
import com.schrondinger.quin.mvp.presenter.CountryRegionSelectPresenter;
import com.schrondinger.quin.ui.register.pendent.CountryListAdapter;
import com.schrondinger.quin.ui.register.pendent.PinnedHeaderDecoration;
import com.schrondinger.quin.widget.WaveSideBarView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_country_region_select)
public class CountryRegionSelectActivity extends BaseMVPActivity<CountryRegionSelectPresenter,CountryRegionSelectModel> implements CountryRegionSelectConstrct.View {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.side_view)
    WaveSideBarView mSideBarView;
    CountryListAdapter mCountryListAdapter;

    private List<CountryRegionResult.CountryRegionListBean> list;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initLoad() {
        super.initLoad();
        // 解析XML获得数据源
        mPresenter.getCountryRegionList(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        back.setOnClickListener(this);
    }


    @Override
    public void onRequestStart(int tag) {
        if (tag == Constants.WAITING){
            mLoadingView.show();
        }
    }

    @Override
    public void onInternetError() {

    }

    @Override
    public void updateList(CountryRegionResult countryRegionResult) {
        list =  countryRegionResult.getCountryRegionList();
        // 初始化RecycleListView
        initList();
        // 初始化slidebar
        initSlideBar();
    }

    private void initList(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
        mCountryListAdapter = new CountryListAdapter(this, list);
        mRecyclerView.setAdapter(mCountryListAdapter);
    }

    private void initSlideBar(){
        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mCountryListAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("country","美国");
                intent.putExtra("code","1");
                setResult(2001,intent);
                closeActivity();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 截获后退键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(2000,null);
        }
        return super.onKeyDown(keyCode, event);
    }
}
