package com.schrondinger.quin.ui.main.fragment


import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseFragment
import com.schrondinger.quin.base.activity.BaseMVPFragment
import com.schrondinger.quin.bean.User
import com.schrondinger.quin.bean.common.CommMap
import com.schrondinger.quin.mvp.constract.DrawerBaseFragmentContract
import com.schrondinger.quin.mvp.model.DrawerBaseFragmentModel
import com.schrondinger.quin.mvp.presenter.DrawerBaseFragmentPresenter
import com.schrondinger.quin.ui.main.MainActivity
import com.schrondinger.quin.ui.mine.adapter.ContentPagerAdapter
import com.schrondinger.quin.ui.mine.fragment.MineOneFragment
import kotlinx.android.synthetic.main.fragment_type_three.*

@ActivityInject(rootViewId = R.layout.fragment_type_three)
class TypeThreeFragment : BaseMVPFragment<DrawerBaseFragmentPresenter, DrawerBaseFragmentModel>(), DrawerBaseFragmentContract.View {

    private lateinit var tabList:ArrayList<CommMap>
    private var contentPagerAdapter: ContentPagerAdapter? = null
    private lateinit var tabFragments: Array<Fragment?>

    override fun initData() {
        super.initData()
        tabList = arrayListOf(CommMap("视频", MineOneFragment::class.java.name),
                CommMap("综合", MineOneFragment::class.java.name),
                CommMap("热门", MineOneFragment::class.java.name))

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
        contentPagerAdapter = ContentPagerAdapter(childFragmentManager, tabList)
        vp_content.adapter = contentPagerAdapter
        vp_content.currentItem = 0
        vp_content.offscreenPageLimit = tabFragments.size
    }

    private fun initTab() {
        tl_tab.setupWithViewPager(vp_content)
        tl_tab.setTabTextColors(ContextCompat.getColor(_mActivity, R.color.tab_text_color), ContextCompat.getColor(_mActivity, R.color.colorPrimary))
        tl_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(_mActivity,R.color.colorPrimary))
    }
    override fun updateUI(user: User) {

    }
}
