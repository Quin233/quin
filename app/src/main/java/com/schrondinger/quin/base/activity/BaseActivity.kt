package com.schrondinger.quin.base.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.schrondinger.quin.BuildConfig
import com.schrondinger.quin.R
import com.schrondinger.quin.Utils.AntiHijackingUtil
import com.schrondinger.quin.Utils.App
import com.schrondinger.quin.Utils.Util
import me.yokeyword.fragmentation.SupportActivity

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/28 9:29 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */

open class BaseActivity : SupportActivity(), BaseFunction, View.OnClickListener {
    var title1: Int = 0

    var mUnbinder: Unbinder? = null

    //进程保护
    private val handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            when (msg.what) {
                9999 -> if (!AntiHijackingUtil.checkActivity(this@BaseActivity)) {
                    Toast.makeText(this@BaseActivity, "我的应用正在后台运行!", Toast.LENGTH_LONG).show() //提示用户App运行在后台
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (javaClass.isAnnotationPresent(ActivityInject::class.java)) {
            val annotation = javaClass.getAnnotation(ActivityInject::class.java)
            if (annotation.rootViewId != -1) {
                setContentView(annotation.rootViewId)
            } else {
                throw RuntimeException("rootView is null")
            }
            title1 = annotation.title
        } else {
            throw RuntimeException("getClass().isAnnotationPresent(ActivityInject.class) is failed")
        }
        val displayMetrics = resources.displayMetrics
        displayMetrics.scaledDensity = displayMetrics.density
        //View inject helper
        mUnbinder = ButterKnife.bind(this)
        App.getInstance().addActivity(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initData()
        initView()
        initLoad()
        initListener()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (!BuildConfig.DEBUG) {
            handler.sendEmptyMessageDelayed(9999, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder?.unbind()
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initLoad() {

    }

    override fun onClick(v: View) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    /**
     * Activity跳转
     * @param clazz 目标Activity
     */
    protected fun toActivity(clazz: Class<out Activity>) {
        val intent = Intent()
        intent.setClass(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
    }

    /**
     * Activity跳转
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     */
    protected fun toActivity(bundle: Bundle, clazz: Class<out Activity>) {
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**
     * 打开目标Activity并等待返回
     * @param clazz 目标Activity
     * @param requestCode 请求码
     */
    protected fun toActivityForResult(clazz: Class<out Activity>, requestCode: Int) {
        val bundle = Bundle()
        toActivityForResult(bundle, clazz, requestCode)
    }

    /**
     * 打开目标Activity并等待返回
     * @param bundle 携带的Bundle参数
     * @param clazz 目标Activity
     * @param requestCode 请求码
     */
    private fun toActivityForResult(bundle: Bundle, clazz: Class<out Activity>, requestCode: Int) {
        val intent = Intent()
        intent.putExtras(bundle)
        intent.setClass(this, clazz)
        startActivityForResult(intent, requestCode)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    protected fun setToolBar(toolBar: Toolbar) {
        if (!Util.isNullOrEmpty(toolBar)) {
            setSupportActionBar(toolBar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
//            toolBar.title = ""
            toolBar.setNavigationOnClickListener { finish() }
        }
    }

}

