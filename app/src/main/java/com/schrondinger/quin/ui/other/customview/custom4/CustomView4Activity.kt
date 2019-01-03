package com.schrondinger.quin.ui.other.customview.custom4

import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view4.*

@ActivityInject(rootViewId = R.layout.activity_custom_view4)
class CustomView4Activity : BaseActivity() {

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

    override fun initData() {
        super.initData()

        var dataList1 = arrayOf(arrayOf("1","77"),
                arrayOf("2","71"),
                arrayOf("3","80"),
                arrayOf("4","79"),
                arrayOf("5","82"),
                arrayOf("6","75"),
                arrayOf("7","90"),
                arrayOf("8","89"),
                arrayOf("9","95"),
                arrayOf("10","91"),
                arrayOf("11","83"),
                arrayOf("12","60"),
                arrayOf("13","96"),
                arrayOf("14","100"),
                arrayOf("15","53"),
                arrayOf("16","114"),
                arrayOf("17","101"),
                arrayOf("18","10"),
                arrayOf("19","30"),
                arrayOf("20","118"))
        vs_vertical_square.setData(dataList1).setMax_Parts("120",4)

        var dataList2 = arrayOf(arrayOf("A级","20"),
                arrayOf("B级","60"),
                arrayOf("C级","90"),
                arrayOf("D级","123"),
                arrayOf("E级","200"),
                arrayOf("F级","177"))
        pc_pie_chart.setData(dataList2)
    }


}
