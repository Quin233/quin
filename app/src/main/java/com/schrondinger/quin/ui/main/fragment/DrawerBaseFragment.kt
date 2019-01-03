package com.schrondinger.quin.ui.main.fragment

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Constants
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseMVPFragment
import com.schrondinger.quin.bean.User
import com.schrondinger.quin.mvp.constract.DrawerBaseFragmentContract
import com.schrondinger.quin.mvp.model.DrawerBaseFragmentModel
import com.schrondinger.quin.mvp.presenter.DrawerBaseFragmentPresenter
import com.schrondinger.quin.ui.other.OtherFunctionActivity
import kotlinx.android.synthetic.main.main_activity_drawlayout_head.*

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/2 3:19 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.fragment_type_three)
class DrawerBaseFragment : BaseMVPFragment<DrawerBaseFragmentPresenter, DrawerBaseFragmentModel>(), DrawerBaseFragmentContract.View {



    override fun initView() {
        super.initView()
    }


    override fun updateUI(user: User) {

    }
}