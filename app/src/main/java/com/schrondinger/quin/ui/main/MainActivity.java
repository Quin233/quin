package com.schrondinger.quin.ui.main;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SpUtil;
import com.schrondinger.quin.Utils.Util;
import com.schrondinger.quin.base.activity.ActivityInject;
import com.schrondinger.quin.base.activity.BaseActivity;
import com.schrondinger.quin.common.RxManager;
import com.schrondinger.quin.ui.login.LoginActivity;
import com.schrondinger.quin.ui.main.fragment.TypeOneFragment;
import com.schrondinger.quin.ui.main.fragment.TypeThreeFragment;
import com.schrondinger.quin.ui.main.fragment.TypeTwoFragment;
import com.schrondinger.quin.widget.CircleImageView;
import com.schrondinger.quin.widget.dialog.SchrodingerDialog;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

@ActivityInject(rootViewId = R.layout.activity_main)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentTabHost.OnTabChangeListener  {
    @BindView(R.id.view_toolbar)
    Toolbar mToolBar;
    @BindView(android.R.id.tabcontent)
    FrameLayout mTabContent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;

    private View viewDrawlayoutHead;
    private CircleImageView mUserHeadIcon;
    private TextView mUserName;
    private TextView mUserInfo;

    private RxManager mRxManager;


    private LayoutInflater layoutInflater;
    private Class fragmentArray[] = {TypeOneFragment.class, TypeTwoFragment.class, TypeThreeFragment.class};
    private int imageViewArray[] = { R.drawable.tab_type_one_btn, R.drawable.tab_type_two_btn,R.drawable.tab_type_three_btn };
    private String textViewArray[] = { "主页", "动态","我的"};

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        // 初始化tabHost
        initTabHost();
        // 初始化侧拉控件
        initDrawerLayout();

        checkIsLogin();
    }

    @Override
    public void initListener() {
        super.initListener();
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        //订阅登录事件
        mRxManager = new RxManager();
        mRxManager.register(RxManager.ISLOGIN).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
//                checkIsLoagin();
            }});
    }

    private void initTabHost(){
        layoutInflater = LayoutInflater.from(this);//加载布局管理器
        /*实例化FragmentTabHost对象并进行绑定*/
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
        /*新建Tabspec选项卡并设置Tab菜单栏的内容和绑定对应的Fragment*/
        for (int i = 0; i < textViewArray.length; i++) {
            // 给每个Tab按钮设置标签、图标和文字
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(textViewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中，并绑定Fragment
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.setTag(i);
        }
    }

    private View getTabItemView(int i) {
        //将xml布局转换为view对象+
        View view = layoutInflater.inflate(R.layout.main_tab_content, null);
        //利用view对象，找到布局中的组件,并设置内容，然后返回视图
        ImageView mImageView = view.findViewById(R.id.imageview);
        TextView mTextView = view.findViewById(R.id.textview);
        mImageView.setImageResource(imageViewArray[i]);
        mTextView.setText(textViewArray[i]);
        return view;
    }

    private void initDrawerLayout(){
        viewDrawlayoutHead = navView.getHeaderView(0);
        mUserHeadIcon = viewDrawlayoutHead.findViewById(R.id.iv_user_head_icon);
        mUserName = viewDrawlayoutHead.findViewById(R.id.user_name);
        mUserInfo = viewDrawlayoutHead.findViewById(R.id.user_info);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,0,0){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Constants.loginState){
                    Util.loadImage(mUserHeadIcon,Constants.user.getUserHeadPhoto(),MainActivity.this);
                    mUserName.setText(Constants.user.getUserName());
                    mUserInfo.setText(Constants.user.getUserPhone());
                }
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true); // 改变item选中状态
//        setTitle(item.getTitle()); // 更改toolbar标题
//        currentNavigationId = item.getItemId(); // 被选中item的资源id
        mDrawerLayout.closeDrawers(); // 关闭导航菜单
        switch (item.getItemId()){
            case R.id.nav_home:
                Log.d(">>>>>>>>>>>>>>>>", "onNavigationItemSelected: 成功！");
                break;
        }
        return true;
    }


    @Override
    public void onTabChanged(String tabId) {
        mToolBar.setTitle(tabId);
    }

    private void checkIsLogin(){
        if (!Constants.loginState){
            mTabHost.setCurrentTab(0);
            if (SpUtil.getString(SpUtil.ISLODINSELF).equals("YES")){
                // 自动登陆

            }else {
                SchrodingerDialog.confirmDialog(this, "是否要登陆？", "您还未登陆QUIN，为了您更好的体验该App，请登陆或注册后登陆~", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 去登陆页面
                        toActivity(LoginActivity.class);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { }});
            }
        }
    }

}
