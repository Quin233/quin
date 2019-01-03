package com.schrondinger.quin.ui.other.adapter

import com.schrondinger.quin.R
import java.io.Serializable

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/17 9:41 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
data class MenuItem(val iconId:Int,val text:String,val secondText:String,val textColor:Int,val activity:String,val needLogin:Boolean, val dataType:Int) : Serializable{

    init {

    }

    /**
     * 有图片，文字，字体颜色，目标Activity，是否需要登录，数据类型
     */
    constructor(iconId: Int,text: String,textColor: Int,activity: String,needLogin: Boolean,dataType: Int):
            this(iconId,text,"",textColor,activity,needLogin,dataType)

    /**
     * 有图片，文字，目标Activity，是否需要登录，数据类型
     */
    constructor(iconId: Int,text: String,activity: String,needLogin: Boolean,dataType: Int):
            this(iconId,text,"", R.color.second_color,activity,needLogin,dataType)

    /**
     * 有文字，字体颜色，数据类型
     */
    constructor(text: String,textColor: Int,dataType: Int):
            this(0,text,"",textColor,"",false,dataType)

    /**
     * 有文字，数据类型
     */
    constructor(text: String,dataType: Int):
            this(0,text,"",R.color.second_color,"",false,dataType)

    /**
     * 有数据类型
     */
    constructor(dataType: Int):
            this(0,"","",R.color.second_color,"",false,dataType)
}