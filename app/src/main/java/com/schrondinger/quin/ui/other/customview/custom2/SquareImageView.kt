package com.schrondinger.quin.ui.other.customview.custom2

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.orhanobut.logger.Logger

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/20 3:59 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class SquareImageView @JvmOverloads  constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): ImageView(context, attrs, defStyleAttr){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measureWidth = measuredWidth
        var measureHeight = measuredHeight
        if (measureWidth<measureHeight){
            measureHeight = measureWidth
        }else{
            measureWidth = measureHeight
        }
        setMeasuredDimension(measureWidth,measureHeight)
    }
}