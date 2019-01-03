package com.schrondinger.quin.base.activity

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/28 9:29 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
interface BaseFunction {
    /** 初始化数据方法  */
    fun initData()

    /** 初始化UI控件方法  */
    fun initView()

    /** 初始化事件监听方法  */
    fun initListener()

    /** 初始化界面加载  */
    fun initLoad()
}
