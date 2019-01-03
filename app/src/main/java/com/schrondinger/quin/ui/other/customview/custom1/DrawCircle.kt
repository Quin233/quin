package com.schrondinger.quin.ui.other.customview.custom1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2018/12/19 5:16 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class DrawCircle @JvmOverloads constructor(context:Context, attrs:AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var paint=Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(200f,200f,100f,paint)
    }
}