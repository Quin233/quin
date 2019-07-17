package com.schrondinger.quin.ui.other

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import com.schrondinger.quin.bean.common.CommItem
import com.schrondinger.quin.ui.login.LoginActivity
import com.schrondinger.quin.bean.common.MenuItem
import com.schrondinger.quin.ui.other.adapter.OtherFunctionAdapter
import com.schrondinger.quin.ui.other.customview.CustomViewActivity
import kotlinx.android.synthetic.main.activity_other_function.*
import java.text.FieldPosition
import java.util.ArrayList

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/17 9:29 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.activity_other_function)
class OtherFunctionActivity : BaseActivity() {

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
        allFunctionItems.add(MenuItem(CommItem(0,"类别", ""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_TITLE.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "自定义View", CustomViewActivity::class.java.name), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "查询2", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "查询3", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "查询4", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "查询5", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(0,"",""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal))

        allFunctionItems.add(MenuItem(CommItem(0,"设置", ""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_TITLE.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "设置1", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "设置2", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "设置3", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "设置4", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "设置5", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(0,"",""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal))

        allFunctionItems.add(MenuItem(CommItem(0,"生活", ""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_TITLE.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活1", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活2", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活3", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活4", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活5", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(R.drawable.icon_all_fund, "生活6", ""), false, OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_MENU.ordinal))
        allFunctionItems.add(MenuItem(CommItem(0,"",""),false,OtherFunctionAdapter.ITEM_TYPE.ITEM_TYPE_DIVIDER.ordinal))

        // 设置列表样式
        linearLayoutManager = GridLayoutManager(this, 4)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return mAdapter.getSpanSize(position)
            }
        }
        // 将样式设置到ry中
        ry_all_function.layoutManager = linearLayoutManager
        // 设置adapter
        mAdapter = OtherFunctionAdapter(allFunctionItems)
        // 将adapter设置到ry中
        ry_all_function.adapter = mAdapter
        mAdapter.setOnMenuItemClickListener(object :OtherFunctionAdapter.OnMenuItemClickListener{
            override fun onMenuClick(view: View, position: Int) {
                var data = allFunctionItems[position]
                if (!Constants.loginState && data.needLogin){
                    toActivity(LoginActivity::class.java)
                }else{
                    try {
                        val clazz = Class.forName((data.data as CommItem).activity) as Class<BaseActivity>
                        toActivity(clazz)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


}
