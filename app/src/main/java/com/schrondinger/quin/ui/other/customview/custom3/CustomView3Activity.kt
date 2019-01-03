package com.schrondinger.quin.ui.other.customview.custom3

import android.view.MotionEvent
import com.schrondinger.quin.R
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view3.*


@ActivityInject(rootViewId = R.layout.activity_custom_view3)
class CustomView3Activity : BaseActivity() {

    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

    override fun initListener() {
        super.initListener()
        pp_percent_picker.setOnMyMoveListener(object:PercentPicker.OnMyMoveListener{
            override fun onMove(percent: Float) {
                tv_percent.text = String.format("%.1f",(percent*100))+"%"
            }
        })
        btn_go.setOnTouchListener { view, motionEvent ->
            if (view.id == R.id.btn_go){
                when(motionEvent.action ){
                    MotionEvent.ACTION_DOWN->{
                        sp_speedometer2.start()
                    }
                    MotionEvent.ACTION_UP->{
                        sp_speedometer2.stop()
                    }
                }
            }
            true
        }
    }

}
