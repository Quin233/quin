package com.schrondinger.quin.base.activity

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：2.0
 * 创建日期： 2017/12/28 9:29 AM
 * 描    述：
 * 修订历史：2018/12/17 9:29 AM
 * ================================================
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ActivityInject(val rootViewId: Int = -1, val title: Int = -1)
