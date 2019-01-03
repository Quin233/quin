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
 * 创建日期： 2018/12/20 9:40 AM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class DrawArc @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var paint= Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL // 填充模式
        canvas?.drawArc(200f, 100f, 800f, 500f, -110f, 100f, true, paint) // 绘制扇形（到中心点）
        canvas?.drawArc(200f, 100f, 800f, 500f, 20f, 140f, false, paint) // 绘制弧形（不到中心点）
        paint.style = Paint.Style.STROKE // 画线模式
        canvas?.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, paint) // 绘制不封口的弧形
    }
}