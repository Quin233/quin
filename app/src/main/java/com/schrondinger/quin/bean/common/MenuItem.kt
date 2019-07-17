package com.schrondinger.quin.bean.common

import java.io.Serializable

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/17 9:41 AM
 * 描    述：recycleView的通用数据源
 * 修订历史：2018/1/4 3:44 PM
 * ================================================
 */
data class MenuItem<T>( val data:T?, val needLogin:Boolean, val dataType:Int) : Serializable{

    constructor(dataType: Int) : this(null,false,dataType)
}