package com.schrondinger.quin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * ================================================
 * 作    者：schrodinger
 * 版    本：1.0
 * 创建日期： 2019/1/2 4:27 PM
 * 描    述：
 * 修订历史：
 * ================================================
 */
class DrawerIconView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){

    private var xLength = 100
    private var yLength = 60
    private  var desiredWidth = xLength
    private  var desiredHeight = yLength

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize/3
        } else {
            desiredHeight = yLength + paddingTop + paddingBottom
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = Math.min(heightSize, desiredHeight)/3
            }
        }
        desiredWidth = desiredHeight*6/10

        setMeasuredDimension(desiredWidth,desiredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.strokeWidth = desiredHeight/7f
        var lines = floatArrayOf(0f,desiredHeight/7f,desiredWidth.toFloat(),desiredHeight/7f,0f,desiredHeight/2f,desiredWidth.toFloat(),desiredHeight/2f,0f,desiredHeight*6/7f,desiredWidth.toFloat(),desiredHeight*6/7f)
        canvas.drawLines(lines,paint)

    }
}