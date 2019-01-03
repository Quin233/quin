package com.schrondinger.quin.ui.other.customview.custom1

import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view1.*

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/19 4:29 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.activity_custom_view1)
class CustomView1Activity : BaseActivity() {

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

}
