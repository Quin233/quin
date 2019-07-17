package com.schrondinger.quin.ui.music.fragment

import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.base.activity.BaseFragment
import com.schrondinger.quin.bean.common.MenuItem
import com.schrondinger.quin.bean.common.BannerItem
import com.schrondinger.quin.bean.music.MusicButtonItem
import com.schrondinger.quin.bean.music.MusicContentItem
import com.schrondinger.quin.bean.music.MusicTitleItem
import com.schrondinger.quin.ui.login.LoginActivity
import com.schrondinger.quin.ui.music.MusicOfMineActivity
import com.schrondinger.quin.ui.music.adapter.MusicMainAdapter
import kotlinx.android.synthetic.main.fragment_music.*


/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/3 4:37 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.fragment_music)
class MusicFragment : BaseFragment(){

    private var listBanners =mutableListOf<BannerItem>()
    private var musicItems  = mutableListOf<MenuItem<Any>>()
    lateinit var linearLayoutManager: GridLayoutManager
    lateinit var mAdapter: MusicMainAdapter

    override fun initData() {
        super.initData()
        if (musicItems.size>0){
            musicItems.clear()
        }
        // 轮播图
        listBanners.add(BannerItem(R.drawable.ic_banner_1, "", ""))
        listBanners.add(BannerItem(R.drawable.ic_banner_2, "", ""))
        listBanners.add(BannerItem(R.drawable.ic_banner_3, "", ""))
        musicItems.add(MenuItem(listBanners,false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_BANNER.ordinal))

        // 四个按钮
        musicItems.add(MenuItem(MusicButtonItem(R.drawable.music_love,"我的音乐",MusicOfMineActivity::class.java.name), false, MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        musicItems.add(MenuItem(MusicButtonItem(R.drawable.music_recommend,"推荐",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        musicItems.add(MenuItem(MusicButtonItem(R.drawable.music_list,"歌单",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        musicItems.add(MenuItem(MusicButtonItem(R.drawable.music_charts,"排行榜",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        // 分割线
        musicItems.add(MenuItem(MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal))
        // title推荐歌单
        musicItems.add(MenuItem(MusicTitleItem("推荐歌单",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_TITLE.ordinal))
        // content
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"3879万","经典老歌，久听不厌","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"88469","「galgame轻音杂物盒」 p2·沉睡的街道","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"1亿","那些好听到发疯的粤语歌","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"371万","学生时代歌词本，书写青春岁月","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"5861万","90后的回忆杀 - 「持续更新」","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"120万","BanDari(班得瑞)经典精选50首","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))

        // title最新音乐
        musicItems.add(MenuItem(MusicTitleItem("最新音乐",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_TITLE.ordinal))
        // content
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","推荐合口味的新歌","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","Coffee or Tea","李俊毅/徐圣恩",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","Undecided","Chris Brown",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","陈奕迅全新大碟","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","Dream Worlds","Immediate Music",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
        musicItems.add(MenuItem(MusicContentItem(R.drawable.bg_default_music,"","玩乐","霍尊",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal))
    }

    override fun initView() {
        super.initView()
        sr_refresh.setColorSchemeResources(R.color.colorPrimary)
    }

    override fun initListener() {
        super.initListener()
        sr_refresh.setOnRefreshListener {
            Handler().postDelayed({
                when((musicItems[7].data as MusicContentItem).text){
                    "经典老歌，久听不厌"->{
                        musicItems[7] =  MenuItem(MusicContentItem(R.drawable.bg_default_music,"4318万","『唯美纯音』不需要歌词，也可以打动你的心","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal)
                    }
                    else->{
                        musicItems[7] = MenuItem(MusicContentItem(R.drawable.bg_default_music,"3879万","经典老歌，久听不厌","",""),false,MusicMainAdapter.ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal)
                    }
                }
                mAdapter.notifyDataSetChanged()
                sr_refresh.isRefreshing = false
            }, 1000)
        }
    }

    override fun initLoad() {
        super.initLoad()
        // 设置列表样式
        linearLayoutManager = GridLayoutManager(_mActivity, 12)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return mAdapter.getSpanSize(position)
            }
        }
        // 将样式设置到ry中
        recyclerView.layoutManager = linearLayoutManager
        // 设置adapter
        mAdapter = MusicMainAdapter(_mActivity,musicItems)
        // 将adapter设置到ry中
        recyclerView.adapter = mAdapter

        mAdapter.setOnBannerItemClickListener(object :MusicMainAdapter.OnBannerItemClickListener{
            override fun onBannerClick(position: Int,needLogin:Boolean) {
                if (!Constants.loginState && needLogin) {
                    openActivity(LoginActivity::class.java)
                } else {

                }
            }

        })

        mAdapter.setOnMenuItemClickListener(object :MusicMainAdapter.OnMenuItemClickListener{
            override fun onMenuClick(view: View, position: Int,type:Int) {
                if (!Constants.loginState && musicItems[position].needLogin) {
                    openActivity(LoginActivity::class.java)
                } else {
                    try {
                        val clazz = Class.forName((musicItems[position].data as MusicButtonItem).activity) as Class<BaseActivity>
                        openActivity(clazz)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

}