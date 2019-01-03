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
 * 创建日期： 2018/12/20 9:16 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class DrawLine @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint= Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        val points = floatArrayOf(20f, 20f, 120f, 20f, 70f, 20f, 70f, 120f, 20f, 120f, 120f, 120f, 150f, 20f, 250f, 20f, 150f, 20f, 150f, 120f, 250f, 20f, 250f, 120f, 150f, 120f, 250f, 120f)
        canvas.drawLines(points, paint)
    }
}