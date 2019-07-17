package com.schrondinger.quin.ui.common.adaper

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.*

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/3 4:53 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class ImageViewPagerAdapter : PagerAdapter {
    private var context:Context
    private var imgs:IntArray
    private var onViewPagerClickListener: OnViewPagerClickListener? = null

    constructor(imgs: IntArray, context: Context):super(){
        this.context = context
        this.imgs = imgs
    }

    override fun getCount(): Int {
        if (imgs.isEmpty())return 0
        return imgs.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(imgs[position])

        imageView.setOnClickListener { onViewPagerClickListener?.onClick(position) }
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    interface OnViewPagerClickListener {
        fun onClick(position: Int)
    }
    fun setOnViewPagerClickListener(onViewPagerClickListener: OnViewPagerClickListener) {
        this.onViewPagerClickListener = onViewPagerClickListener
    }

}