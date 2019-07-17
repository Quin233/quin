package com.schrondinger.quin.ui.other.customview

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.common.CommItem
import com.schrondinger.quin.bean.common.MenuItem
import com.schrondinger.quin.ui.other.adapter.OtherFunctionAdapter
import com.schrondinger.quin.ui.other.customview.custom1.CustomView1Activity
import com.schrondinger.quin.ui.other.customview.custom2.CustomView2Activity
import com.schrondinger.quin.ui.other.customview.custom3.CustomView3Activity
import com.schrondinger.quin.ui.other.customview.custom4.CustomView4Activity
import kotlinx.android.synthetic.main.activity_custom_view.*
import java.util.*

@ActivityInject(rootViewId = R.layout.activity_custom_view)
class CustomViewActivity : BaseActivity() {

    var allFunctionItems = ArrayList<MenuItem<Any>>()
    lateinit var linearLayoutManager: GridLayoutManager
    lateinit var mAdapter: OtherFunctionAdapter

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

    override fun initLoad() {
        super.initLoad()
        // 初始化数据源
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "统计图", CustomView4Activity::class.java.name), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "百分比", CustomView3Activity::class.java.name), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "基础二", CustomView2Activity::class.java.name), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "基础一", CustomView1Activity::class.java.name), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))

        // 设置列表样式
        linearLayoutManager = GridLayoutManager(this, 4)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return mAdapter.getSpanSize(position)
            }
        }
        // 将样式设置到ry中
        mRecyclerView.layoutManager = linearLayoutManager
        // 设置adapter
        mAdapter = OtherFunctionAdapter(allFunctionItems)
        // 将adapter设置到ry中
        mRecyclerView.adapter = mAdapter
        mAdapter.setOnMenuItemClickListener(object :OtherFunctionAdapter.OnMenuItemClickListener{
            override fun onMenuClick(view: View, position: Int) {
                var data = allFunctionItems[position]
                try {
                    val clazz = Class.forName((data.data as CommItem).activity) as Class<BaseActivity>
                    toActivity(clazz)
                }catch (e:ClassNotFoundException){}
            }
        })
    }
}
