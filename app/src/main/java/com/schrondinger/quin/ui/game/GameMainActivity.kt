package com.schrondinger.quin.ui.game

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.common.BannerItem
import com.schrondinger.quin.bean.common.MenuItem
import com.schrondinger.quin.bean.game.GameButtonItem
import com.schrondinger.quin.ui.game.adapter.GameMainAdapter
import com.schrondinger.quin.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_game_main.*

@ActivityInject(rootViewId = R.layout.activity_game_main)
class GameMainActivity : BaseActivity() {

    private var listBanners =mutableListOf<BannerItem>()
    private var gameItems  = mutableListOf<MenuItem<Any>>()
    lateinit var linearLayoutManager: GridLayoutManager
    lateinit var mAdapter: GameMainAdapter

    override fun initData() {
        super.initData()
        if (gameItems.size>0){
            gameItems.clear()
        }
        // 轮播图
        listBanners.add(BannerItem(R.drawable.ic_banner_1, "", ""))
        listBanners.add(BannerItem(R.drawable.ic_banner_2, "", ""))
        listBanners.add(BannerItem(R.drawable.ic_banner_3, "", ""))
        gameItems.add(MenuItem(listBanners,false, GameMainAdapter.ITEM_TYPE.ITEM_TYPE_BANNER.ordinal))


        // 按钮
        gameItems.add(MenuItem(GameButtonItem(R.drawable.nba,"NBA单队记录",GameNBAMainActivity::class.java.name), false, GameMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        gameItems.add(MenuItem(GameButtonItem(R.drawable.music_recommend,"命运-冠位指定",GameNBAMainActivity::class.java.name),false,GameMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        gameItems.add(MenuItem(GameButtonItem(R.drawable.music_list,"碧蓝航线",GameNBAMainActivity::class.java.name),false,GameMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        gameItems.add(MenuItem(GameButtonItem(R.drawable.music_charts,"雀姬麻将",GameNBAMainActivity::class.java.name),false,GameMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        gameItems.add(MenuItem(GameButtonItem(R.drawable.music_charts,"Nier机械纪元",GameNBAMainActivity::class.java.name),false,GameMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))

    }

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
        sr_refresh.setColorSchemeResources(R.color.colorPrimary)
    }

    override fun initLoad() {
        super.initLoad()
        // 设置列表样式
        linearLayoutManager = GridLayoutManager(this, 5)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return mAdapter.getSpanSize(position)
            }
        }
        // 将样式设置到ry中
        recyclerView.layoutManager = linearLayoutManager
        // 设置adapter
        mAdapter = GameMainAdapter(this,gameItems)
        // 将adapter设置到ry中
        recyclerView.adapter = mAdapter

        mAdapter.setOnBannerItemClickListener(object :GameMainAdapter.OnBannerItemClickListener{
            override fun onBannerClick(position: Int,needLogin:Boolean) {
                if (!Constants.loginState && needLogin) {
                    toActivity(LoginActivity::class.java)
                } else {

                }
            }

        })

        mAdapter.setOnMenuItemClickListener(object :GameMainAdapter.OnMenuItemClickListener{
            override fun onMenuClick(view: View, position: Int, type:Int) {
                if (!Constants.loginState && gameItems[position].needLogin) {
                    toActivity(LoginActivity::class.java)
                } else {
                    try {
                        val clazz = Class.forName((gameItems[position].data as GameButtonItem).activity) as Class<BaseActivity>
                        toActivity(clazz)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }




}
