package com.schrondinger.quin.ui.mine;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;
import com.schrondinger.quin.bean.common.CommMap;
import com.schrondinger.quin.ui.mine.adapter.ContentPagerAdapter;
import com.schrondinger.quin.ui.mine.fragment.MineOneFragment;
import com.schrondinger.quin.ui.mine.fragment.MineThreeFragment;
import com.schrondinger.quin.ui.mine.fragment.MineTwoFragment;
import com.schrondinger.quin.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;

@ActivityInject(rootViewId = R.layout.activity_mine)
public class MineActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener  {
    @BindView(R.id.view_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.al_toolbar)
    AppBarLayout mAlToolBar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.mine_head1)
    RelativeLayout mRlHead1;
    @BindView(R.id.mine_head2)
    RelativeLayout mRlHead2;
    @BindView(R.id.tl_tab)
    TabLayout mTabTl;
    @BindView(R.id.vp_content)
    ViewPager mContentVp;

    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_type)
    TextView mUserType;
    @BindView(R.id.mine_im_head)
    CircleImageView mUserHead;
    @BindView(R.id.user_gender)
    ImageView mUserGender;
    @BindView(R.id.user_article)
    TextView mUserArticle;
    @BindView(R.id.user_qianmingdang)
    TextView mUserSignature;
    @BindView(R.id.user_info)
    TextView mUserInfo;


//    private String[] tabIndecators = {"主页","动态","收藏"};
    private ArrayList<CommMap> tabIndecators = new ArrayList<>();
    private Fragment[] tabFragments = {MineOneFragment.newInstance(), MineTwoFragment.newInstance(), MineThreeFragment.newInstance()};
    private ContentPagerAdapter contentPagerAdapter;
    private CollapsingToolbarLayoutState state;

    @Override
    public void initData() {
        super.initData();
        tabIndecators.add(new CommMap("主页",MineOneFragment.class.getName()));
        tabIndecators.add(new CommMap("动态",MineTwoFragment.class.getName()));
        tabIndecators.add(new CommMap("收藏",MineThreeFragment.class.getName()));
    }

    @Override
    public void initView() {
        super.initView();
        setToolBar(mToolbar);
        ImmersionBar.with(this).init(); //初始化，默认透明状态栏和黑色导航栏
        initContent();
        initTab();
//        initText();
    }

    @Override
    public void initListener() {
        super.initListener();
        mAlToolBar.addOnOffsetChangedListener(this);
        mUserInfo.setOnClickListener(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
    }

    private void initContent(){
        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(),tabIndecators);
        mContentVp.setAdapter(contentPagerAdapter);
        mContentVp.setCurrentItem(0);
        mContentVp.setOffscreenPageLimit(tabIndecators.size());
    }

    private void initTab(){
        mTabTl.setupWithViewPager(mContentVp);
        mTabTl.setTabTextColors(ContextCompat.getColor(this,R.color.tab_text_color),ContextCompat.getColor(this,R.color.colorPrimary));
        mTabTl.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
    }

    private void initText(){
        if (Util.isNullOrEmpty(Constants.user.getUserName())){
            mUserName.setText("竟然还没有名字");
        }else{
            mUserName.setText(Constants.user.getUserName());
        }
        if (Constants.user.getUserType().equals("Admin")){
            mUserType.setText("超级管理员");
        }else {
            mUserType.setText("普通会员");
        }
        if (Util.isNullOrEmpty(Constants.user.getUserHeadPhoto())){
            mUserHead.setImageResource(R.drawable.head_default);
        }else {
            Glide.with(this).load(Constants.user.getUserHeadPhoto()).into(mUserHead);
        }
        if (Util.isNullOrEmpty(Constants.user.getGender())){
            mUserGender.setImageResource(R.drawable.mix_gender);
        } else if (Constants.user.getGender().equals("F")){
            mUserGender.setImageResource(R.drawable.female);
        }else if (Constants.user.getGender().equals("M")){
            mUserGender.setImageResource(R.drawable.man);
        }else{
            mUserGender.setImageResource(R.drawable.mix_gender);
        }
        SpannableString string = new SpannableString(123+" 动态");
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(Color.parseColor("#000000"));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#90000000"));
        string.setSpan(colorSpan1, 0, string.length()-3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        string.setSpan(colorSpan2, string.length()-2, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mUserArticle.setText(string);
        if (Util.isNullOrEmpty(Constants.user.getInfo())){
            mUserSignature.setText("这个人很懒，什么都没有写。");
        }else{
            mUserSignature.setText(Constants.user.getInfo());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_info:
                toActivity(UserInfoActivity.class);
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        if (verticalOffset == 0) {
//            if (state != CollapsingToolbarLayoutState.EXPANDED) {
//                state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                collapsingToolbarLayout.setTitle("");//设置title不显示
//            }
//        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
//            if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                collapsingToolbarLayout.setTitle("测试");//设置title
//                state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
//            }
//        } else {
//            if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
//                if(state == CollapsingToolbarLayoutState.COLLAPSED){
//                }
//                state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
//            }
//        }
        if (verticalOffset <= -(mRlHead1.getHeight()+mRlHead2.getHeight()) * 0.37) {
//            mToolbar.setTitle(Constants.user.getUserName());//设置title
            mToolbar.setTitle("未登录");//设置title
        } else {
            mToolbar.setTitle("");
        }
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
