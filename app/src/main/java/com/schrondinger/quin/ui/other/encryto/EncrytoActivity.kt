package com.schrondinger.quin.ui.other.encryto

import com.schrodinger.jni.telecomencry.TeleComUtil
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view.view_toolbar
import kotlinx.android.synthetic.main.activity_encryto.*

@ActivityInject(rootViewId = R.layout.activity_encryto)
class EncrytoActivity : BaseActivity() {
    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

    override fun initLoad() {
        super.initLoad()
        tv_text.text = TeleComUtil().telecomEncryData("吃啥")
    }

}
