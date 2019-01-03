package com.schrondinger.quin.ui.other.customview.custom2

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.Util
import com.schrondinger.quin.base.activity.ActivityInject
import com.schrondinger.quin.base.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view2.*

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/20 3:58 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
@ActivityInject(rootViewId = R.layout.activity_custom_view2)
class CustomView2Activity : BaseActivity() {
    override fun initView() {
        super.initView()
        setToolBar(view_toolbar)
    }

    override fun initListener() {
        super.initListener()
        btn_modify_up.setOnClickListener(this)
        btn_modify_down.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_modify_up->{
                if (Util.isNullOrEmpty(et_width_up.text.toString()) || Util.isNullOrEmpty(et_height_up.text.toString())){
                    Toast.makeText(this,"请输入宽高！",Toast.LENGTH_LONG).show()
                }else{
                    var width = et_width_up.text.toString().toFloat()
                    var height = et_height_up.text.toString().toFloat()
                    var params = ig_first_image.layoutParams as LinearLayout.LayoutParams
                    params.width = Util.dip2px(this,width)
                    params.height = Util.dip2px(this,height)
                    ig_first_image.layoutParams = params
                }
            }
            R.id.btn_modify_down->{
                if (Util.isNullOrEmpty(et_width_down.text.toString()) || Util.isNullOrEmpty(et_height_down.text.toString())){
                    Toast.makeText(this,"请输入宽高！",Toast.LENGTH_LONG).show()
                }else{
                    var width = et_width_down.text.toString().toFloat()
                    var height = et_height_down.text.toString().toFloat()
                    var params = sig_second_image.layoutParams as LinearLayout.LayoutParams
                    params.width = Util.dip2px(this,width)
                    params.height = Util.dip2px(this,height)
                    sig_second_image.layoutParams = params
                }
            }
        }
    }


}
