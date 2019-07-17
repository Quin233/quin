package com.schrondinger.quin.ui.music

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.base.activity.BaseFragment
import com.schrondinger.quin.bean.common.CommMap
import com.schrondinger.quin.ui.common.adaper.ContentFragmentPagerAdapter
import com.schrondinger.quin.ui.music.fragment.MusicLocalAllFragment
import kotlinx.android.synthetic.main.activity_music_of_local.*

@ActivityInject(rootViewId = R.layout.activity_music_of_local)
class MusicOfLocalActivity : BaseActivity() {
    private lateinit var tabList:ArrayList<CommMap>
    private lateinit var tabFragments: Array<Fragment?>
    private var contentFragmentPagerAdapter: ContentFragmentPagerAdapter? = null

    override fun initData() {
        super.initData()
        tabList = arrayListOf(CommMap("单曲", MusicLocalAllFragment::class.java.name),
                CommMap("歌手",MusicLocalAllFragment::class.java.name),
                CommMap("专辑",MusicLocalAllFragment::class.java.name),
                CommMap("文件夹",MusicLocalAllFragment::class.java.name))

        tabFragments = kotlin.arrayOfNulls(tabList.size)

        for (i in tabList.indices){
            val clazz = Class.forName(tabList[i].value) as Class<BaseFragment>
            tabFragments[i] = clazz.newInstance()
        }
    }

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
        initViewPager()
        initTab()
    }

    private fun initViewPager(){
        contentFragmentPagerAdapter = ContentFragmentPagerAdapter(supportFragmentManager, tabList)
        vp_content.adapter = contentFragmentPagerAdapter
        vp_content.currentItem = 0
        vp_content.offscreenPageLimit = tabFragments.size
    }

    private fun initTab() {
        tl_tab.setupWithViewPager(vp_content)
        tl_tab.setTabTextColors(ContextCompat.getColor(this, R.color.tab_text_color2), ContextCompat.getColor(this, R.color.colorWhite))
        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite))
    }



}
