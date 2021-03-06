package com.schrondinger.quin.ui.main.fragment

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseFragment
import com.schrondinger.quin.bean.common.CommMap
import com.schrondinger.quin.ui.main.MainActivity
import com.schrondinger.quin.ui.common.adaper.ContentFragmentPagerAdapter
import com.schrondinger.quin.ui.mine.fragment.MineOneFragment
import kotlinx.android.synthetic.main.fragment_type_one.*

@ActivityInject(rootViewId = R.layout.fragment_type_one)
class TypeOneFragment : BaseFragment() {
    private lateinit var tabList:ArrayList<CommMap>
    private var contentFragmentPagerAdapter: ContentFragmentPagerAdapter? = null
    private lateinit var tabFragments: Array<Fragment?>

    override fun initData() {
        super.initData()
        tabList = arrayListOf(CommMap("直播", MineOneFragment::class.java.name),
                CommMap("推荐", MineOneFragment::class.java.name),
                CommMap("追番", MineOneFragment::class.java.name))

        tabFragments = kotlin.arrayOfNulls(tabList.size)

        for (i in tabList.indices){
            val clazz = Class.forName(tabList[i].value) as Class<BaseFragment>
            tabFragments[i] = clazz.newInstance()
        }
    }

    override fun initView() {
        super.initView()
        initViewPager()
        initTab()
    }

    override fun initListener() {
        super.initListener()
        di_open_drawer.setOnClickListener(this)
        ci_user_head.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.di_open_drawer,R.id.ci_user_head->{
                (activity as MainActivity).openDrawer()
            }
        }
    }

    private fun initViewPager(){
        contentFragmentPagerAdapter = ContentFragmentPagerAdapter(childFragmentManager, tabList)
        vp_content.adapter = contentFragmentPagerAdapter
        vp_content.currentItem = 0
        vp_content.offscreenPageLimit = tabFragments.size
    }

    private fun initTab() {
        tl_tab.setupWithViewPager(vp_content)
        tl_tab.setTabTextColors(ContextCompat.getColor(_mActivity, R.color.tab_text_color), ContextCompat.getColor(_mActivity, R.color.colorPrimary))
        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(_mActivity,R.color.colorPrimary))
    }
}
